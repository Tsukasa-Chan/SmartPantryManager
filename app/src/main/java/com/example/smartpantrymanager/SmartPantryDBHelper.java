package com.example.smartpantrymanager;

import android.content.ContentValues;
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
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS pantry_items");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        onCreate(db);
    }

    private void insertRecipe(SQLiteDatabase db, String name, String steps, Object[][] ingredients){
        ContentValues recipeValues = new ContentValues();
        recipeValues.put("name",name);
        recipeValues.put("steps",steps);
        long recipeId = db.insert("recipes",null, recipeValues);

        for(Object[] ingredient:ingredients){
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put("recipe_id",recipeId);
            ingredientValues.put("ingredient_name",(String)ingredient[0]);
            ingredientValues.put("quantity",(Double) ingredient[1]);
            ingredientValues.put("unit",(String) ingredient[2]);
            db.insert("recipe_ingredients",null,ingredientValues);
        }
    }

    private void seedRecipes(SQLiteDatabase db){

        insertRecipe(db, "Tomato and Egg Stir Fry",
                "Wash and chop the tomatoes. Beat the eggs in a bowl. " +
                        "Heat a little oil in a pan and fry the tomatoes for 2 minutes. " +
                        "Pour in the eggs and stir until fully cooked.",
                new Object[][]{
                        {"tomato", 2.0, "pcs"},
                        {"egg", 3.0, "pcs"},
                        {"oil", 1.0, "tbsp"}
                });

        insertRecipe(db, "Egg Fried Rice",
                "Cook the rice until soft. Beat the eggs in a bowl. " +
                        "Heat a little oil in a pan and scramble the eggs. " +
                        "Add the cooked rice and soy sauce. " +
                        "Stir everything together and cook for another 2 minutes.",
                new Object[][]{
                        {"rice", 1.0, "cup"},
                        {"egg", 2.0, "pcs"},
                        {"soy sauce", 1.0, "tbsp"},
                        {"oil", 1.0, "tbsp"}
                });

        insertRecipe(db, "Cheese Omelette",
                "Beat the eggs and milk together in a bowl. " +
                        "Pour the mixture into a lightly oiled, heated pan. " +
                        "Cook until the eggs begin to set. " +
                        "Sprinkle the cheese over the eggs, fold the omelette in half, " +
                        "and cook until the cheese melts.",
                new Object[][]{
                        {"egg", 3.0, "pcs"},
                        {"cheese", 50.0, "g"},
                        {"milk", 30.0, "ml"},
                        {"oil", 1.0, "tbsp"}
                });

        insertRecipe(db, "Garlic Butter Rice",
                "Cook the rice until soft. Melt the butter in a pan over medium heat. " +
                        "Chop the garlic and add it to the melted butter. " +
                        "Fry until fragrant, then add the cooked rice. " +
                        "Stir until the rice is evenly coated and heated through.",
                new Object[][]{
                        {"rice", 1.0, "cup"},
                        {"butter", 1.0, "tbsp"},
                        {"garlic", 2.0, "pcs"}
                });

        insertRecipe(db, "Simple Tomato Pasta",
                "Boil the pasta according to the packet instructions until tender. " +
                        "Drain the pasta and set it aside. " +
                        "Heat the oil in a pan and fry the chopped garlic briefly. " +
                        "Add the chopped tomatoes and cook until they soften into a sauce. " +
                        "Add the pasta and stir until evenly coated.",
                new Object[][]{
                        {"pasta", 200.0, "g"},
                        {"tomato", 3.0, "pcs"},
                        {"garlic", 1.0, "pcs"},
                        {"oil", 1.0, "tbsp"}
                });

        insertRecipe(db, "Cheesy Pasta",
                "Boil the pasta according to the packet instructions until tender. " +
                        "Drain the pasta and return it to the pot. " +
                        "Add the butter and grated cheese. " +
                        "Stir until the butter and cheese have melted and coated the pasta.",
                new Object[][]{
                        {"pasta", 200.0, "g"},
                        {"cheese", 80.0, "g"},
                        {"butter", 1.0, "tbsp"}
                });

        insertRecipe(db, "Chicken and Rice",
                "Cook the rice until soft. Cut the chicken into small pieces " +
                        "and season with salt. Heat a little oil in a pan and fry the chicken, " +
                        "stirring occasionally, until fully cooked. " +
                        "Check that the centre of the chicken reaches 74 degrees Celsius. " +
                        "Serve the cooked chicken over the rice.",
                new Object[][]{
                        {"chicken", 200.0, "g"},
                        {"rice", 1.0, "cup"},
                        {"salt", 1.0, "tsp"},
                        {"oil", 1.0, "tbsp"}
                });

        insertRecipe(db, "Onion Soup",
                "Peel and slice the onions. Melt the butter in a pot over medium heat. " +
                        "Add the onions and cook until soft. " +
                        "Dissolve the stock cube in water and add it to the pot. " +
                        "Bring the soup to a boil, then reduce the heat and simmer for 15 minutes. " +
                        "Serve while hot.",
                new Object[][]{
                        {"onion", 3.0, "pcs"},
                        {"butter", 1.0, "tbsp"},
                        {"stock cube", 1.0, "pcs"}
                });

        insertRecipe(db, "Potato Salad",
                "Wash and peel the potatoes, then cut them into smaller pieces. " +
                        "Boil the potatoes until soft, then drain and allow them to cool. " +
                        "Chop the onion into small pieces. " +
                        "Place the potatoes in a bowl and mix with the mayonnaise and onion. " +
                        "Stir gently and serve.",
                new Object[][]{
                        {"potato", 4.0, "pcs"},
                        {"mayonnaise", 2.0, "tbsp"},
                        {"onion", 1.0, "pcs"}
                });

        insertRecipe(db, "Mashed Potatoes",
                "Wash, peel and chop the potatoes into smaller pieces. " +
                        "Boil the potatoes until soft, then drain the water. " +
                        "Mash the potatoes using a fork or potato masher. " +
                        "Add the butter and milk, then mix until smooth and creamy. " +
                        "Serve while warm.",
                new Object[][]{
                        {"potato", 4.0, "pcs"},
                        {"butter", 1.0, "tbsp"},
                        {"milk", 50.0, "ml"}
                });

        insertRecipe(db, "Banana Pancakes",
                "Peel the banana and mash it in a bowl. " +
                        "Add the egg and flour, then whisk until a smooth batter forms. " +
                        "Heat a lightly oiled pan over medium heat. " +
                        "Pour small spoonfuls of batter into the pan. " +
                        "Cook until bubbles appear, then flip and cook the other side until golden.",
                new Object[][]{
                        {"banana", 1.0, "pcs"},
                        {"egg", 1.0, "pcs"},
                        {"flour", 0.5, "cup"},
                        {"oil", 1.0, "tbsp"}
                });

        insertRecipe(db, "Rice Pudding",
                "Place the rice and milk in a pot over low heat. " +
                        "Add the sugar and stir until dissolved. " +
                        "Simmer gently, stirring often to prevent the rice from sticking. " +
                        "Continue cooking until the rice is soft and the mixture becomes thick and creamy. " +
                        "Allow to cool slightly before serving.",
                new Object[][]{
                        {"rice", 0.5, "cup"},
                        {"milk", 2.0, "cup"},
                        {"sugar", 2.0, "tbsp"}
                });

        insertRecipe(db, "Carrot Soup",
                "Peel and chop the carrots and onion. " +
                        "Heat a little oil in a pot and fry the onion until soft. " +
                        "Add the carrots, stock cube and enough water to cover the vegetables. " +
                        "Bring to a boil, then simmer until the carrots are soft. " +
                        "Blend the mixture until smooth and serve.",
                new Object[][]{
                        {"carrot", 4.0, "pcs"},
                        {"onion", 1.0, "pcs"},
                        {"stock cube", 1.0, "pcs"},
                        {"oil", 1.0, "tbsp"}
                });

        insertRecipe(db, "Grilled Cheese Sandwich",
                "Spread the butter over one side of each slice of bread. " +
                        "Place the cheese between the slices with the buttered sides facing outward. " +
                        "Heat a pan over medium heat and place the sandwich in the pan. " +
                        "Cook until the bread is golden brown, then flip it over. " +
                        "Cook the other side until golden and the cheese has melted.",
                new Object[][]{
                        {"bread", 2.0, "pcs"},
                        {"cheese", 50.0, "g"},
                        {"butter", 1.0, "tbsp"}
                });

        insertRecipe(db, "Egg Sandwich",
                "Place the eggs in a pot of water and boil until hard-boiled. " +
                        "Allow the eggs to cool, then peel and mash them in a bowl. " +
                        "Add the mayonnaise and mix until combined. " +
                        "Spread the egg mixture over one slice of bread. " +
                        "Place the second slice on top and serve.",
                new Object[][]{
                        {"egg", 2.0, "pcs"},
                        {"bread", 2.0, "pcs"},
                        {"mayonnaise", 1.0, "tbsp"}
                });

        insertRecipe(db, "Vegetable Stir Fry",
                "Wash and chop the carrots, onion and garlic. " +
                        "Heat a little oil in a pan over medium heat. " +
                        "Add the garlic and fry briefly until fragrant. " +
                        "Add the carrots and onion, then stir fry until just tender. " +
                        "Add the soy sauce and stir everything together before serving.",
                new Object[][]{
                        {"carrot", 2.0, "pcs"},
                        {"onion", 1.0, "pcs"},
                        {"garlic", 1.0, "pcs"},
                        {"soy sauce", 1.0, "tbsp"},
                        {"oil", 1.0, "tbsp"}
                });
    }

}
