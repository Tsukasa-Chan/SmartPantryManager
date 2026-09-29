package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.Models.PantryItem;

import java.util.ArrayList;
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {
    private ArrayList<PantryItem> pantryItems;

    public class PantryViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameTextView;
        private TextView itemQuantityTextView;
        private TextView itemExpiryTextView;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.textItemName);
            itemQuantityTextView = itemView.findViewById(R.id.textItemQuantity);
            itemExpiryTextView = itemView.findViewById(R.id.textItemExpiry);
        }

        public TextView getItemNameTextView() {
            return itemNameTextView;
        }

        public TextView getItemQuantityTextView() {
            return itemQuantityTextView;
        }

        public TextView getItemExpiryTextView() {
            return itemExpiryTextView;
        }

    }

        public PantryAdapter(ArrayList<PantryItem> items){
            pantryItems = items;
        }

        @NonNull
        @Override
        public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_pantry,parent,false);
            PantryViewHolder pantryViewHolder = new PantryViewHolder(view);
            return pantryViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PantryViewHolder holder, int position){
            PantryItem currentItem = pantryItems.get(position);

            holder.getItemNameTextView().setText(currentItem.getName());

            String quantityText = currentItem.getQuantity()+" " + currentItem.getUnit();
            holder.getItemQuantityTextView().setText(quantityText);

            if(currentItem.getExpiryDate()!=null){
                String expiryText = "Expires " + DateFormat.format("dd MMM",currentItem.getExpiryDate());
                holder.getItemExpiryTextView().setText(expiryText);
            }else{
                holder.getItemExpiryTextView().setText("");
            }
        }

        @Override
        public int getItemCount(){
            return pantryItems.size();
        }
    }

