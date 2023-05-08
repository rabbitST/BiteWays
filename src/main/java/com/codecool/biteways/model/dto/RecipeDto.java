package com.codecool.biteways.model.dto;

import com.codecool.biteways.model.Ingredient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

    private Long id;

    @NotBlank(message = "Please provide a name for the recipe.")
    @Size(min = 3, max = 50, message = "Please enter a recipe name that is between 3 and 30 characters in length.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.éáíóöőüúÉÁÍÓÖŐÜÚ]*$", message = "The recipe name can only contain letters, numbers, hyphens, spaces.")
    private String name;

    private int downloaded;

    @NotBlank(message = "Please provide instructions for the recipe.")
    @Size(min = 8, max = 600, message = "Please enter instructions that are between 8 and 600 characters long.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-., \\n\\r\"'’]*$", message = "Please enter instructions that only contain letters, numbers, hyphens, quote, double quote  and spaces.")
    private String instructions;

    @Valid
    private List<Ingredient> ingredientList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDto recipeDto = (RecipeDto) o;
        return downloaded == recipeDto.downloaded && Objects.equals(name, recipeDto.name) && Objects.equals(instructions, recipeDto.instructions) && Objects.equals(ingredientList, recipeDto.ingredientList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, downloaded, instructions, ingredientList);
    }

    @Override
    public String toString() {
        return "RecipeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", downloaded=" + downloaded +
                ", instructions='" + instructions + '\'' +
                ", ingredientList=" + ingredientList +
                '}';
    }
}
