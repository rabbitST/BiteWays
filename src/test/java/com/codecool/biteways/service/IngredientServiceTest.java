package com.codecool.biteways.service;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Ingredient;
import com.codecool.biteways.model.Menu;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.IngredientDto;
import com.codecool.biteways.model.enums.UnitType;
import com.codecool.biteways.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    private static final List<Ingredient> ingredientList = new ArrayList<>();
    private static final List<Menu> menuList = new ArrayList<>();


    @BeforeAll
    static void setData() {
        Recipe recipe1 = new Recipe(1L, "recipe1", 12, "instrucions 1", ingredientList, menuList);
        Ingredient ingredient1 = new Ingredient(1L, "flour", recipe1, 500F, UnitType.valueOf("g".toUpperCase()));
        Ingredient ingredient2 = new Ingredient(2L, "milk", recipe1, 1F, UnitType.valueOf("l".toUpperCase()));
        ingredientList.add(ingredient1);
        ingredientList.add(ingredient2);
    }

    @Test
    void saveIngredient_saveOneShouldReturnOne() {
        Ingredient ingredient = ingredientList.get(0);
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);

        Ingredient savedIngredient = ingredientService.saveIngredient(ingredient);

        verify(ingredientRepository).save(ingredient);
        assertThat(savedIngredient).isNotNull();
        assertThat(savedIngredient.getId()).isGreaterThan(0);
        assertThat(savedIngredient).isEqualTo(ingredient);
    }

    @Test
    void findAllIngredient_shouldReturnIngredientList() {
        List<IngredientDto> ingredientDtoList = ingredientList.
                stream().
                map(i -> ingredientService.ingredientToDto(i)).
                toList();
        when(ingredientRepository.findAll()).thenReturn(ingredientList);
        assertThat(ingredientService.findAllIngredient()).isEqualTo(ingredientDtoList);
    }

    @Test
    void findIngredientById_shouldReturnReferencedId() throws RecordNotFoundException {
        Ingredient ingredient = ingredientList.get(1);
        when(ingredientRepository.findById(eq(1L))).thenReturn(Optional.ofNullable(ingredient));
        IngredientDto ingredientDto = ingredientService.findIngredientById(1L);
        assert ingredient != null;
        assertThat(ingredientDto.getId()).isEqualTo(ingredient.getId());
        assertThat(ingredientDto.getName()).isEqualTo(ingredient.getName());
    }

    @Test
    void findIngredientById_idNotFound_shouldThrowRecordNotFoundException() {
        Long id = 3L;
        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> ingredientService.findIngredientById(id));
    }

    @Test
    void updateIngredient_shouldReturnUpdatedIngredient() throws RecordNotFoundException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(23L);
        ingredient.setName("name");
        when(ingredientRepository.findById(23L)).thenReturn(Optional.of(ingredient));

        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("updated name");

        Ingredient updatedIngredient = ingredientService.updateIngredient(23L, ingredientDto);

        assertThat(updatedIngredient.getId()).isEqualTo(ingredient.getId());
        assertThat(updatedIngredient.getName()).isEqualTo(ingredientDto.getName());
    }

    @Test
    void deleteIngredient_twoDeleteInvokedTwoPerformed() throws RecordNotFoundException {
        when(ingredientRepository.findById(any())).thenReturn(Optional.ofNullable(ingredientList.get(0)));
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());
        ingredientService.deleteIngredient(1L);
        ingredientService.deleteIngredient(1L);
        assertThat(ingredientRepository.findAll()).isEmpty();
        verify(ingredientRepository, times(2)).deleteById(1L);
    }

    @Test
    void deleteIngredient_shouldCallRepositoryWithCorrectId() throws RecordNotFoundException {
        Long id = 1L;
        doNothing().when(ingredientRepository).deleteById(id);
        when(ingredientRepository.findById(1L)).thenReturn(Optional.ofNullable(ingredientList.get(0)));
        ingredientService.deleteIngredient(id);
        verify(ingredientRepository, times(1)).deleteById(id);
    }

    @Test
    void ingredientToDto_shouldReturnCorrectIngredientDto() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Oil");
        ingredient.setQuantity(2.0F);
        ingredient.setUnitType(UnitType.TABLESPOON);
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Hummus");
        ingredient.setRecipe(recipe);

        IngredientDto expectedDto = new IngredientDto();
        expectedDto.setId(1L);
        expectedDto.setName("Oil");
        expectedDto.setRecipe(recipe);
        expectedDto.setQuantity(2.0F);
        expectedDto.setUnitType(UnitType.TABLESPOON);

        IngredientDto actualDto = ingredientService.ingredientToDto(ingredient);

        assertThat(actualDto).isEqualTo(expectedDto);
    }

}