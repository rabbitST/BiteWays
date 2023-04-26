package com.codecool.biteways.service;

import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.RawRecipe;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.RecipeDto;
import com.codecool.biteways.model.enums.UnitType;
import com.codecool.biteways.repository.IngredientRepository;
import com.codecool.biteways.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public Recipe saveRecipe(RawRecipe rawRecipe) {
        Recipe r = new Recipe();
        r.setName(rawRecipe.getName());
        r.setInstructions(rawRecipe.getInstructions());
        r.setDownloaded(1);
        recipeRepository.save(r);
        r.setIngredientList(rawTextToIngredientList(rawRecipe, r));
        return r;
    }

    public List<RecipeDto> findAllRecipe() {
        return recipeRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Recipe::getId))
                .map(this::recipeToDto)
                .collect(Collectors.toList());
    }

    public RecipeDto findRecipeById(Long id) {
        return this.recipeToDto(recipeRepository.findById(id).orElseThrow());
    }

    @Transactional
    public void updateRecipe(Long id, Recipe update) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        recipe.setName(update.getName());
        recipe.setInstructions(update.getInstructions());
        update.
                getIngredientList().
                forEach(i -> {
                    Ingredient savedIngredient = ingredientRepository.findById(i.getId()).orElseThrow();
                    savedIngredient.setName(i.getName());
                    savedIngredient.setQuantity(i.getQuantity());
                    savedIngredient.setUnitType(i.getUnitType());
                    savedIngredient.setRecipe(recipe);
                });
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        recipe.
                getIngredientList().
                forEach(i -> {
                    i.setRecipe(null);
                    ingredientRepository.deleteById(i.getId());
                });
        recipeRepository.deleteById(id);
    }

    public RecipeDto recipeToDto(Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipe.getId());
        recipeDto.setName(recipe.getName());
        recipeDto.setDownloaded(recipe.getDownloaded());
        recipeDto.setIngredients(setIngredientMap(recipe));
        return recipeDto;
    }

    private static Map<String, Map<Float, UnitType>> setIngredientMap(Recipe recipe) {
        return recipe.
                getIngredientList()
                .stream()
                .collect(Collectors.toMap(
                        Ingredient::getName,
                        i -> {
                            Map<Float, UnitType> quantityUnit = new HashMap<>();
                            quantityUnit.put(i.getQuantity(), i.getUnitType());
                            return quantityUnit;
                        }));
    }

    public List<Ingredient> rawTextToIngredientList(RawRecipe rawRecipe, Recipe recipe) {
        List<Ingredient> ingredientList = new ArrayList<>();
        processIngredients(rawRecipe, recipe, ingredientList);
        return ingredientList;
    }

    private void processIngredients(RawRecipe rawRecipe, Recipe recipe, List<Ingredient> ingredientList) {
        String[] lines = rawRecipe.getIngredients().split("\n");
        for (String line : lines) {
            Ingredient newIngredient = new Ingredient();
            processIngredientLine(recipe, line, newIngredient);
            ingredientRepository.save(newIngredient);
            ingredientList.add(newIngredient);
        }
    }

    private static void processIngredientLine(Recipe recipe, String line, Ingredient newIngredient) {
        Matcher matcher = Pattern.compile("^([0-9]+[A-Za-z]+)\\b").matcher(line);
        if (matcher.find()) {
            String ingredientName = line.substring(matcher.end()).trim();
            Float quantity = Float.valueOf(matcher.group().replaceAll("\\D", ""));
            UnitType unitType = UnitType.valueOf(matcher.group().replaceAll("\\d", "").toUpperCase());
            setIngredientProperties(newIngredient, recipe, ingredientName, quantity, unitType);
        } else {
            setIngredientProperties(newIngredient, recipe, line.trim(), 1f, UnitType.UNIT);
        }
    }

    private static void setIngredientProperties(Ingredient newIngredient, Recipe recipe, String ingredientName, Float quantity, UnitType unitType) {
        newIngredient.setName(ingredientName);
        newIngredient.setRecipe(recipe);
        newIngredient.setQuantity(quantity);
        newIngredient.setUnitType(unitType);
    }
}
