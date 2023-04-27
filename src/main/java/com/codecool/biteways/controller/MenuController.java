package com.codecool.biteways.controller;

import com.codecool.biteways.model.Menu;
import com.codecool.biteways.service.MenuService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/biteways/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public Menu saveMenu(@RequestBody Menu menu) {
        return menuService.saveMenu(menu);
    }

    @GetMapping
    public List<Menu> findAllMenu() {
        return menuService.findAllMenu();
    }

    @GetMapping(value = "/{id}")
    public Menu findMenuById(@PathVariable("id") Long id) {
        return menuService.findMenuById(id);
    }

    @PutMapping(value = "/{id}")
    public Menu updateMenu(
            @PathVariable("id") Long id,
            @RequestBody Menu menu
    ) {
        return menuService.updateMenu(id, menu);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteMenu(
            @PathVariable("id") Long id
    ) {
        menuService.deleteMenu(id);
    }

    @GetMapping(value = "/createmenu")
    public Menu createMenu() {
        return menuService.createMenu();
    }


}
