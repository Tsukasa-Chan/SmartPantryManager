package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.Models.PantryItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements  PantryAdapter.ItemDeleteListener{

    private RecyclerView recyclerPantryList;
    private TextView textEmptyPantry;

    private void openActivity(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }

    private void initAddButton() {
        Button btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(v -> {
            openActivity(AddEditActivity.class);
        });
    }

    private void initNavButtons() {
        Button btnNavPantry = findViewById(R.id.btnNavPantry);
        Button btnNavRecipes = findViewById(R.id.btnNavRecipes);
        Button btnNavSettings = findViewById(R.id.btnNavSettings);

        btnNavPantry.setOnClickListener(v -> {
            //
        });

        btnNavRecipes.setOnClickListener(v -> {
            openActivity(SuggestedRecipesActivity.class);
        });

        btnNavSettings.setOnClickListener(v -> {
            openActivity(SettingsActivity.class);
        });
    }

    private void loadPantryList(){

        SharedPreferences preferences = getSharedPreferences(
                "SmartPantryPreferences",Context.MODE_PRIVATE
        );

        String sortField = preferences.getString("sortfield","name");
        String sortOrder = preferences.getString("sortorder","ASC");

        PantryDataSource ds = new PantryDataSource(this);
        ArrayList<PantryItem> pantryItems;

        try{
            ds.open();
            pantryItems = ds.getAllItems(sortField,sortOrder);
            ds.close();
        }catch(Exception e){
            pantryItems = new ArrayList<>();
            Toast.makeText(this,"Error retrieving pantry items",
                    Toast.LENGTH_LONG).show();
        }

        if(pantryItems.isEmpty()){
            textEmptyPantry.setVisibility(View.VISIBLE);
            recyclerPantryList.setVisibility(View.GONE);
        }else{
            textEmptyPantry.setVisibility(View.GONE);
            recyclerPantryList.setVisibility(View.VISIBLE);

            recyclerPantryList.setLayoutManager(new LinearLayoutManager(this));
            PantryAdapter adapter = new PantryAdapter(pantryItems,this);
            recyclerPantryList.setAdapter(adapter);
        }

    }

    @Override
    public void onItemDeleted(){
        loadPantryList();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadPantryList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerPantryList = findViewById(R.id.recyclerPantryList);
        textEmptyPantry = findViewById(R.id.textEmptyPantry);

        textEmptyPantry.setVisibility(View.VISIBLE);
        recyclerPantryList.setVisibility(View.GONE);

        initAddButton();
        initNavButtons();
    }


}