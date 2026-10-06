package com.example.smartpantrymanager.Models;

import java.util.Calendar;

// class for one item in our pantry, hold the data for one item
public class PantryItem{
    // item id so we know which item in the database
    private int itemId;

    // name of the ingredient
    private String name;

    // quantity of the ingredient
    private double quantity;

    // unit size of the ingredient
    private String unit;

    // expiry date for the item
    private Calendar expiryDate;

    // constructor to set up a new item not yet created
    public PantryItem(){
        itemId = -1;
        expiryDate = null;
    }

    // getters and setters that let us read values and set them
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Calendar getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Calendar expiryDate) {
        this.expiryDate = expiryDate;
    }



}
