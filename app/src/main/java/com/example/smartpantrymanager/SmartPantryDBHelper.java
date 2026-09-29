package com.example.smartpantrymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SmartPantryDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_PANTRY_ITEMS =
            "CREATE TABLE pantry_items("+
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "name TEXT NOT NULL,"+
                    "quantity REAL NOT NULL,"+
                    "unit TEXT NOT NULL,"+
                    "expiry_date TEXT)";

    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE recipes("+
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "name TEXT NOT NULL,"+
                    "steps TEXT NOT NULL)";

    private static final String CREATE_TABLE_RECIPE_INGREDIENTS =
            "CREATE TABLE recipe_ingredients("+
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "recipe_id INTEGER NOT NULL,"+
                    "ingredient_name TEXT NOT NULL,"+
                    "quantity REAL NOT NULL,"+
                    "unit TEXT NOT NULL)";

    public SmartPantryDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_PANTRY_ITEMS);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS pantry_items");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        onCreate(db);
    }
}
