package com.codecool.biteways.controller;

import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.dto.IngredientDto;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IngredientControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void saveIngredient() {
    }

    @Test
    @DirtiesContext
    void findAllIngredient_insertTwoIngredient_shouldReturnLengthTwo() {
        Ingredient ingredient1 = new Ingredient("salt");
        Ingredient ingredient2 = new Ingredient("almond milk");
        restTemplate.postForObject("/api/biteways/ingredient", ingredient1, Ingredient.class);
        restTemplate.postForObject("/api/biteways/ingredient", ingredient2, Ingredient.class);

        Ingredient[] ingredients = restTemplate.getForObject("/api/biteways/ingredient", Ingredient[].class);

        List<String> saved = Stream.of(ingredients).map(Ingredient::getName).toList();
        List<String> expected = Arrays.asList(ingredient1.getName(), ingredient2.getName());

        assertNotNull(ingredients);
        assertEquals(2, ingredients.length);
        assertTrue(saved.containsAll(expected));
        assertFalse(Arrays.asList(ingredients).isEmpty());
    }

    @Test
    @DirtiesContext
    void findIngredientById_findById1_shouldReturnId1() {
        Ingredient ingredient1 = new Ingredient("salt");
        Ingredient ingredient2 = new Ingredient("almond milk");
        restTemplate.postForObject("/api/biteways/ingredient", ingredient1, Ingredient.class);
        restTemplate.postForObject("/api/biteways/ingredient", ingredient2, Ingredient.class);

        Ingredient savedIngredient = restTemplate.getForObject("/api/biteways/ingredient/1", Ingredient.class);
        assertThat(savedIngredient).isEqualTo(ingredient1);
    }

    @Test
    @DirtiesContext
    void updateIngredient_existingIngredient_shouldReturnUpdatedIngredient() {
        Ingredient ingredient = new Ingredient("salt");
        Ingredient savedIngredient = restTemplate.postForObject("/api/biteways/ingredient", ingredient, Ingredient.class);

        IngredientDto ingredientDto = new ModelMapper().map(new Ingredient("updated salt"), IngredientDto.class);
        restTemplate.put("/api/biteways/ingredient/" + savedIngredient.getId(), ingredientDto);

        Ingredient updatedIngredient = restTemplate.getForObject("/api/biteways/ingredient/" + savedIngredient.getId(), Ingredient.class);
        assertNotNull(updatedIngredient);
        assertEquals(updatedIngredient.getName(), ingredientDto.getName());
    }

    @Test
    @DirtiesContext
    void deleteIngredient_deleteExistingIngredient_shouldReturnNoContent() {
        Ingredient ingredient = new Ingredient("salt");
        Ingredient savedIngredient = restTemplate.postForObject("/api/biteways/ingredient", ingredient, Ingredient.class);

        restTemplate.delete("/api/biteways/ingredient/" + savedIngredient.getId());

        ResponseEntity<Ingredient[]> response = restTemplate.exchange("/api/biteways/ingredient", HttpMethod.GET, null, Ingredient[].class);
        HttpStatusCode statusCode = response.getStatusCode();

        Ingredient[] ingredients = response.getBody();
        assert ingredients != null;
        assertThat(ingredients.length).isEqualTo(0);
        assertThat(statusCode).isEqualTo(HttpStatusCode.valueOf(200));
    }
}