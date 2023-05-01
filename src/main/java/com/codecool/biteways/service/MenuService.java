package com.codecool.biteways.service;

import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.Menu;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.ShoppingItem;
import com.codecool.biteways.model.dto.MenuDto;
import com.codecool.biteways.repository.MenuRepository;
import com.codecool.biteways.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

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

    public Menu createMenu() {
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
        return menu;
    }

    private String createMenuName() {
        LocalDate today = LocalDate.now();
        LocalDate todayPlus7Day = LocalDate.now().plusDays(7);
        StringBuilder menuName = buildMenuName(today, todayPlus7Day);
        return menuName.toString();
    }

    public StringBuilder buildMenuName(LocalDate today, LocalDate todayPlus7Day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");
        StringBuilder menuName = new StringBuilder();
        menuName.append("Menu offer from ");
        menuName.append(today.format(formatter));
        menuName.append(" to ");
        menuName.append(todayPlus7Day.format(formatter));
        return menuName;
    }

    public MenuDto menuToMenuDto(Menu m) {
        return new ModelMapper().map(m, MenuDto.class);
    }

    public List<ShoppingItem> generateShoppingList(Menu menu) {
        List<ShoppingItem> shoppingItemList = new ArrayList<>();
        menu.
                getRecipeList().
                forEach(r -> r.
                        getIngredientList().
                        forEach(ingredient -> processIngredient(shoppingItemList, ingredient))
                );
        return shoppingItemList;
    }

    private static void processIngredient(List<ShoppingItem> shoppingItemList, Ingredient ingredient) {
        int index = IntStream.range(0, shoppingItemList.size())
                .filter(i -> shoppingItemList.
                        get(i).
                        getItemName().
                        equals(ingredient.getName())
                        &&
                        shoppingItemList.
                                get(i).
                                getUnitType().
                                equals(ingredient.getUnitType())
                )
                .findFirst()
                .orElse(-1);
        addToShoppingList(shoppingItemList, ingredient, index);
    }

    private static void addToShoppingList(List<ShoppingItem> shoppingItemList, Ingredient ingredient, int index) {
        if (index >= 0) {
            shoppingItemList.
                    get(index).
                    setQuantity(shoppingItemList.get(index).getQuantity() + ingredient.getQuantity());
        } else {
            shoppingItemList.
                    add(new ShoppingItem(ingredient.getName(), ingredient.getQuantity(), ingredient.getUnitType()));
        }
    }

}
