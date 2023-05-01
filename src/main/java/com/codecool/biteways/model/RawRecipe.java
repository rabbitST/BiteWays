package com.codecool.biteways.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RawRecipe {

    @NotBlank(message = "Please provide a name for the recipe.")
    @Size(min = 3, max = 30, message = "Please enter a recipe name that is between 3 and 30 characters in length.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.]*$", message = "The recipe name can only contain letters, numbers, hyphens, spaces.")
    private String name;

    @NotBlank(message = "Please provide instructions for the recipe.")
    @Size(min = 5, max = 600, message = "Please enter instructions that are between 8 and 600 characters long.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.\\n\\r]*$", message = "Please enter instructions that only contain letters, numbers, hyphens, and spaces.")
    private String instructions;

    @NotBlank(message = "Please provide ingredients for the recipe.")
    @Size(min = 5, max = 600, message = "Please enter ingredients that are between 8 and 600 characters long.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.\\r\\n]*$", message = "Please enter ingredients that only contain letters, numbers, hyphens, spaces, and line breaks.")
    private String ingredients;

    public RawRecipe(String name, String instructions, String ingredients) {
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }
}
