package com.codecool.biteways.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChefDto {

    @Digits(integer = 10,fraction = 0 , message = "The id must be a number!")
    private Long id;
    @NotBlank(message = "The name of the chef must not be left blank.")
    @Size(min = 3,max = 50, message = "The name must be between 3 and 50 characters in length.")
    private String name;

    @Valid
    private List<Recipe> recipeList;
}