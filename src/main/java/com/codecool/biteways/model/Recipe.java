package com.codecool.biteways.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    private String name;
    private int downloaded;
    private String instructions;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    List<Ingredient> ingredientList;
    @ManyToMany(mappedBy = "recipeList")
    @JsonIgnore
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
    public String
    toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredientList=" + ingredientList +
                ", downloaded=" + downloaded +
                '}';
    }
}
