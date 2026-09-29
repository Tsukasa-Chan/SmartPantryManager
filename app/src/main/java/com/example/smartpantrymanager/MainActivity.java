package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.Models.PantryItem;

public class MainActivity extends AppCompatActivity {

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
            //
        });

        btnNavSettings.setOnClickListener(v -> {
            openActivity(SettingsActivity.class);
        });
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