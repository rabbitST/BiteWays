package com.codecool.biteways.controller;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.RawRecipe;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.RecipeDto;
import com.codecool.biteways.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/biteways/recipe")
@Tag(name = "Recipe Controller", description = "API endpoints for managing Recipes")
@Log4j2
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(
            summary = "Save new Recipe entity.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the saved Recipe."),
                    @ApiResponse(responseCode = "400", description = "Invalid input data.")
            }
    )
    @PostMapping
    public ResponseEntity<?> saveRecipe(@Valid @RequestBody RawRecipe rawRecipe, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Recipe savedRecipe = recipeService.saveRecipe(rawRecipe);
            return ResponseEntity.ok(savedRecipe);
        }
    }

    @Operation(
            summary = "Fetch all Recipes from the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with all Recipes stored in th database."),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request.")
            }
    )
    @GetMapping
    public List<RecipeDto> findAllRecipe() {
        return recipeService.findAllRecipe();
    }

    @Operation(
            summary = "Return the requested Recipe entity",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns the Recipe with the requested ID."),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request.")
            }
    )
    @GetMapping(value = "/{id}")
    public RecipeDto findRecipeById(@PathVariable("id") Long id) throws RecordNotFoundException {
        return recipeService.findRecipeById(id);
    }

    @Operation(
            summary = "Update the Recipe with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "The Recipe was successfully updated"),
                    @ApiResponse(responseCode = "404", description = "The specified Recipe was not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateRecipeById(
            @PathVariable("id") Long id,
            @Valid @RequestBody Recipe recipe,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Recipe updatedRecipe = recipeService.updateRecipe(id, recipe);
            return ResponseEntity.ok(updatedRecipe);
        }
    }

    @Operation(
            summary = "Delete the Recipe with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "The Recipe was successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "The specified Recipe was not found")
            }
    )
    @DeleteMapping(value = "/{id}")
    public void deleteRecipe(@PathVariable("id") Long id) {
        recipeService.deleteRecipe(id);
    }

}
