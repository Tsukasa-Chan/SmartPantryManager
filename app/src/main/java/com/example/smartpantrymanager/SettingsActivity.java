package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

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
            openActivity(SuggestedRecipesActivity.class);
        });

        btnNavSettings.setOnClickListener(v->{
            //
        });
    }


    private void initSettings(){
        SharedPreferences preferences = getSharedPreferences(
                "SmartPantryPreferences", Context.MODE_PRIVATE);

        String sortBy = preferences.getString("sortfield","name");
        String sortOrder = preferences.getString("sortorder","ASC");

        RadioButton rbName = findViewById(R.id.radioName);
        RadioButton rbExpiry = findViewById(R.id.radioExpiry);
        RadioButton rbQuantity = findViewById(R.id.radioQuantity);

        if(sortBy.equalsIgnoreCase("name")){
            rbName.setChecked(true);
        }else if(sortBy.equalsIgnoreCase("expiry_date")){
            rbExpiry.setChecked(true);
        }else{
            rbQuantity.setChecked(true);
        }

        RadioButton rbASC = findViewById(R.id.radioAscending);
        RadioButton rbDSC = findViewById(R.id.radioDescending);

        if(sortOrder.equalsIgnoreCase("ASC")){
            rbASC.setChecked(true);
        }else{
            rbDSC.setChecked(true);
        }

    }

    private void initSortByClick(){
        RadioGroup sortByGroup = findViewById(R.id.radioGroupSortBy);

        sortByGroup.setOnCheckedChangeListener((group,checkedId)->{
            SharedPreferences.Editor editor = getSharedPreferences(
                    "SmartPantryPreferences",Context.MODE_PRIVATE
            ).edit();

            if(checkedId == R.id.radioName){
                editor.putString("sortfield","name");
            }else if(checkedId == R.id.radioExpiry){
                editor.putString("sortfield","expiry_date");
            }else{
                editor.putString("sortfield","quantity");
            }
            editor.apply();
        });
    }

    private void initSortOrderClick(){
        RadioGroup sortOrderGroup = findViewById(R.id.radioGroupSortOrder);

        sortOrderGroup.setOnCheckedChangeListener((group,checkedId)->{
           SharedPreferences.Editor editor = getSharedPreferences(
                   "SmartPantryPreferences",Context.MODE_PRIVATE).edit();

           if(checkedId == R.id.radioAscending){
               editor.putString("sortorder","ASC");
           }else{
               editor.putString("sortorder","DESC");
           }

           editor.apply();

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavButtons();
        initSettings();
        initSortByClick();
        initSortOrderClick();
    }
}