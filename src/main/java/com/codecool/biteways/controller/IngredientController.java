package com.codecool.biteways.controller;


import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.dto.IngredientDto;
import com.codecool.biteways.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biteways/ingredient")
@Tag(name = "Ingredient Controller", description = "API endpoints for managing Ingredients")
@Log4j2
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @Operation(
            summary = "Create a new Ingredient entity",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The Ingredient entity was successfully created"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request")
            }
    )
    @PostMapping
    public void saveIngredient(@RequestBody Ingredient ingredient) {
        ingredientService.saveIngredient(ingredient);
    }

    @Operation(
            summary = "Retrieve all Ingredient entities",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The list of all Ingredient entities"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request")
            }
    )
    @GetMapping
    public List<IngredientDto> findAllRecipeIngredient() {
        return ingredientService.findAllIngredient();
    }

    @Operation(
            summary = "Retrieve an Ingredient entity by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The requested Ingredient entity"),
                    @ApiResponse(responseCode = "404", description = "The Ingredient entity with the specified ID was not found"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> findIngredientById(
            @PathVariable("id") Long id) throws RecordNotFoundException {
        return new ResponseEntity<>(ingredientService.findIngredientById(id), HttpStatus.FOUND);
    }

    @Operation(
            summary = "Update an Ingredient entity by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The updated Ingredient entity"),
                    @ApiResponse(responseCode = "404", description = "The Ingredient entity with the specified ID was not found"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request")
            }
    )
    @PutMapping("/{id}")
    public Ingredient updateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredientDto
    ) throws RecordNotFoundException {
        return ingredientService.updateIngredient(id, ingredientDto);
    }

    @Operation(
            summary = "Delete an Ingredient entity by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "The Ingredient entity was successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "The Ingredient entity with the specified ID was not found"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request")
            }
    )
    @DeleteMapping("/{id}")
    public void deleteIngredient(
            @PathVariable("id") Long id
    ) {
        ingredientService.deleteIngredient(id);
    }
}
