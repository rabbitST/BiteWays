package com.codecool.biteways.repository;

import com.codecool.biteways.model.Ingredient;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;

@Table(name = "ingredient")
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
