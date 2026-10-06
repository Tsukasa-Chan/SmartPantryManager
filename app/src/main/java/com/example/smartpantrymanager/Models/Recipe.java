package com.example.smartpantrymanager.Models;

import java.util.ArrayList;
import java.util.List;

// class to hold the data of a singular recipe
public class Recipe {
    // recipe id
    private int recipeId;

    // recipe name
    private String name;

    // recipe steps
    private String steps;

    // list of ingredients used in recipe
    private List<RecipeIngredient> ingredients;

    // constructor to set up an empty recipe
    // id starts at -1 and ingredients list is empty, so it is never null
    public Recipe(){
        recipeId = -1;
        ingredients = new ArrayList<>();
    }

    // this constructor lets us set up a recipe with a name and steps already fill in
    public Recipe(String name, String steps){
        this();
        this.name = name;
        this.steps = steps;
    }

    // getters and setters that let us get and set the values
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
