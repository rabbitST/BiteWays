package com.codecool.biteways.controller;


import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.dto.IngredientDto;
import com.codecool.biteways.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biteways/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }


    @PostMapping
    public void saveIngredient(@RequestBody Ingredient ingredient) {
        ingredientService.saveIngredient(ingredient);
    }

    @GetMapping
    public List<IngredientDto> findAllRecipeIngredient() {
        return ingredientService.findAllIngredient();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> findIngredientById(
            @PathVariable("id") Long id) throws RecordNotFoundException {
        return new ResponseEntity<>(ingredientService.findIngredientById(id), HttpStatus.FOUND);
    }

    @PutMapping("/{id}")
    public Ingredient updateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredientDto
    ) throws RecordNotFoundException {
        return ingredientService.updateIngredient(id, ingredientDto);
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(
            @PathVariable("id") Long id
    ) {
        ingredientService.deleteIngredient(id);
    }
}
