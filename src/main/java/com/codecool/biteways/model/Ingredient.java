package com.codecool.biteways.model;

import com.codecool.biteways.model.enums.UnitType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = true, foreignKey = @ForeignKey(name = "FK_INGREDIENT_RECIPE"))
    @JsonBackReference
    private Recipe recipe;
    private Float quantity;
    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    public Ingredient(String name, Recipe recipe, Float quantity, UnitType unitType) {
        this.name = name;
        this.recipe = recipe;
        this.quantity = quantity;
        this.unitType = unitType;
    }

    public Ingredient(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name) && Objects.equals(recipe, that.recipe) && Objects.equals(quantity, that.quantity) && unitType == that.unitType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, recipe, quantity, unitType);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", recipe=" + recipe +
                ", quantity=" + quantity +
                ", unitType=" + unitType +
                '}';
    }
}