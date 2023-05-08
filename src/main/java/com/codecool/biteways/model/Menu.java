package com.codecool.biteways.model;

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
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please provide a name for the menu.")
    @Size(min = 3, max = 50, message = "Please enter a menu name that is between 3 and 30 characters in length.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.éáíóöőüúÉÁÍÓÖŐÜÚ]*$", message = "The menu name can only contain letters, numbers, hyphens, spaces.")
    private String name;

    @ManyToMany(cascade = {
            CascadeType.MERGE
    })
    @JoinTable(name="menu_recipe",
            joinColumns = @JoinColumn(name="menu_id"),
            inverseJoinColumns = @JoinColumn(name="recipe_id")
    )
    @Valid
    private List<Recipe> recipeList=new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(name, menu.name) && Objects.equals(recipeList, menu.recipeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, recipeList);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", recipeList=" + recipeList +
                '}';
    }
}
