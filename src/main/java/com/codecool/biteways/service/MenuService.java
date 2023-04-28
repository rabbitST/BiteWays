package com.codecool.biteways.service;

import com.codecool.biteways.model.Menu;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.MenuDto;
import com.codecool.biteways.repository.MenuRepository;
import com.codecool.biteways.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final RecipeRepository recipeRepository;

    public MenuService(MenuRepository menuRepository, RecipeRepository recipeRepository) {
        this.menuRepository = menuRepository;
        this.recipeRepository = recipeRepository;
    }

    public MenuDto saveMenu(Menu menu) {
        return menuToMenuDto(menuRepository.save(menu));
    }

    public List<MenuDto> findAllMenu() {
        return menuRepository.
                findAll().
                stream().
                map(this::menuToMenuDto).
                toList();
    }

    public MenuDto findMenuById(Long id) {
        return menuToMenuDto(menuRepository.findById(id).orElseThrow());
    }

    @Transactional
    public MenuDto updateMenu(Long id, Menu update) {
        Menu menu = menuRepository.findById(id).orElseThrow();
        menu.setName(update.getName());
        menu.setRecipeList(update.getRecipeList());
        return menuToMenuDto(menuRepository.save(menu));
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public MenuDto createMenu() {
        Menu menu = new Menu();
        menu.setName(createMenuName());
        List<Recipe> recipes = recipeRepository.findAll();
        Set<Recipe> menuRecipes = new HashSet<>();
        while (menuRecipes.size() < 7) {
            menuRecipes.add(recipes.get(new Random().nextInt(recipes.size())));
        }
        menu.setRecipeList(menuRecipes.
                stream().
                sorted(Comparator.comparingLong(Recipe::getId))
                .toList());
        return menuToMenuDto(menu);
    }

    private String createMenuName() {
        LocalDate today = LocalDate.now();
        LocalDate todayPlus7Day = LocalDate.now().plusDays(7);
        StringBuilder menuName = buildMenuName(today, todayPlus7Day);
        return menuName.toString();
    }

    private static StringBuilder buildMenuName(LocalDate today, LocalDate todayPlus7Day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");
        StringBuilder menuName = new StringBuilder();
        menuName.append("Menu offer from ");
        menuName.append(today.format(formatter));
        menuName.append(" to ");
        menuName.append(todayPlus7Day.format(formatter));
        return menuName;
    }

    public MenuDto menuToMenuDto(Menu m) {
        MenuDto menuDto = new ModelMapper().map(m, MenuDto.class);
        menuDto.setRecipes(m.getRecipeList().
                stream().
                map(Recipe::getName).
                toList()
        );
        return menuDto;
    }

}
