package com.codecool.biteways.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuDto {

    private Long id;
    private String name;
    private List<String> recipes = new ArrayList<>();

}
