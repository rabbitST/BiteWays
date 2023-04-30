package com.codecool.biteways.service;

import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.Menu;
import com.codecool.biteways.model.RawRecipe;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.RecipeDto;
import com.codecool.biteways.model.enums.UnitType;
import com.codecool.biteways.repository.IngredientRepository;
import com.codecool.biteways.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeService recipeService;

    private static Recipe recipe1 = new Recipe();
    private static Recipe recipe2 = new Recipe();
    private static final List<Ingredient> ingredientList1 = new ArrayList<>();
    private static final List<Ingredient> ingredientList2 = new ArrayList<>();
    private static final List<Menu> menuList = new ArrayList<>();

    @BeforeAll
    static void setData() {
        Ingredient ingredient1 = new Ingredient(1L, "flour", recipe1, 500F, UnitType.valueOf("g".toUpperCase()));
        Ingredient ingredient2 = new Ingredient(2L, "milk", recipe1, 1F, UnitType.valueOf("l".toUpperCase()));
        Ingredient ingredient3 = new Ingredient(3L, "rice", recipe2, 1F, UnitType.valueOf("cup".toUpperCase()));
        ingredientList1.add(ingredient1);
        ingredientList1.add(ingredient2);
        ingredientList2.add(ingredient3);
        recipe1 = new Recipe(1L, "recipe1", 12, "instrucions 1", ingredientList1, menuList);
        recipe2 = new Recipe(2L, "recipe1", 12, "instrucions 1", ingredientList2, menuList);
    }

    @Test
    void testSaveRecipe_convertValidRawRecipeToValidRecipe() {
        RawRecipe rawRecipe = new RawRecipe();
        rawRecipe.setName("Test Recipe");
        rawRecipe.setInstructions("Test instructions");
        rawRecipe.setIngredients("1cup flour\n2tbsp sugar");

        Recipe recipeToSave = new Recipe();
        recipeToSave.setName("Test Recipe");
        recipeToSave.setInstructions("Test instructions");
        recipeToSave.setDownloaded(1);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("flour");
        ingredient1.setQuantity(1f);
        ingredient1.setUnitType(UnitType.CUP);
        ingredient1.setRecipe(recipeToSave);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("sugar");
        ingredient2.setQuantity(2f);
        ingredient2.setUnitType(UnitType.TBSP);
        ingredient2.setRecipe(recipeToSave);
        recipeToSave.setIngredientList(new ArrayList<>(List.of(ingredient1, ingredient2)));

        List<Ingredient> expectedIngredientList = new ArrayList<>();
        expectedIngredientList.add(ingredient1);
        expectedIngredientList.add(ingredient2);

        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipeToSave);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient1, ingredient2);

        Recipe savedRecipe = recipeService.saveRecipe(rawRecipe);

        verify(ingredientRepository, times(2)).save(any(Ingredient.class));
        assertEquals(recipeToSave, savedRecipe);
        assertEquals(expectedIngredientList, savedRecipe.getIngredientList());
    }

    @Test
    void testFindAllRecipe_ValidRecipeListAddedValidRecipeDtoListReturned() {
        List<Recipe> recipes = new ArrayList<>(List.of(recipe1, recipe2));
        List<RecipeDto> recipesDto = recipes.stream().map(r -> recipeService.recipeToDto(r)).toList();
        when(recipeRepository.findAll()).thenReturn(recipes);

        List<RecipeDto> result = recipeService.findAllRecipe();

        assertThat(result.size()).isEqualTo(recipesDto.size());
        assertThat(result).containsAll(recipesDto);
    }

    @Test
    void testFindRecipeById_validIdReturnTheRequestedRecipe() {
        RecipeDto expected = recipeService.recipeToDto(recipe2);
        when(recipeRepository.findById(2L)).thenReturn(Optional.ofNullable(recipe2));
        RecipeDto requestedRecipe = recipeService.findRecipeById(2L);

        assertThat(requestedRecipe).isEqualTo(expected);

    }

    @Test
    void testUpdateRecipe_shouldReturnRecipeWithUpdatedProperties() {
        Recipe recipeToUpdate = recipe2;

        Recipe update = new Recipe();
        update.setName("updated name");
        update.setInstructions("updated instructions");
        ingredientList2.get(0).setName("updated ingredient");
        ingredientList2.get(0).setQuantity(300F);
        update.setIngredientList(ingredientList2);

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeToUpdate));
        when(ingredientRepository.findById(3L)).thenReturn(Optional.of(ingredientList2.get(0)));

        recipeService.updateRecipe(1L, update);

        verify(recipeRepository).findById(1L);
        verify(ingredientRepository).findById(3L);
        assertEquals("updated name", recipeToUpdate.getName());
        assertEquals("updated instructions", recipeToUpdate.getInstructions());
        assertEquals(1, recipeToUpdate.getIngredientList().size());
        assertEquals("updated ingredient", recipeToUpdate.getIngredientList().get(0).getName());
        assertEquals(300f, recipeToUpdate.getIngredientList().get(0).getQuantity());
    }

    @Test
    void deleteRecipe() {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe2));

        recipeService.deleteRecipe(2L);

        verify(ingredientRepository, times(1)).deleteById(anyLong());
        verify(recipeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void recipeToDto() {
        RecipeDto recipeDto = recipeService.recipeToDto(recipe1);

        assertEquals(recipe1.getId(), recipeDto.getId());
        assertEquals(recipe1.getName(), recipeDto.getName());
        assertEquals(recipe1.getDownloaded(), recipeDto.getDownloaded());
        assertEquals(2, recipeDto.getIngredientList().size());
        List<String> ingredientNames = recipeDto.getIngredientList().stream().map(Ingredient::getName).toList();

        assertThat(ingredientNames).contains(recipe1.getIngredientList().get(0).getName());
        assertThat(ingredientNames).contains(recipe1.getIngredientList().get(1).getName());
    }

}