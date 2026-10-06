package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.smartpantrymanager.Models.PantryItem;

import java.util.Calendar;

// this screen is used to add a new pantry item or edit one that already exists
public class AddEditActivity extends AppCompatActivity
        implements DatePickerDialog.SaveDateListener {

    // method to open another screen
    // CLEAR_TOP will reuse the screen if it is already open, instead of stacking a new copy
    private void openActivity(Class activity){
        Intent intent = new Intent(this,activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // back button lets us go back to pantry lists
    private void initBackButton(){
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
            openActivity(MainActivity.class);
        });
    }

    // fill in the drop with options from UNIT_OPTIONS
    private void initUnitSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                UNIT_OPTIONS
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);
    }

    // expiry date button when clicked opens up the date picker dialog
    private void initExpiryDateButton() {
        Button btnExpiryDate = findViewById(R.id.btnExpiryDate);
        btnExpiryDate.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            DatePickerDialog dialog = new DatePickerDialog();
            dialog.show(fm, "DatePick");
        });
    }

    // will call the date picker dialog when the user picks a date
    // will save the date and show it on the screen
    @Override
    public void didFinishDatePickerDialog(Calendar selectedDate) {
        currentItem.setExpiryDate(selectedDate);
        textExpiryDate.setText(DateFormat.format("dd MMM yyyy", selectedDate));
    }

    // saves item to the database
    // if the item is -1 we know it is a new item
    // if it is not -1 we know its an existing item
    private void saveItemToDatabase(){
        PantryDataSource ds = new PantryDataSource(this);
        boolean didSucceed;

        try{
            ds.open();
            if(itemId == -1) {
                didSucceed = ds.insertItem(currentItem);
            }else{
                didSucceed = ds.updateItem(currentItem);
            }
            ds.close();
        }catch(Exception e){
            didSucceed = false;
        }

        if(didSucceed){
            // save was successful, we close the screen and go back to the pantry list
           finish();
        }else{
            // not saved, stay on this screen and tell the user
            Toast.makeText(this,"Could not save ingredient. Please try again", Toast.LENGTH_LONG).show();
        }
    }

    // sets up the save button
    // an item is only saved if the input passes validation
    private void initSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            if (isInputValid()) {
                saveItemToDatabase();
            }
        });
    }


    // checks what the user typed in and will return true if everything is valid
    // if something is invalid it will show an error message and return false
    private boolean isInputValid() {
        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();

        // an item requires a name, it cannot be empty
        if (name.isEmpty()) {
            showError("Please enter an ingredient name");
            return false;
        }

        // an item requires a quantity, it cannot be empty
        if (quantityText.isEmpty()) {
            showError("Please enter a quantity");
            return false;
        }

        // check that the quantity entered was a number and not a word
        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            showError("Quantity must be a number");
            return false;
        }

        // the quantity needs to be more than 0
        if (quantity <= 0) {
            showError("Quantity must be greater than 0");
            return false;
        }

        // if everything is valid, then hide an old error message
        textError.setVisibility(View.GONE);

        currentItem.setName(name);
        currentItem.setQuantity(quantity);
        currentItem.setUnit((String) spinnerUnit.getSelectedItem());

        return true;
    }

    // fills the form with the details of the item if its being edited
    // so the person doesn't need to enter everything again
    // they can just change what needs to be changed and leave other
    // things the same
    private void loadItemData(){
        editName.setText(currentItem.getName());

        editQuantity.setText(String.valueOf(currentItem.getQuantity()));

        // find the position of the items unit in the dropdown list
        int unitPosition = -1;

        for(int i = 0; i < UNIT_OPTIONS.length;i++){
            if(UNIT_OPTIONS[i].equals(currentItem.getUnit())){
                unitPosition = i;
                break;
            }
        }

        // if it is -1, then it was not found
        if(unitPosition!=-1){
            spinnerUnit.setSelection(unitPosition);
        }

        // only show an expiry date if the item has one
        if(currentItem.getExpiryDate()!=null){
            textExpiryDate.setText(DateFormat.format("dd MMM yyyy"
            ,currentItem.getExpiryDate()));
        }

    }

    // shows an error message on the screen
    private void showError(String message) {
        textError.setText(message);
        textError.setVisibility(View.VISIBLE);
    }

    // the units the user can choose from in the dropdown
    private static final String[] UNIT_OPTIONS = {
            "g", "kg", "ml", "l", "tsp", "tbsp", "cup", "pcs"
    };

    // the item that we are adding or editing
    private PantryItem currentItem;

    // the id of the item being editted
    // if it is -1 we know it is being added and not editted
    private int itemId;

    // the views on the screen
    private EditText editName;
    private EditText editQuantity;
    private Spinner spinnerUnit;
    private TextView textExpiryDate;
    private TextView textError;

    // runs once when the screen is first created

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get the itemId that was sent with the Intent from the pantry lisy
        // if nothing was sent, it will say -1 which means its a new item
       Intent intent = getIntent();
       itemId = intent.getIntExtra("itemId",-1);

       if(itemId == -1){
           // adding so we start with an empty item
           currentItem = new PantryItem();
       }else{
           // editing so we load the existing items from the database
           PantryDataSource ds = new PantryDataSource(this);

           try{
               ds.open();
               currentItem = ds.getItem(itemId);
               ds.close();
           }catch(Exception e){
               currentItem = new PantryItem();
           }
       }

        // link the variables to the views in the layout
        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        textExpiryDate = findViewById(R.id.textExpiryDate);
        textError = findViewById(R.id.textError);

        // call the methods from dropdown and buttons
        initUnitSpinner();
        initExpiryDateButton();
        initSaveButton();
        initBackButton();

        // when editingm change the title and fill form with the items details
        if(itemId != -1){
            TextView textTitle = findViewById(R.id.textTitle);
            textTitle.setText("Edit Ingredient");
            loadItemData();
        }
    }


}