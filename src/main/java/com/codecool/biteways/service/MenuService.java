package com.codecool.biteways.service;

import com.codecool.biteways.model.Menu;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.repository.MenuRepository;
import com.codecool.biteways.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final RecipeRepository recipeRepository;

    public MenuService(MenuRepository menuRepository, RecipeRepository recipeRepository) {
        this.menuRepository = menuRepository;
        this.recipeRepository = recipeRepository;
    }

    public Menu createMenu() {
        Menu menu = new Menu();
        List<Recipe> recipes = recipeRepository.findAll();
        Set<Recipe> menuRecipes = new HashSet<>();
        while (menuRecipes.size() < 7) {
            menuRecipes.add(recipes.get(new Random().nextInt(recipes.size())));
        }
        menu.setRecipeList(menuRecipes.
                stream().
                sorted(Comparator.comparingLong(Recipe::getId))
                .toList());
        return menu;
    }

    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public List<Menu> findAllMenu() {
        return menuRepository.findAll();
    }

    public Menu findMenuById(Long id) {
        return menuRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Menu updateMenu(Long id, Menu update) {
        Menu menu = menuRepository.findById(id).orElseThrow();
        menu.setName(update.getName());
        menu.setRecipeList(update.getRecipeList());
        return menuRepository.save(menu);
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

}
