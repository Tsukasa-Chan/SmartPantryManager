package com.example.smartpantrymanager.Models;

public class RecipeIngredient{
    private int ingredientId;

    private int recipeId;

    private String ingredientName;

    private double quantity;

    private String unit;

    public RecipeIngredient(){
        ingredientId = -1;
        recipeId = -1;
    }

    public RecipeIngredient(String ingredientName, double quantity, String unit){
        this();
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
    }

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
