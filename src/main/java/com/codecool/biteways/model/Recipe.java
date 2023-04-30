package com.codecool.biteways.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
@Table(name = "recipe")
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please provide a name for the recipe.")
    @Size(min = 3, max = 30, message = "Please enter a recipe name that is between 3 and 30 characters in length.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.]*$", message = "The recipe name can only contain letters, numbers, hyphens, spaces.")
    private String name;

    private int downloaded;

    @NotBlank(message = "Please provide instructions for the recipe.")
    @Size(min = 8, max = 600, message = "Please enter instructions that are between 8 and 600 characters long.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.]*$", message = "Please enter instructions that only contain letters, numbers, hyphens, and spaces.")
    private String instructions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @Valid
    List<Ingredient> ingredientList;

    @ManyToMany(mappedBy = "recipeList")
    @JsonIgnore
    @Valid
    private List<Menu> menuList=new ArrayList<>();

    public Recipe(String name, int downloaded, String instructions, List<Ingredient> ingredientList) {
        this.name = name;
        this.downloaded = downloaded;
        this.instructions = instructions;
        this.ingredientList = ingredientList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return downloaded == recipe.downloaded && Objects.equals(name, recipe.name) && Objects.equals(ingredientList, recipe.ingredientList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredientList, downloaded);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", downloaded=" + downloaded +
                ", instructions='" + instructions + '\'' +
                ", ingredientList=" + ingredientList +
                '}';
    }
}
