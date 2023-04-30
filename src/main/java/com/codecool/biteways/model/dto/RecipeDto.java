package com.codecool.biteways.model.dto;

import com.codecool.biteways.model.Ingredient;
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
    private String name;
    private int downloaded;
    private String instructions;
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
