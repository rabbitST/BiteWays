package com.codecool.biteways.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RawRecipe {
    private String name;
    private String instructions;
    private String ingredients;

    public RawRecipe(String name, String instructions, String ingredients) {
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }
}
