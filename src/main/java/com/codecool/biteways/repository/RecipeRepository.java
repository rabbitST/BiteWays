package com.codecool.biteways.repository;

import com.codecool.biteways.model.Recipe;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;

@Table(name = "recipe")
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
