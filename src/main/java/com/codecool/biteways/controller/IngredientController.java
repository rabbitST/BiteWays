package com.codecool.biteways.controller;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.dto.IngredientDto;
import com.codecool.biteways.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> saveIngredient(@Valid @RequestBody Ingredient ingredient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Ingredient savedIngredient = ingredientService.saveIngredient(ingredient);
            return ResponseEntity.ok(savedIngredient);
        }
    }

    @Operation(
            summary = "Retrieve all Ingredient entities",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The list of all Ingredient entities"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request")
            }
    )
    @GetMapping
    public List<IngredientDto> findAllIngredient() {
        List<IngredientDto> ingredientDtoList=ingredientService.findAllIngredient();
        System.out.println(ingredientDtoList);
        return ingredientDtoList;
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
        return new ResponseEntity<>(ingredientService.findIngredientById(id), HttpStatus.OK);
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
    public ResponseEntity<?> updateIngredient(
            @PathVariable("id") Long id,
            @Valid @RequestBody IngredientDto ingredientDto,
            BindingResult bindingResult
    ) throws RecordNotFoundException {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Ingredient updatedIngredient = ingredientService.updateIngredient(id, ingredientDto);
            return ResponseEntity.ok(updatedIngredient);
        }
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String fieldName = ex.getName();
        String fieldType = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        String invalidValue = Objects.requireNonNull(ex.getValue()).toString();
        String errorMessage = "Invalid " + fieldName + " value: " + invalidValue + ". Expected " + fieldType + ".";
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
