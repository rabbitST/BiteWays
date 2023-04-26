package com.codecool.biteways.model.dto;

import com.codecool.biteways.model.enums.UnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
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
    private Map<String, Map<Float, UnitType>> ingredients = new HashMap<>();

    @Override
    public String toString() {
        return "RecipeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", downloaded=" + downloaded +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDto recipeDto = (RecipeDto) o;
        return downloaded == recipeDto.downloaded && Objects.equals(name, recipeDto.name) && Objects.equals(ingredients, recipeDto.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients, downloaded);
    }
}
