package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantrymanager.Models.PantryItem;

import java.util.ArrayList;
import java.util.Calendar;

public class PantryDataSource {
    private SQLiteDatabase database;
    private SmartPantryDBHelper dbHelper;

    public  PantryDataSource(Context context){
        dbHelper = new SmartPantryDBHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public boolean insertItem(PantryItem item){
        boolean didSucceed = false;

        try{
            ContentValues values = new ContentValues();
            values.put("name",item.getName());
            values.put("quantity",item.getQuantity());
            values.put("unit",item.getUnit());

            if(item.getExpiryDate()!=null){
                values.put("expiry_date",String.valueOf(item.getExpiryDate().getTimeInMillis()));
            }else{
                values.putNull("expiry_date");
            }

            didSucceed = database.insert("pantry_items",null,values)>0;
        }catch (Exception e){
            //
        }
        return didSucceed;
    }

    public boolean updateItem(PantryItem item){
        boolean didSucceed = false;

        try{
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

            didSucceed = database.update("pantry_items",values,
                    "_id="+rowId,null)>0;

        }catch (Exception e){
            //
        }
        return  didSucceed;
    }

    public boolean deleteItem(int itemId){
        boolean didSucceed = false;

        try{
            didSucceed = database.delete("pantry_items",
                    "_id="+itemId,null)>0;
        }catch (Exception e){
            //
        }
        return didSucceed;
    }

    public ArrayList<PantryItem> getAllItems(String sortField, String sortOrder){
        ArrayList<PantryItem> items = new ArrayList<>();

        try{
            String order;
            if(sortOrder.equalsIgnoreCase("DESC")){
                order = "DESC";
            }else{
                order = "ASC";
            }

            String orderBy;
            if(sortField.equals("quantity")){
                orderBy = "quantity " + order;
            }else if(sortField.equals("expiry_date")){
                orderBy = "expiry_date IS NULL, CAST(expiry_date AS INTEGER) " + order;
            }else{
                orderBy = "name COLLATE NOCASE " + order;
            }

            String query = "SELECT * FROM pantry_items ORDER BY " + orderBy;
            Cursor cursor = database.rawQuery(query,null);

            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    PantryItem item = new PantryItem();
                    item.setItemId(cursor.getInt(0));
                    item.setName(cursor.getString(1));
                    item.setQuantity(cursor.getDouble(2));
                    item.setUnit(cursor.getString(3));

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
            //
        }
        return items;
    }

    public ArrayList<PantryItem> getAllItems(){
        return getAllItems("name","ASC");
    }

    public PantryItem getItem(int itemId){
        PantryItem item = new PantryItem();

        try{
            String query = "SELECT * FROM pantry_items WHERE _id="+itemId;
            Cursor cursor = database.rawQuery(query,null);

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
            //
        }
        return item;
    }
}
