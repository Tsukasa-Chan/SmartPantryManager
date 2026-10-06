package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantrymanager.Models.PantryItem;

import java.util.ArrayList;
import java.util.Calendar;

// handles all the database work for pantry items
// create, read, update and delete
// keep using this class instead of writing SQL everywhere
public class PantryDataSource {
    // the open database connection
    private SQLiteDatabase database;

    // the helper that creates and opens the database
    private SmartPantryDBHelper dbHelper;

    // constructor that creates the helper
    public  PantryDataSource(Context context){
        dbHelper = new SmartPantryDBHelper(context);
    }

    // opens the database so it can be read from and written to
    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    // closes the database. this must be called when finished
    public void close(){
        dbHelper.close();
    }

    // method to add a new item to the database
    // return true if it succeeded and false if not succeeded
    public boolean insertItem(PantryItem item){
        boolean didSucceed = false;

        try{
            // content values will hold the column names and the values to save in them
            ContentValues values = new ContentValues();
            values.put("name",item.getName());
            values.put("quantity",item.getQuantity());
            values.put("unit",item.getUnit());

            // The expiry date is saved as the number of milliseconds
            // if there is no expiry date, null is saved
            if(item.getExpiryDate()!=null){
                values.put("expiry_date",String.valueOf(item.getExpiryDate().getTimeInMillis()));
            }else{
                values.putNull("expiry_date");
            }

            // insert returns the id of the new row, or -1 if it failed
            didSucceed = database.insert("pantry_items",null,values)>0;
        }catch (Exception e){
            // didSucceed stays false, so the screen shows an error
        }
        return didSucceed;
    }

    // changes an existing item in the database
    // returns true if successful, if not false
    public boolean updateItem(PantryItem item){
        boolean didSucceed = false;

        try{
            // id of the row that must be updated
            long rowId = item.getItemId();
            ContentValues values = new ContentValues();
            values.put("name",item.getName());
            values.put("quantity",item.getQuantity());
            values.put("unit",item.getUnit());

            if(item.getExpiryDate()!=null){
                values.put("expiry_date",String.valueOf(item.getExpiryDate().getTimeInMillis()));
            }else{
                values.putNull("expiry_date");
            }

            // returns how many rows were changed
            // using  _id makes sure only the one that has that id gets updated
            didSucceed = database.update("pantry_items",values,
                    "_id="+rowId,null)>0;

        }catch (Exception e){
            // didSucceed stays false, so the screen shows an error message
        }
        return  didSucceed;
    }

    // remove an item from the database
    // returns true if the row was deleted or else returns false
    public boolean deleteItem(int itemId){
        boolean didSucceed = false;

        try{
            // will return how many rows were removed
            didSucceed = database.delete("pantry_items",
                    "_id="+itemId,null)>0;
        }catch (Exception e){
            // didSucceed stays false, so the screen shows an error message
        }
        return didSucceed;
    }

    // get all the items from the database in the order the user chose in settings
    public ArrayList<PantryItem> getAllItems(String sortField, String sortOrder){
        ArrayList<PantryItem> items = new ArrayList<>();

        try{
            // only allow for ASC or DESC, anything else becomes ASC
            String order;
            if(sortOrder.equalsIgnoreCase("DESC")){
                order = "DESC";
            }else{
                order = "ASC";
            }

            // build the ORDER BY part of the query depending on what to sort by
            String orderBy;
            if(sortField.equals("quantity")){
                orderBy = "quantity " + order;
            }else if(sortField.equals("expiry_date")){
                // item with no expiry date will go to the bottom
                // CAST is turning the saved text into a number so the dates can be sorted correctly
                orderBy = "expiry_date IS NULL, CAST(expiry_date AS INTEGER) " + order;
            }else{
                // sort by name, ignoring capital letters
                orderBy = "name COLLATE NOCASE " + order;
            }

            String query = "SELECT * FROM pantry_items ORDER BY " + orderBy;
            // a cursor is used to move through the rows that query returns
            Cursor cursor = database.rawQuery(query,null);

            // go through every row and turn it into a pantry item
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    PantryItem item = new PantryItem();
                    item.setItemId(cursor.getInt(0));
                    item.setName(cursor.getString(1));
                    item.setQuantity(cursor.getDouble(2));
                    item.setUnit(cursor.getString(3));

                    // only set the expiry date if it is not null
                    if(!cursor.isNull(4)){
                        Calendar expiryDate = Calendar.getInstance();
                        expiryDate.setTimeInMillis(Long.parseLong(cursor.getString(4)));
                        item.setExpiryDate(expiryDate);
                    }

                    items.add(item);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }catch(Exception e){
            // empty list returned, so the screen shows the empty message
        }
        return items;
    }

    // gets all the items sorted by name from A to Z
    public ArrayList<PantryItem> getAllItems(){
        return getAllItems("name","ASC");
    }

    // gets one item from the database using its id
    public PantryItem getItem(int itemId){
        PantryItem item = new PantryItem();

        try{
            String query = "SELECT * FROM pantry_items WHERE _id="+itemId;
            Cursor cursor = database.rawQuery(query,null);

            // only fill in the item if a row was found
            if(cursor.moveToFirst()){
                item.setItemId(cursor.getInt(0));
                item.setName(cursor.getString(1));
                item.setQuantity(cursor.getDouble(2));
                item.setUnit(cursor.getString(3));

                if(!cursor.isNull(4)){
                    Calendar expiryDate = Calendar.getInstance();
                    expiryDate.setTimeInMillis(Long.parseLong(cursor.getString(4)));
                    item.setExpiryDate(expiryDate);
                }
            }
            cursor.close();
        }catch(Exception e){
            // an empty pantry, so id of -1 is returned
        }
        return item;
    }
}
