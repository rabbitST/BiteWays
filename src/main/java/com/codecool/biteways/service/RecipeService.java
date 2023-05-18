package com.codecool.biteways.service;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.RawRecipe;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.RecipeDto;
import com.codecool.biteways.model.enums.UnitType;
import com.codecool.biteways.repository.IngredientRepository;
import com.codecool.biteways.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
        Recipe r = new Recipe(rawRecipe.getName(), 1, rawRecipe.getInstructions());
        recipeRepository.save(r);
        r.setIngredientList(rawTextToIngredientList(rawRecipe, r));
        recipeRepository.save(r);
        return r;
    }

    public List<RecipeDto> findAllRecipe() {
        return recipeRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Recipe::getId))
                .map(this::recipeToDto)
                .collect(Collectors.toList());
    }

    public RecipeDto findRecipeById(Long id) throws RecordNotFoundException {
        return this.recipeToDto(recipeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(
                String.format("Requested ID: %s not found!", id)
        )));
    }

    @Transactional
    public Recipe updateRecipe(Long id, Recipe update) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        recipe.setName(update.getName());
        recipe.setInstructions(update.getInstructions());
        if(update.getIngredientList()!=null){
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
        return recipe;
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        if (recipe.getMenuList().isEmpty()){
            recipe.setMenuList(new ArrayList<>());
            recipe.setIngredientList(new ArrayList<>());
            recipeRepository.save(recipe);
            recipeRepository.deleteById(id);
        }
    }

    public RecipeDto recipeToDto(Recipe recipe) {
        List<Ingredient> sortedIngredients = recipe.getIngredientList().stream()
                .sorted(Comparator.comparing(Ingredient::getId))
                .collect(Collectors.toList());

        RecipeDto recipeDto = new ModelMapper().map(recipe, RecipeDto.class);
        recipeDto.setIngredientList(sortedIngredients);

        return recipeDto;
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
            newIngredient.setRecipe(recipe);
            processIngredientLine(line, newIngredient);
            ingredientRepository.save(newIngredient);
            ingredientList.add(newIngredient);
        }
    }
    private static void processIngredientLine(String line, Ingredient newIngredient) {
        Matcher matcher = Pattern.compile("^([0-9]+(?:[,.][0-9]+)? ?[A-Za-z]+)\\b").matcher(line);

        if (matcher.find()) {
            String unit = matcher.group().replaceAll("[^a-zA-Z]", "").toUpperCase();
            setUnitType(line, newIngredient, matcher, unit);
        } else {
            setIngredientProperties(newIngredient, line.trim(), 1f, UnitType.UNIT);
        }
    }
    private static void setUnitType(String line, Ingredient newIngredient, Matcher matcher, String unit) {
        if (isUnitTypeValid(unit)) {
            String ingredientName = line.substring(matcher.end()).trim();
            Float quantity = Float.valueOf(matcher.group().replaceAll("[^0-9\\\\,.]", "").replaceAll("[,.]","."));
            UnitType unitType = UnitType.valueOf(unit);
            setIngredientProperties(newIngredient, ingredientName, quantity, unitType);
        } else {
            setIngredientProperties(newIngredient, line.trim(), 1f, UnitType.UNIT);
        }
    }
    private static boolean isUnitTypeValid(String unit) {
        return Arrays.stream(UnitType.values()).anyMatch(u -> String.valueOf(u).equals(unit));
    }

    private static void setIngredientProperties(Ingredient newIngredient, String ingredientName, Float quantity, UnitType unitType) {
        newIngredient.setName(ingredientName);
        newIngredient.setQuantity(quantity);
        newIngredient.setUnitType(unitType);
    }
}
