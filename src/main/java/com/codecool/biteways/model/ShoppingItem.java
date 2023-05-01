package com.codecool.biteways.model;

import com.codecool.biteways.model.enums.UnitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingItem {
    private String itemName;
    private Float quantity;
    private UnitType unitType;
}
