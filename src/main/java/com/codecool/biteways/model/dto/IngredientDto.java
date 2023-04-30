package com.codecool.biteways.model.dto;

import com.codecool.biteways.model.enums.UnitType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    private Long id;

    @NotBlank(message = "Please provide a name for the ingredient.")
    @Size(min = 3, max = 30, message = "Please enter an ingredient name that is between 3 and 30 characters in length.")
    @Pattern(regexp = "^[a-zA-Z0-9\\-\\s]*$", message = "The ingredient name can only contain letters, numbers, and hyphens.")
    private String name;

    private String recipeName;

    @Digits(integer = 10, fraction = 2, message = "The quantity should be a numeric value with up to 2 decimal places.")
    private Float quantity;

    @NotNull(message = "Please select a valid unit type.")
    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    public IngredientDto(String name, String recipeName, Float quantity, UnitType unitType) {
        this.name = name;
        this.recipeName = recipeName;
        this.quantity = quantity;
        this.unitType = unitType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientDto that = (IngredientDto) o;
        return Objects.equals(name, that.name) && Objects.equals(recipeName, that.recipeName) && Objects.equals(quantity, that.quantity) && unitType == that.unitType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, recipeName, quantity, unitType);
    }

    @Override
    public String toString() {
        return "IngredientDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", quantity=" + quantity +
                ", unitType=" + unitType +
                '}';
    }
}
