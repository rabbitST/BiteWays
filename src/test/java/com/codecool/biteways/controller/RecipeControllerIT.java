package com.codecool.biteways.controller;

import com.codecool.biteways.model.RawRecipe;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.RecipeDto;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    void testSaveRecipeWithValidRawRecipe_ShouldSaveValidRecipe() {
        RawRecipe rawRecipe = new RawRecipe("recipe1", "cook it", "2tablespoon salt\n1cup flour");
        Recipe recipe = restTemplate.postForObject("/api/biteways/recipe", rawRecipe, Recipe.class);
        assertThat(rawRecipe.getName()).isEqualTo(recipe.getName());
        assertThat(rawRecipe.getInstructions()).isEqualTo(recipe.getInstructions());
    }

    @Test
    @DirtiesContext
    void testSaveRecipeWithInvalidData_shouldReturnErrorMessage() {
        RawRecipe rawRecipe = new RawRecipe("re", "cook it", "2tablespoon salt\n1cup flour");
        ResponseEntity<?> response = restTemplate.postForEntity("/api/biteways/recipe", rawRecipe, Object.class);
        List<?> errorList = (List<?>) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String expectedErrorMessage = "Please enter a recipe name that is between 3 and 30 characters in length.";
        assert errorList != null;
        assertThat(errorList.get(0)).isEqualTo(expectedErrorMessage);
        assertTrue(response.getBody() instanceof List);
        assertTrue(errorList.stream().allMatch(e -> e instanceof String));
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
    void testFindRecipeByIdWithValidId_shouldReturnTheRequestedId() {
        List<Recipe> recipes = saveRecipes();
        Recipe recipe = restTemplate.getForObject("/api/biteways/recipe/2", Recipe.class);
        assertNotNull(recipe);
        assertThat(recipe.getName()).isEqualTo(recipes.get(1).getName());
    }

    @Test
    void testFindRecipeByIdWithInvalidCharacter_shouldReturnErrorMessage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/biteways/recipe/m", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Invalid id value"));
    }

    @Test
    @DirtiesContext
    void testUpdateRecipeById_shouldReturnUpdatedRecipe() {
        saveRecipes();
        RecipeDto recipeToUpdate = restTemplate.getForObject("/api/biteways/recipe/1", RecipeDto.class);
        recipeToUpdate.setName("updated name");
        recipeToUpdate.setInstructions("updated instructions");
        restTemplate.put("/api/biteways/recipe/1", new ModelMapper().map(recipeToUpdate, Recipe.class), Recipe.class);
        RecipeDto updatedRecipeDto = restTemplate.getForObject("/api/biteways/recipe/1", RecipeDto.class);
        assertThat(updatedRecipeDto).isEqualTo(recipeToUpdate);
    }

    @Test
    void testUpdateRecipeByIdWithInvalidRecipe_shouldReturnErrorMessage() {
        saveRecipes();
        Recipe recipe=restTemplate.getForObject("/api/biteways/recipe/1", Recipe.class);
        recipe.setName("*");
        ResponseEntity<String> response = restTemplate.exchange("/api/biteways/recipe/1", HttpMethod.PUT, new HttpEntity<>(recipe), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("The recipe name can only contain letters, numbers, hyphens, spaces."));
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