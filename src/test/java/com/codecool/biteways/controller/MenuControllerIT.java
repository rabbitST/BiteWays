package com.codecool.biteways.controller;

import com.codecool.biteways.model.Menu;
import com.codecool.biteways.model.RawRecipe;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.MenuDto;
import com.codecool.biteways.service.MenuService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
class MenuControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MenuService menuService;

    @Test
    @DirtiesContext
    void testSaveMenu_shouldSaveMenuWithValidData() {
        saveRecipes();
        createTestMenus();
        List<MenuDto> menuDtoList = List.of(restTemplate.getForObject("/api/biteways/menu", MenuDto[].class));
        assertThat(menuDtoList).hasSize(5);
        MenuDto menuDto1 = menuDtoList.get(0);
        assertThat(menuDto1.getId()).isEqualTo(1);
        assertThat(menuDto1.getRecipeList()).hasSize(7);
    }

    @Test
    @DirtiesContext
    void findAllMenu_shouldReturnAllMenus() {
        saveRecipes();
        createTestMenus();
        List<MenuDto> menuDtoList = List.of(restTemplate.getForObject("/api/biteways/menu", MenuDto[].class));
        assertThat(menuDtoList).isNotNull();
        assertThat(menuDtoList.size()).isEqualTo(5);
    }

    @Test
    @DirtiesContext
    void findMenuById_shouldReturnMenuById() {
        saveRecipes();
        createTestMenus();
        MenuDto menuDto = restTemplate.getForObject("/api/biteways/menu/1", MenuDto.class);
        assertThat(menuDto).isNotNull();
        assertThat(menuDto.getId()).isEqualTo(1);
        assertThat(menuDto.getName()).isNotNull();
        assertThat(menuDto.getRecipeList()).isNotEmpty();
        assertThat(menuDto.getRecipeList()).hasSize(7);
    }

    @Test
    @DirtiesContext
    void updateMenu_shouldUpdateMenuWithValidData() {
        saveRecipes();
        createTestMenus();

        MenuDto menuDto = restTemplate.getForObject("/api/biteways/menu/2", MenuDto.class);
        Menu toUpdate = new ModelMapper().map(menuDto, Menu.class);
        toUpdate.setName("updated name");
        restTemplate.put("/api/biteways/menu/2", toUpdate, MenuDto.class);
        MenuDto updatedMenu=restTemplate.getForObject("/api/biteways/menu/2", MenuDto.class);
        assertThat(updatedMenu.getName()).isEqualTo("updated name");
    }

    @Test
    @DirtiesContext
    void
    testDeleteMenu_shouldDeleteMenuById() {
        saveRecipes();
        createTestMenus();
        List<MenuDto> menuDtoListBeforeDelete = List.of(restTemplate.getForObject("/api/biteways/menu", MenuDto[].class));
        assertThat(menuDtoListBeforeDelete.size()).isGreaterThan(0);
        restTemplate.delete("/api/biteways/menu/" + menuDtoListBeforeDelete.get(0).getId());
        List<MenuDto> menuDtoListAfterDelete = List.of(restTemplate.getForObject("/api/biteways/menu", MenuDto[].class));
        assertThat(menuDtoListAfterDelete.size()).isLessThan(menuDtoListBeforeDelete.size());


    }

    @Test
    @DirtiesContext
    void testCreateMenu_shouldReturnValidMenuWithSevenRecipes() {
        List<Recipe> recipeList = saveRecipes();
        List<String> dataBaseRecipeNames = recipeList.
                stream().
                map(Recipe::getName).
                toList();
        Menu createdMenu = menuService.createMenu();
        List<String> createdMenuRecipeNames = createdMenu.getRecipeList().stream().map(Recipe::getName).toList();

        LocalDate today = LocalDate.now();
        LocalDate todayPlus7Day = LocalDate.now().plusDays(7);
        String menuName = String.valueOf(menuService.buildMenuName(today, todayPlus7Day));

        assertThat(createdMenu.getRecipeList().size()).isEqualTo(7);
        assertThat(createdMenu.getName()).isEqualTo(menuName);
        assertThat(dataBaseRecipeNames).containsAll(createdMenuRecipeNames);

    }

    public List<Recipe> saveRecipes() {
        RawRecipe rawRecipe1 = new RawRecipe("recipe1", "cook it", "2tablespoon salt\n1cup flour");
        Recipe recipe1 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe1, Recipe.class);

        RawRecipe rawRecipe2 = new RawRecipe("recipe2", "bake it", "2cup rice\n3cup tomato");
        Recipe recipe2 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe2, Recipe.class);


        RawRecipe rawRecipe3 = new RawRecipe("recipe3", "instruction 3", "2cup ingredient 31\n1cup ingredient 32");
        Recipe recipe3 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe3, Recipe.class);

        RawRecipe rawRecipe4 = new RawRecipe("recipe4", "instruction 4", "2cup ingredient 41\n1cup ingredient 42");
        Recipe recipe4 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe4, Recipe.class);

        RawRecipe rawRecipe5 = new RawRecipe("recipe5", "instruction 5", "2cup ingredient 51\n1cup ingredient 52");
        Recipe recipe5 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe5, Recipe.class);

        RawRecipe rawRecipe6 = new RawRecipe("recipe6", "instruction 6", "2cup ingredient 61\n1cup ingredient 62");
        Recipe recipe6 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe6, Recipe.class);

        RawRecipe rawRecipe7 = new RawRecipe("recipe7", "instruction 7", "2cup ingredient 71\n1cup ingredient 72");
        Recipe recipe7 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe7, Recipe.class);

        RawRecipe rawRecipe8 = new RawRecipe("recipe8", "instruction 8", "2cup ingredient 81\n1cup ingredient 82");
        Recipe recipe8 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe8, Recipe.class);

        RawRecipe rawRecipe9 = new RawRecipe("recipe9", "instruction 9", "2cup ingredient 91\n1cup ingredient 92");
        Recipe recipe9 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe9, Recipe.class);

        return new ArrayList<>(List.of(recipe1, recipe2, recipe3, recipe4, recipe5, recipe6, recipe7, recipe8, recipe9));
    }

    public void createTestMenus() {
        for (int i = 0; i < 5; i++) {
            Menu menu = restTemplate.getForObject("/api/biteways/menu/createmenu", Menu.class);
            restTemplate.postForObject("/api/biteways/menu", menu, MenuDto.class);
        }
    }
}