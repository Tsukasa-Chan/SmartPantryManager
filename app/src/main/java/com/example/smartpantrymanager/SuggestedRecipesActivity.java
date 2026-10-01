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

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerRecipeList;
    private TextView textEmptyRecipes;

    private void openActivity(Class activity){
        Intent intent = new Intent(this,activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initNavButtons(){
        Button btnNavPantry = findViewById(R.id.btnNavPantry);
        Button btnNavRecipes = findViewById(R.id.btnNavRecipes);
        Button btnNavSettings = findViewById(R.id.btnNavSettings);

        btnNavPantry.setOnClickListener(v->{
            openActivity(MainActivity.class);
        });

        btnNavRecipes.setOnClickListener(v->{
            //
        });

        btnNavSettings.setOnClickListener(v->{
            openActivity(SettingsActivity.class);
        });
    }

    private void loadRecipeList(){
        PantryDataSource pantryDs = new PantryDataSource(this);
        RecipeDataSource recipeDs = new RecipeDataSource(this);
        ArrayList<PantryItem> pantryItems;
        ArrayList<Recipe> allRecipes;

        try{
            pantryDs.open();
            pantryItems = pantryDs.getAllItems();
            pantryDs.close();

            recipeDs.open();
            allRecipes = recipeDs.getAllRecipes();
            recipeDs.close();
        }catch(Exception e){
            pantryItems = new ArrayList<>();
            allRecipes = new ArrayList<>();
            Toast.makeText(this,"Error retrieving recipes",
                    Toast.LENGTH_LONG).show();
        }

        RecipeMatcher matcher = new RecipeMatcher();
        ArrayList<Recipe> matchingRecipes = matcher.getMatchingRecipes(allRecipes, pantryItems);

        if(matchingRecipes.isEmpty()){
            textEmptyRecipes.setVisibility(View.VISIBLE);
            recyclerRecipeList.setVisibility(View.GONE);
        }else{
            textEmptyRecipes.setVisibility(View.GONE);
            recyclerRecipeList.setVisibility(View.VISIBLE);

            recyclerRecipeList.setLayoutManager(new LinearLayoutManager(this));
            RecipeAdapter adapter = new RecipeAdapter(matchingRecipes);
            recyclerRecipeList.setAdapter(adapter);
        }
    }

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

        recyclerRecipeList = findViewById(R.id.recyclerRecipeList);
        textEmptyRecipes = findViewById(R.id.textEmptyRecipes);

        textEmptyRecipes.setVisibility(View.VISIBLE);
        recyclerRecipeList.setVisibility(View.GONE);

        initNavButtons();
    }
}