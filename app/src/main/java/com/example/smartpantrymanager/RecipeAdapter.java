package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.Models.Recipe;

import java.util.ArrayList;

// adapter for the suggested recipes list
// takes the list of matching recipes and turn each one into a row
// the recycle shows this on screen
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    // recipes that are shown in the list
    private ArrayList<Recipe> recipes;

    // view holder holds the views of one row
    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeNameTextView;
        private TextView recipeIngredientCountTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            // link variables to the views for item recipe xml
            recipeNameTextView = itemView.findViewById(R.id.textRecipeName);
            recipeIngredientCountTextView = itemView.findViewById(R.id.textRecipeIngredientCount);

            // when the row is tapped, open the recipes detail screen for that recipe
            itemView.setOnClickListener(v->{
                int position = getAdapterPosition();

                // no  position means the row no longer in the list
                if(position!=RecyclerView.NO_POSITION){
                    Recipe selectedRecipe = recipes.get(position);

                    Context context = itemView.getContext();

                    // send the recipe id with the intent so the detail screen knows what to show
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra("recipeId",selectedRecipe.getRecipeId());
                    context.startActivity(intent);

                }
            });
        }

        // getters so our adapter can reach the views of this row
        public TextView getRecipeNameTextView() {
            return recipeNameTextView;
        }

        public TextView getRecipeIngredientCountTextView() {
            return recipeIngredientCountTextView;
        }
    }

    // constructor
    public RecipeAdapter(ArrayList<Recipe> items){
        recipes = items;
    }


// called when recycle view needs a new row
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recipe,parent,false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);
        return recipeViewHolder;
    }

    // called to fill a row with data of the recipe of the recipe at this position
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position){
        Recipe currentRecipe = recipes.get(position);

        // show the recipes name
        holder.getRecipeNameTextView().setText(currentRecipe.getName());

        // get the number of ingredients the recipe needs
        int ingredientCount = currentRecipe.getIngredients().size();
        String countText;
        // if it needs one ingredient then use ingredient or else use ingredients
        if(ingredientCount == 1){
            countText = ingredientCount + " ingredient";
        }else{
            countText = ingredientCount + " ingredients";
        }
        holder.getRecipeIngredientCountTextView().setText(countText);
    }

    // tells the recycle view  how many rows there are
    @Override
    public int getItemCount(){
        return recipes.size();
    }
}