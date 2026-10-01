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

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private ArrayList<Recipe> recipes;

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeNameTextView;
        private TextView recipeIngredientCountTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.textRecipeName);
            recipeIngredientCountTextView = itemView.findViewById(R.id.textRecipeIngredientCount);

            itemView.setOnClickListener(v->{
                int position = getAdapterPosition();

                if(position!=RecyclerView.NO_POSITION){
                    Recipe selectedRecipe = recipes.get(position);

                    Context context = itemView.getContext();

                }
            });
        }

        public TextView getRecipeNameTextView() {
            return recipeNameTextView;
        }

        public TextView getRecipeIngredientCountTextView() {
            return recipeIngredientCountTextView;
        }
    }

    public RecipeAdapter(ArrayList<Recipe> items){
        recipes = items;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recipe,parent,false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position){
        Recipe currentRecipe = recipes.get(position);

        holder.getRecipeNameTextView().setText(currentRecipe.getName());

        int ingredientCount = currentRecipe.getIngredients().size();
        String countText;
        if(ingredientCount == 1){
            countText = ingredientCount + " ingredient";
        }else{
            countText = ingredientCount + " ingredients";
        }
        holder.getRecipeIngredientCountTextView().setText(countText);
    }

    @Override
    public int getItemCount(){
        return recipes.size();
    }
}