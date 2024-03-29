package com.codecool.biteways.model;

import com.codecool.biteways.model.enums.UnitType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Please provide a name for the ingredient.")
    @Size(min = 2, max = 50, message = "Please enter an ingredient name that is between 3 and 30 characters in length.")
    @Pattern(regexp = "^[a-zA-Z0-9\\-\\s,.éáíóöőüúÉÁÍÓÖŐÜÚ]*$", message = "The ingredient name can only contain letters, numbers, and hyphens.")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "recipe_id", foreignKey = @ForeignKey(name = "FK_INGREDIENT_RECIPE"))
    @JsonIgnore
    private Recipe recipe;

    @Digits(integer = 10, fraction = 2, message = "The quantity should be a numeric value with up to 2 decimal places.")
    private Float quantity;

    @NotNull(message = "Please select a valid unit type.")
    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    public Ingredient(String name, Float quantity, UnitType unitType, Recipe recipe) {
        this.name = name;
        this.quantity = quantity;
        this.unitType = unitType;
        this.recipe = recipe;
    }

    public Ingredient(String name, Float quantity, UnitType unitType) {
        this.name = name;
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
        return Objects.equals(name, that.name) && Objects.equals(quantity, that.quantity) && unitType == that.unitType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, unitType);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unitType=" + unitType +
                '}';
    }
}