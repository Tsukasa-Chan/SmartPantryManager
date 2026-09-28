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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.smartpantrymanager.Models.PantryItem;

import java.util.Calendar;


public class AddEditActivity extends AppCompatActivity
        implements DatePickerDialog.SaveDateListener {

    private void openActivity(Class activity){
        Intent intent = new Intent(this,activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initBackButton(){
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
            openActivity(MainActivity.class);
        });
    }

    private void initUnitSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                UNIT_OPTIONS
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);
    }

    private void initExpiryDateButton() {
        Button btnExpiryDate = findViewById(R.id.btnExpiryDate);
        btnExpiryDate.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            DatePickerDialog dialog = new DatePickerDialog();
            dialog.show(fm, "DatePick");
        });
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedDate) {
        currentItem.setExpiryDate(selectedDate);
        textExpiryDate.setText(DateFormat.format("dd MMM yyyy", selectedDate));
    }

    private void initSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            if (isInputValid()) {
                saveItemToDatabase();
            }
        });
    }


    private boolean isInputValid() {
        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();

        if (name.isEmpty()) {
            showError("Please enter an ingredient name");
            return false;
        }

        if (quantityText.isEmpty()) {
            showError("Please enter a quantity");
            return false;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            showError("Quantity must be a number");
            return false;
        }

        if (quantity <= 0) {
            showError("Quantity must be greater than 0");
            return false;
        }

        textError.setVisibility(View.GONE);

        currentItem.setName(name);
        currentItem.setQuantity(quantity);
        currentItem.setUnit((String) spinnerUnit.getSelectedItem());

        return true;
    }

    private void showError(String message) {
        textError.setText(message);
        textError.setVisibility(View.VISIBLE);
    }

    private void saveItemToDatabase() {
        textError.setVisibility(View.GONE);
    }
    private static final String[] UNIT_OPTIONS = {
            "g", "kg", "ml", "l", "tsp", "tbsp", "cup", "pcs"
    };

    private PantryItem currentItem;

    private EditText editName;
    private EditText editQuantity;
    private Spinner spinnerUnit;
    private TextView textExpiryDate;
    private TextView textError;

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

        currentItem = new PantryItem();

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        textExpiryDate = findViewById(R.id.textExpiryDate);
        textError = findViewById(R.id.textError);

        initUnitSpinner();
        initExpiryDateButton();
        initSaveButton();
        initBackButton();
    }


}