package com.codecool.biteways.model.dto;

import com.codecool.biteways.model.Recipe;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuDto {

    private Long id;

    @NotBlank(message = "Please provide a name for the menu.")
    @Size(min = 3, max = 30, message = "Please enter a menu name that is between 3 and 30 characters in length.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.]*$", message = "The menu name can only contain letters, numbers, hyphens, spaces.")
    private String name;
    @Valid
    private List<Recipe> recipeList=new ArrayList<>();

}
