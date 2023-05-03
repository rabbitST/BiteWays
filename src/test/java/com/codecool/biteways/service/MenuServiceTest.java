package com.codecool.biteways.service;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Menu;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.MenuDto;
import com.codecool.biteways.repository.MenuRepository;
import com.codecool.biteways.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private MenuService menuService;

    private static final List<Recipe> RECIPES = List.of(
            new Recipe(1L, "Recipe 1", 30, "Instructions 1", List.of(), List.of()),
            new Recipe(2L, "Recipe 2", 60, "Instructions 2", List.of(), List.of())
    );

    private static final List<Menu> MENUS = List.of(
            new Menu(1L, "Menu 1", RECIPES),
            new Menu(2L, "Menu 2", List.of(RECIPES.get(0)))
    );


    @Test
    void saveMenu_saveOneShouldReturnOne() {
        Menu menu = MENUS.get(0);
        when(menuRepository.save(menu)).thenReturn(menu);
        MenuDto savedMenuDto = menuService.saveMenu(menu);
        verify(menuRepository).save(menu);
        assertThat(savedMenuDto).isNotNull();
        assertThat(savedMenuDto.getId()).isGreaterThan(0);
        assertThat(savedMenuDto).isEqualTo(new ModelMapper().map(menu, MenuDto.class));
    }

    @Test
    void testFindAllMenu_menusExist_returnAllMenus() {
        when(menuRepository.findAll()).thenReturn(MENUS);
        List<MenuDto> menuDtoList = menuService.findAllMenu();
        assertThat(menuDtoList).hasSize(2);
        assertThat(menuDtoList.get(0)).isEqualTo(new MenuDto(1L, "Menu 1", RECIPES));
        assertThat(menuDtoList.get(1)).isEqualTo(new MenuDto(2L, "Menu 2", List.of(RECIPES.get(0))));
    }

    @Test
    void testFindMenuById_menuExists_returnMenu() throws RecordNotFoundException {
        when(menuRepository.findById(1L)).thenReturn(Optional.of(MENUS.get(0)));
        MenuDto menuDto = menuService.findMenuById(1L);
        assertThat(menuDto).isEqualTo(new MenuDto(1L, "Menu 1", RECIPES));
    }

    @Test
    void testFindMenuById_menuDoesNotExist_throwRecordNotFoundException() {
        when(menuRepository.findById(3L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> menuService.findMenuById(3L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessage("Requested ID: 3 not found!");
    }

    @Test
    void testUpdateMenu_validIdAndMenu_updateSuccessful() {
        Menu menu = new Menu(null, "New Menu", List.of(RECIPES.get(1)));
        Menu menuToUpdate = new Menu(1L, "Old Menu", List.of(RECIPES.get(0)));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuToUpdate));
        when(menuRepository.save(any(Menu.class))).thenReturn(menuToUpdate);
        MenuDto updatedMenu = menuService.updateMenu(1L, menu);

        verify(menuRepository).findById(1L);
        verify(menuRepository).save(any(Menu.class));
        assertEquals("New Menu", updatedMenu.getName());
        assertEquals(List.of(RECIPES.get(1)), updatedMenu.getRecipeList());
    }

    @Test
    void testDeleteMenu_menuExists_menuDeleted() {
        Menu menu = new Menu(1L, "New Menu", List.of(RECIPES.get(1)));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(any());
        menuService.deleteMenu(1L);
        verify(menuRepository).deleteById(any());
    }

}