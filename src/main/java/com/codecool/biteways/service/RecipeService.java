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

        Map<String, List<String>> result = new HashMap<>();
        result.put("name", new ArrayList<>(Arrays.asList(rawRecipe.getName())));
        result.put("ingredients", Arrays.stream(rawRecipe.getIngredients().split("\\r?\\n")).map(i -> i.replaceAll("[\t\r]", " ").trim()).toList());


        result.put("preparation", Arrays.stream(rawRecipe.getPreparation().split("\\r?\\n")).map(i -> i.replaceAll("[\t\r]", " ").trim()).toList());
        return null;
    }

    public List<RecipeDto> findAllRecipe() {
        return recipeRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Recipe::getId))
                .map(this::recipeToDto)
                .collect(Collectors.toList());
    }

    public RecipeDto findRecipeById(Long id) {
        return recipeToDto(recipeRepository.findById(id).orElseThrow());
    }

    @Transactional
    public void updateRecipe(Long id, Recipe update) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        recipe.setName(update.getName());
        recipe.setIngredientList(update.getIngredientList());
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
        Map<String, Map<Float, UnitType>> ingredientList = recipe.getIngredientList()
                .stream()
                .collect(Collectors.toMap(
                        Ingredient::getName,
                        i -> {
                            Map<Float, UnitType> quantityUnit = new HashMap<>();
                            quantityUnit.put(i.getQuantity(), i.getUnitType());
                            return quantityUnit;
                        }));
        recipeDto.setIngredients(ingredientList);
        return recipeDto;
    }
}
