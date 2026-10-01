package com.example.smartpantrymanager;

import com.example.smartpantrymanager.Models.PantryItem;
import com.example.smartpantrymanager.Models.Recipe;
import com.example.smartpantrymanager.Models.RecipeIngredient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecipeMatcher {

    // Returns only the recipes that can be made completely from the pantry.
    // Expired pantry items are never counted.
    public ArrayList<Recipe> getMatchingRecipes(List<Recipe> recipes, List<PantryItem> pantryItems){
        ArrayList<Recipe> matches = new ArrayList<>();

        for(Recipe recipe : recipes){
            if(canMakeRecipe(recipe, pantryItems)){
                matches.add(recipe);
            }
        }
        return matches;
    }

    public boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantryItems){
        List<RecipeIngredient> ingredients = recipe.getIngredients();

        if(ingredients.isEmpty()){
            return false;
        }

        for(RecipeIngredient ingredient : ingredients){
            if(!hasEnoughOf(ingredient, pantryItems)){
                return false;
            }
        }
        return true;
    }

    // Adds up everything in the pantry that is the same ingredient (and the same kind
    // of unit) and checks it is at least what the recipe needs
    private boolean hasEnoughOf(RecipeIngredient ingredient, List<PantryItem> pantryItems){
        String neededName = cleanName(ingredient.getIngredientName());
        String neededFamily = getUnitFamily(ingredient.getUnit());
        double neededAmount = toBaseAmount(ingredient.getQuantity(), ingredient.getUnit());

        double availableAmount = 0;

        for(PantryItem item : pantryItems){
            if(isExpired(item)){
                continue;
            }
            if(!cleanName(item.getName()).equals(neededName)){
                continue;
            }
            if(!getUnitFamily(item.getUnit()).equals(neededFamily)){
                continue;
            }
            availableAmount += toBaseAmount(item.getQuantity(), item.getUnit());
        }

        return availableAmount >= neededAmount - 0.0001;
    }

    // Lowercase, trim and remove a plural ending so "Eggs" matches "egg"
    // and "Tomatoes" matches "tomato"
    private String cleanName(String name){
        if(name == null){
            return "";
        }

        String cleaned = name.trim().toLowerCase();

        if(cleaned.endsWith("oes")){
            cleaned = cleaned.substring(0, cleaned.length() - 2);
        }else if(cleaned.endsWith("s") && !cleaned.endsWith("ss")){
            cleaned = cleaned.substring(0, cleaned.length() - 1);
        }
        return cleaned;
    }

    // g and kg are both "mass", ml, l, tsp, tbsp and cup are all "volume".
    // Anything else (like pcs) only matches the same unit.
    private String getUnitFamily(String unit){
        if(unit == null){
            return "";
        }

        String u = unit.toLowerCase();

        if(u.equals("g") || u.equals("kg")){
            return "mass";
        }
        if(u.equals("ml") || u.equals("l") || u.equals("tsp")
                || u.equals("tbsp") || u.equals("cup")){
            return "volume";
        }
        return u;
    }

    // Converts to grams (for mass) or millilitres (for volume) so amounts can be compared
    private double toBaseAmount(double quantity, String unit){
        if(unit == null){
            return quantity;
        }

        String u = unit.toLowerCase();

        if(u.equals("kg")){
            return quantity * 1000;
        }
        if(u.equals("l")){
            return quantity * 1000;
        }
        if(u.equals("tsp")){
            return quantity * 5;
        }
        if(u.equals("tbsp")){
            return quantity * 15;
        }
        if(u.equals("cup")){
            return quantity * 250;
        }
        return quantity;
    }

    // An item is expired if its expiry date is before today (expires today is still fine)
    private boolean isExpired(PantryItem item){
        if(item.getExpiryDate() == null){
            return false;
        }

        Calendar today = Calendar.getInstance();
        clearTime(today);

        Calendar expiry = (Calendar) item.getExpiryDate().clone();
        clearTime(expiry);

        return expiry.before(today);
    }

    private void clearTime(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}