package com.codecool.biteways.model.dto;

import com.codecool.biteways.model.Recipe;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuDto {

    private Long id;
    private String name;
    private List<Recipe> recipeList=new ArrayList<>();

}
