package com.codecool.biteways.controller;

import com.codecool.biteways.model.RawRecipe;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.RecipeDto;
import com.codecool.biteways.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RecipeService recipeService;

    @Test
    void saveRecipe() {
    }

    @Test
    @DirtiesContext
    void findAllRecipe_insertTwoRecipe_shouldReturnLengthTwo() {
        List<Recipe> savedRecipes = saveRecipes();
        Recipe[] recipes = restTemplate.getForObject("/api/biteways/recipe", Recipe[].class);

        List<String> saved = Stream.of(recipes).map(Recipe::getName).toList();
        List<String> expected = Arrays.asList(savedRecipes.get(0).getName(), savedRecipes.get(1).getName());

        assertNotNull(recipes);
        assertEquals(2, recipes.length);
        assertTrue(saved.containsAll(expected));
        assertFalse(Arrays.asList(recipes).isEmpty());
    }

    @Test
    @DirtiesContext
    void findRecipeById() {
        List<Recipe> recipes = saveRecipes();
        Recipe recipe = restTemplate.getForObject("/api/biteways/recipe/2", Recipe.class);
        assertNotNull(recipe);
        assertThat(recipe.getName()).isEqualTo(recipes.get(1).getName());
    }

    @Test
    @DirtiesContext
    @Transactional
    void testUpdateRecipeById_shouldReturnUpdatedRecipe() {
        List<Recipe> savedRecipes = saveRecipes();

        Recipe recipeToUpdate = savedRecipes.get(0);
        recipeToUpdate.setName("updated name");
        recipeToUpdate.setInstructions("updated instructions");

        restTemplate.put("/api/biteways/recipe/1", recipeToUpdate, Recipe.class);
        RecipeDto updatedRecipeDto = restTemplate.getForObject("/api/biteways/recipe/1", RecipeDto.class);

        RecipeDto recipeToUpdateDto = recipeService.recipeToDto(recipeToUpdate);
        assertThat(updatedRecipeDto).isEqualTo(recipeToUpdateDto);
    }

    @Test
    @DirtiesContext
    void deleteRecipe() {
        List<Recipe> savedRecipes = saveRecipes();
        Long recipeIdToDelete = savedRecipes.get(0).getId();
        List<RecipeDto> recipeDtoListBeforeDelete = List.of(restTemplate.getForObject("/api/biteways/recipe", RecipeDto[].class));
        assertTrue(recipeDtoListBeforeDelete.stream().map(RecipeDto::getId).toList().contains(recipeIdToDelete));

        restTemplate.delete("/api/biteways/recipe/" + recipeIdToDelete);

        List<RecipeDto> recipeDtoListAfterDelete = List.of(restTemplate.getForObject("/api/biteways/recipe", RecipeDto[].class));
        assertFalse(recipeDtoListAfterDelete.stream().map(RecipeDto::getId).toList().contains(recipeIdToDelete));
    }

    public List<Recipe> saveRecipes() {
        RawRecipe rawRecipe1 = new RawRecipe("recipe1", "cook it", "2tablespoon salt\n1cup flour");
        RawRecipe rawRecipe2 = new RawRecipe("recipe2", "bake it", "2cup rice\n3cup tomato");
        Recipe recipe1 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe1, Recipe.class);
        Recipe recipe2 = restTemplate.postForObject("/api/biteways/recipe", rawRecipe2, Recipe.class);
        return new ArrayList<>(List.of(recipe1, recipe2));
    }
}