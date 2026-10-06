package com.example.smartpantrymanager;

// imports
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

// main screen of our app, that shows the items in our pantry
public class MainActivity extends AppCompatActivity  implements  PantryAdapter.ItemDeleteListener{

    // list that shows the pentry items
    private RecyclerView recyclerPantryList;
    // the message shown if the pantry is empty
    private TextView textEmptyPantry;

    // opens another screen, use it to navigate between the screens
    private void openActivity(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }

    // clicking the add button will take us to the screen to add a new item
    private void initAddButton() {
        Button btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(v -> {
            openActivity(AddEditActivity.class);
        });
    }

    // method for our nav buttons at the bottom of our screen
    private void initNavButtons() {
        Button btnNavPantry = findViewById(R.id.btnNavPantry);
        Button btnNavRecipes = findViewById(R.id.btnNavRecipes);
        Button btnNavSettings = findViewById(R.id.btnNavSettings);

        // the pantry button doesn't take us anywhere because we already on that screen
        btnNavPantry.setOnClickListener(v -> {
            //
        });

        // opens the suggested recipes screen
        btnNavRecipes.setOnClickListener(v -> {
            openActivity(SuggestedRecipesActivity.class);
        });

        // opens the settings screen
        btnNavSettings.setOnClickListener(v -> {
            openActivity(SettingsActivity.class);
        });
    }

    // method that gets pantry items from the database and shows them in a list
    private void loadPantryList(){

        // reads the pantry items in the sort settings we saved in settings
        // if we never save anything it uses a-z by default
        SharedPreferences preferences = getSharedPreferences(
                "SmartPantryPreferences",Context.MODE_PRIVATE
        );

        String sortField = preferences.getString("sortfield","name");
        String sortOrder = preferences.getString("sortorder","ASC");

        PantryDataSource ds = new PantryDataSource(this);
        ArrayList<PantryItem> pantryItems;

        // get all the items from the database
        try{
            ds.open();
            pantryItems = ds.getAllItems(sortField,sortOrder);
            ds.close();
        }catch(Exception e){
            // if something goes wrong, use an empty list and tell the user
            pantryItems = new ArrayList<>();
            Toast.makeText(this,"Error retrieving pantry items",
                    Toast.LENGTH_LONG).show();
        }

        if(pantryItems.isEmpty()){
            // shows the empty message and hides the list
            textEmptyPantry.setVisibility(View.VISIBLE);
            recyclerPantryList.setVisibility(View.GONE);
        }else{
            // there are actually items so we hide the empty message and show the items
            textEmptyPantry.setVisibility(View.GONE);
            recyclerPantryList.setVisibility(View.VISIBLE);

            // show the items one below the other so it's easier to see
            recyclerPantryList.setLayoutManager(new LinearLayoutManager(this));

            // call the screen when an item is deleted
            PantryAdapter adapter = new PantryAdapter(pantryItems,this);
            recyclerPantryList.setAdapter(adapter);
        }

    }

    // call by the adapter after an item is deleted, so list is reloaded
    // so doesn't show the deleted item anymore
    @Override
    public void onItemDeleted(){
        loadPantryList();
    }

    // runs everytime the screen comes back into view
    // so the list is always up to date
    @Override
    protected void onResume(){
        super.onResume();
        loadPantryList();
    }

    // runs once when the screen is first created
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

        // link the variables to the views in the layout
        recyclerPantryList = findViewById(R.id.recyclerPantryList);
        textEmptyPantry = findViewById(R.id.textEmptyPantry);

        // will start with the empty message showing until items are loaded
        textEmptyPantry.setVisibility(View.VISIBLE);
        recyclerPantryList.setVisibility(View.GONE);

        // call the methods
        initAddButton();
        initNavButtons();
    }


}