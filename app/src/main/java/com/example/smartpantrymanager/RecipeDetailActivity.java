package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartpantrymanager.Models.Recipe;
import com.example.smartpantrymanager.Models.RecipeIngredient;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textRecipeName;
    private TextView textIngredients;
    private TextView textSteps;

    private void initBackButton(){
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
            finish();
        });
    }

    private String formatQuantity(double quantity){
        if(quantity == Math.floor(quantity)){
            return String.valueOf((int) quantity);
        }
        return String.valueOf(quantity);
    }

    private String capitalise(String text){
        if(text == null || text.isEmpty()){
            return "";
        }
        return text.substring(0,1).toUpperCase() + text.substring(1);
    }

    private void loadRecipe(int recipeId){
        Recipe recipe;
        RecipeDataSource ds = new RecipeDataSource(this);

        try{
            ds.open();
            recipe = ds.getRecipe(recipeId);
            ds.close();
        }catch(Exception e){
            recipe = new Recipe();
        }

        // getRecipe gives back a recipe with id -1 when it could not be found
        if(recipe.getRecipeId() == -1){
            Toast.makeText(this,"Could not load recipe",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        textRecipeName.setText(recipe.getName());

        String ingredientsText = "";
        for(RecipeIngredient ingredient : recipe.getIngredients()){
            if(!ingredientsText.isEmpty()){
                ingredientsText += "\n";
            }
            ingredientsText += "- " + capitalise(ingredient.getIngredientName())
                    + ": " + formatQuantity(ingredient.getQuantity())
                    + " " + ingredient.getUnit();
        }
        textIngredients.setText(ingredientsText);

        // Put each sentence of the steps on its own line so it is easier to read
        String stepsText = recipe.getSteps().replace(". ", ".\n\n");
        textSteps.setText(stepsText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textRecipeName = findViewById(R.id.textRecipeName);
        textIngredients = findViewById(R.id.textIngredients);
        textSteps = findViewById(R.id.textSteps);

        initBackButton();

        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("recipeId",-1);

        loadRecipe(recipeId);
    }
}