package com.example.smartpantrymanager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantrymanager.Models.Recipe;
import com.example.smartpantrymanager.Models.RecipeIngredient;

import java.util.ArrayList;

public class RecipeDataSource {
    private SQLiteDatabase database;
    private SmartPantryDBHelper dbHelper;

    public RecipeDataSource(Context context){
        dbHelper = new SmartPantryDBHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    // Gets every recipe, each one already filled with its ingredients
    public ArrayList<Recipe> getAllRecipes(){
        ArrayList<Recipe> recipes = new ArrayList<>();

        try{
            String query = "SELECT * FROM recipes ORDER BY name";
            Cursor cursor = database.rawQuery(query,null);

            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    Recipe recipe = new Recipe();
                    recipe.setRecipeId(cursor.getInt(0));
                    recipe.setName(cursor.getString(1));
                    recipe.setSteps(cursor.getString(2));
                    recipe.setIngredients(getIngredientsForRecipe(recipe.getRecipeId()));

                    recipes.add(recipe);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }catch(Exception e){
            // empty list is returned. so the screen shows the empty message
        }
        return recipes;
    }

    // Gets one recipe (with its ingredients) for the recipe detail screen
    public Recipe getRecipe(int recipeId){
        Recipe recipe = new Recipe();

        try{
            String query = "SELECT * FROM recipes WHERE _id="+recipeId;
            Cursor cursor = database.rawQuery(query,null);

            if(cursor.moveToFirst()){
                recipe.setRecipeId(cursor.getInt(0));
                recipe.setName(cursor.getString(1));
                recipe.setSteps(cursor.getString(2));
                recipe.setIngredients(getIngredientsForRecipe(recipe.getRecipeId()));
            }
            cursor.close();
        }catch(Exception e){
            // empty recipe is returned, so the detail screen closes
        }
        return recipe;
    }

    public ArrayList<RecipeIngredient> getIngredientsForRecipe(int recipeId){
        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();

        try{
            String query = "SELECT * FROM recipe_ingredients WHERE recipe_id="+recipeId;
            Cursor cursor = database.rawQuery(query,null);

            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    RecipeIngredient ingredient = new RecipeIngredient();
                    ingredient.setIngredientId(cursor.getInt(0));
                    ingredient.setRecipeId(cursor.getInt(1));
                    ingredient.setIngredientName(cursor.getString(2));
                    ingredient.setQuantity(cursor.getDouble(3));
                    ingredient.setUnit(cursor.getString(4));

                    ingredients.add(ingredient);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }catch(Exception e){
            // empty list is returned
        }
        return ingredients;
    }
}