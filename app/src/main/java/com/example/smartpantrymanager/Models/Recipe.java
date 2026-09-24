package com.example.smartpantrymanager.Models;

import java.util.ArrayList;
import java.util.List;
public class Recipe {
    private int recipeId;

    private String name;

    private String steps;

    private List<RecipeIngredient> ingredients;

    public Recipe(){
        recipeId = -1;
        ingredients = new ArrayList<>();
    }

    public Recipe(String name, String steps){
        this();
        this.name = name;
        this.steps = steps;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
