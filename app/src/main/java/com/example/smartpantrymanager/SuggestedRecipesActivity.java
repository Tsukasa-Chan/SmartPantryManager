package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.Models.PantryItem;
import com.example.smartpantrymanager.Models.Recipe;

import java.util.ArrayList;

// only shows recipes that can be made with whats in the pantry
public class SuggestedRecipesActivity extends AppCompatActivity {

    // the lists that shows the matching recipe
    private RecyclerView recyclerRecipeList;

    // message the user sees if they have nothing that matches
    private TextView textEmptyRecipes;

    // opens another screen
    private void openActivity(Class activity){
        Intent intent = new Intent(this,activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // buttons in the nav bar for moving between screens
    private void initNavButtons(){
        Button btnNavPantry = findViewById(R.id.btnNavPantry);
        Button btnNavRecipes = findViewById(R.id.btnNavRecipes);
        Button btnNavSettings = findViewById(R.id.btnNavSettings);

        // open the pantry list screen
        btnNavPantry.setOnClickListener(v->{
            openActivity(MainActivity.class);
        });

        // current screen we already are on
        btnNavRecipes.setOnClickListener(v->{
            //
        });

        // opens the settings screen
        btnNavSettings.setOnClickListener(v->{
            openActivity(SettingsActivity.class);
        });
    }

    // find the recipes that match the pantry and shows them in the list
    private void loadRecipeList(){
        PantryDataSource pantryDs = new PantryDataSource(this);
        RecipeDataSource recipeDs = new RecipeDataSource(this);
        ArrayList<PantryItem> pantryItems;
        ArrayList<Recipe> allRecipes;

        // get the pantry items and all the recipes from the db
        try{
            pantryDs.open();
            pantryItems = pantryDs.getAllItems();
            pantryDs.close();

            recipeDs.open();
            allRecipes = recipeDs.getAllRecipes();
            recipeDs.close();
        }catch(Exception e){
            // if something goes wrong, use an empty list and tell the user
            pantryItems = new ArrayList<>();
            allRecipes = new ArrayList<>();
            Toast.makeText(this,"Error retrieving recipes",
                    Toast.LENGTH_LONG).show();
        }

        // use the strict match making logic where only recipes where every ingredient
        // is in the pantry and you have enough
        RecipeMatcher matcher = new RecipeMatcher();
        ArrayList<Recipe> matchingRecipes = matcher.getMatchingRecipes(allRecipes, pantryItems);

        if(matchingRecipes.isEmpty()){
            // no recipes found
            textEmptyRecipes.setVisibility(View.VISIBLE);
            recyclerRecipeList.setVisibility(View.GONE);
        }else{
            // there are matches so show the list
            textEmptyRecipes.setVisibility(View.GONE);
            recyclerRecipeList.setVisibility(View.VISIBLE);

            recyclerRecipeList.setLayoutManager(new LinearLayoutManager(this));
            RecipeAdapter adapter = new RecipeAdapter(matchingRecipes);
            recyclerRecipeList.setAdapter(adapter);
        }
    }

    // runs everytime the screen comes back into view
    @Override
    protected void onResume(){
        super.onResume();
        loadRecipeList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggestedrecipes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // links the variables to the views in the layout
        recyclerRecipeList = findViewById(R.id.recyclerRecipeList);
        textEmptyRecipes = findViewById(R.id.textEmptyRecipes);

        // starts with an empty message until the recipes are loaded
        textEmptyRecipes.setVisibility(View.VISIBLE);
        recyclerRecipeList.setVisibility(View.GONE);

        // call the method for the nav buttons
        initNavButtons();
    }
}