package com.codecool.biteways.controller;

import com.codecool.biteways.model.RawRecipe;
import com.codecool.biteways.model.Recipe;
import com.codecool.biteways.model.dto.RecipeDto;
import com.codecool.biteways.service.RecipeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/biteways/recipe")
@Log4j2
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @GetMapping("/save")
    @ResponseBody
    public Recipe saveRecipe(@RequestBody RawRecipe rawRecipe) {
        return recipeService.saveRecipe(rawRecipe);
    }

    @GetMapping(value = "/saveform")
    public String displayForm() {
        return "/RecipeForm";
    }

    @GetMapping
    @ResponseBody
    public List<RecipeDto> findAllRecipe() {
        return recipeService.findAllRecipe();
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public RecipeDto findRecipeById(@PathVariable("id") Long id) {
        return recipeService.findRecipeById(id);
    }

    @PutMapping(value = "/{id}")
    public void updateRecipeById(
            @PathVariable("id") Long id,
            @RequestBody Recipe recipe
    ) {
        recipeService.updateRecipe(id, recipe);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public void deleteRecipe(@PathVariable("id") Long id) {
        recipeService.deleteRecipe(id);
    }

}
