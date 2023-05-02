package com.codecool.biteways.controller;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Menu;
import com.codecool.biteways.model.ShoppingItem;
import com.codecool.biteways.model.dto.MenuDto;
import com.codecool.biteways.service.MenuService;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/biteways/menu")
@Tag(name = "Menu Controller", description = "API endpoints for managing Menus")
@Log4j2
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Operation(
            summary = "Save new Menu entity.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the saved Menu."),
                    @ApiResponse(responseCode = "400", description = "Invalid input data."),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request.")
            }
    )
    @PostMapping
    public ResponseEntity<?> saveMenu(@Valid @RequestBody Menu menu,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            return ResponseEntity.ok(menuService.saveMenu(menu));
        }
    }

    @Operation(
            summary = "Fetch all Menus from the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with all Menus stored in the database."),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request.")
            }
    )
    @GetMapping
    public List<MenuDto> findAllMenu() {
        return menuService.findAllMenu();
    }

    @Operation(
            summary = "Find a Menu entity by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the requested Menu."),
                    @ApiResponse(responseCode = "404", description = "The requested Menu was not found."),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request.")
            }
    )
    @GetMapping(value = "/{id}")
    public MenuDto findMenuById(@PathVariable("id") Long id) {
        try {
            return menuService.findMenuById(id);
        } catch (RecordNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }


    @Operation(
            summary = "Update a Menu entity by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the updated Menu."),
                    @ApiResponse(responseCode = "400", description = "Invalid input data."),
                    @ApiResponse(responseCode = "404", description = "The requested Menu was not found."),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request.")
            }
    )
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateMenu(@PathVariable("id") Long id, @Valid @RequestBody Menu menu, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            return ResponseEntity.ok(menuService.updateMenu(id, menu));
        }
    }

    @Operation(
            summary = "Delete a Menu entity by ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "The Menu was successfully deleted."),
                    @ApiResponse(responseCode = "404", description = "The requested Menu was not found."),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request.")
            }
    )
    @DeleteMapping(value = "/{id}")
    public void deleteMenu(
            @PathVariable("id") Long id
    ) {
        menuService.deleteMenu(id);
    }

    @Operation(
            summary = "Create a new Menu with default data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the created Menu."),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request.")
            }
    )
    @GetMapping(value = "/createmenu")
    public Menu createMenu() {
        return menuService.createMenu();
    }

    @Operation(
            summary = "Generate a shopping list from a menu",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responds with the generated shopping list."),
                    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request.")
            }
    )
    @GetMapping(value = "/shoppinglist/{id}")
    public List<ShoppingItem> createShoppingList(@PathVariable("id") Long id){
        return menuService.generateShoppingList(id);
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
