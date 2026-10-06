package com.example.smartpantrymanager.Models;

// class for one ingredient that a recipe needs for example 1 tbsp of milk
// only holds the data
public class RecipeIngredient{
    // id of the ingredient
    private int ingredientId;

    // id of the recipe
    private int recipeId;

    // name of the ingredient
    private String ingredientName;

    // quantity of the ingredient
    private double quantity;

    // the unit for the quantity, example g or kg
    private String unit;

    // both the ids will start at -1 because it not in the database
    public RecipeIngredient(){
        ingredientId = -1;
        recipeId = -1;
    }

    // set up an ingredient with its name, quantity and units already
    public RecipeIngredient(String ingredientName, double quantity, String unit){
        this();
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
    }

    // getters and setters, these let other classes read and change the fields
    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }


}
