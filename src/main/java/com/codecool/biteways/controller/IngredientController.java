package com.codecool.biteways.controller;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.dto.IngredientDto;
import com.codecool.biteways.model.enums.UnitType;
import com.codecool.biteways.service.IngredientService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
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
            summary = "Create a new Ingredient entity and add to a Recipe",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The Ingredient entity was successfully created"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request")
            }
    )
    @PostMapping(value = "/addtorecipe/{id}")
    public ResponseEntity<?> addIngredientToRecipe(
            @PathVariable("id") Long id,
            @Valid @RequestBody IngredientDto ingredientDto,
            BindingResult bindingResult) throws RecordNotFoundException {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            IngredientDto newIngredientToRecipe = ingredientService.addIngredientToRecipe(id,ingredientDto);
            return ResponseEntity.ok(newIngredientToRecipe);
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
    ) throws RecordNotFoundException {
        ingredientService.deleteIngredient(id);
    }

    @Operation(
            summary = "Return the available unitTypes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Available UnitTypes returned"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request")
            }
    )
    @GetMapping("/unittype")
    public List<UnitType> findAllUnitType(
    ) {
        try {
            return Arrays.stream(UnitType.values()).toList();
        }catch (IllegalArgumentException iae){
            throw new IllegalStateException("UnitType enum not found!", iae);
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String fieldName = ex.getName();
        String fieldType = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        String invalidValue = Objects.requireNonNull(ex.getValue()).toString();
        String errorMessage = "Invalid " + fieldName + " value: " + invalidValue + ". Expected " + fieldType + ".";
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(HttpMessageNotReadableException ex) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        String errorMessage;

        if (mostSpecificCause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = invalidFormatException.getPath().get(0).getFieldName();
            String fieldType = invalidFormatException.getTargetType().getSimpleName();
            String invalidValue = invalidFormatException.getValue().toString();
            errorMessage = "Invalid " + fieldName + " value: " + invalidValue + ". Expected " + fieldType + ".";
        } else {
            errorMessage = "Invalid request body.";
        }
        log.warn(errorMessage);
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
