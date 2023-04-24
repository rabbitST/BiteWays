package com.codecool.biteways.model.dto;

import com.codecool.biteways.model.enums.UnitType;
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
    private String name;
    private String recipeName;
    private Float quantity;
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
