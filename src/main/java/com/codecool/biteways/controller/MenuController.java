package com.codecool.biteways.controller;

import com.codecool.biteways.model.Menu;
import com.codecool.biteways.model.dto.MenuDto;
import com.codecool.biteways.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public MenuDto saveMenu(@RequestBody Menu menu) {
        return menuService.saveMenu(menu);
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
        return menuService.findMenuById(id);
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
    public MenuDto updateMenu(
            @PathVariable("id") Long id,
            @RequestBody Menu menu
    ) {
        return menuService.updateMenu(id, menu);
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


}
