package com.codecool.biteways.service;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.IngredientDto;
import com.codecool.biteways.repository.IngredientRepository;
import com.codecool.biteways.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public IngredientService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    public Ingredient saveIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public List<IngredientDto> findAllIngredient() {
        return ingredientRepository.
                findAll().
                stream().
                sorted(Comparator.comparing(Ingredient::getId)).
                map(this::ingredientToDto).
                toList();
    }

    public IngredientDto findIngredientById(Long id) throws RecordNotFoundException {
        return ingredientToDto(ingredientRepository.
                findById(id).
                orElseThrow(
                        () -> new RecordNotFoundException(
                                String.format("Requested ID: %s not found!", id)
                        )
                ));
    }

    @Transactional
    public Ingredient updateIngredient(Long id, IngredientDto ingredientDto) throws RecordNotFoundException {
        Ingredient updateIngredient = ingredientRepository.
                findById(id).
                orElseThrow(() -> new RecordNotFoundException(String.format("Requested ID: %s not found!", id)));
        updateIngredient.setName(ingredientDto.getName());
        updateIngredient.setQuantity(ingredientDto.getQuantity());
        updateIngredient.setUnitType(ingredientDto.getUnitType());
        return updateIngredient;
    }

    public void deleteIngredient(Long id) throws RecordNotFoundException {
        Ingredient ingredient = ingredientRepository.
                findById(id).
                orElseThrow(
                        () -> new RecordNotFoundException(String.format("Requested ID: %s not found!", id))
                );
        ingredient.setRecipe(null);
        ingredientRepository.save(ingredient);
        ingredientRepository.deleteById(id);
    }

    public IngredientDto ingredientToDto(Ingredient ingredient) {
        return new ModelMapper().map(ingredient, IngredientDto.class);
    }

    public IngredientDto addIngredientToRecipe(Long recipeId, IngredientDto ingredientDto) throws RecordNotFoundException {
        Ingredient ingredient = new ModelMapper().map(ingredientDto, Ingredient.class);
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecordNotFoundException(String.format("Requested ID: %s not found!", recipeId)));
        ingredient.setRecipe(recipe);
        this.saveIngredient(ingredient);

        return new ModelMapper().map(ingredient, IngredientDto.class);
    }
}
