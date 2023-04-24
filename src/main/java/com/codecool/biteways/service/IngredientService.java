package com.codecool.biteways.service;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.dto.IngredientDto;
import com.codecool.biteways.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }


    public void saveIngredient(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }

    public List<IngredientDto> findAllIngredient() {
        return ingredientRepository.
                findAll().
                stream().
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
        Ingredient updateIngredient = ingredientRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(String.format("Requested ID: %s not found!", id)));
        updateIngredient.setName(ingredientDto.getName());
        return updateIngredient;
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }

    public IngredientDto ingredientToDto(Ingredient ingredient) {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(ingredient.getId());
        ingredientDto.setName(ingredient.getName());
        ingredientDto.setRecipeName(ingredient.getRecipe().getName());
        return ingredientDto;
    }

}
