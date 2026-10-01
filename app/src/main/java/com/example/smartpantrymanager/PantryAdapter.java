package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.Models.PantryItem;

import java.util.ArrayList;
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {
    private ArrayList<PantryItem> pantryItems;

    public interface ItemDeleteListener{
        void onItemDeleted();
    }

    private ItemDeleteListener deleteListener;

    public class PantryViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameTextView;
        private TextView itemQuantityTextView;
        private TextView itemExpiryTextView;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.textItemName);
            itemQuantityTextView = itemView.findViewById(R.id.textItemQuantity);
            itemExpiryTextView = itemView.findViewById(R.id.textItemExpiry);

            itemView.setOnClickListener(v->{
                int position = getAdapterPosition();

                if(position!=RecyclerView.NO_POSITION){
                    PantryItem selectedItem = pantryItems.get(position);

                    Context context = itemView.getContext();

                    Intent intent = new Intent(context, AddEditActivity.class);
                    intent.putExtra("itemId",selectedItem.getItemId());

                    context.startActivity(intent);
                }
            });

            ImageButton btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
            btnDeleteItem.setOnClickListener(v->{
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION){
                    PantryItem selectedItem = pantryItems.get(position);
                    confirmDelete(itemView.getContext(),selectedItem);
                }
            });

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

        public PantryAdapter(ArrayList<PantryItem> items, ItemDeleteListener listener){
            pantryItems = items;
            deleteListener = listener;
        }

    private void confirmDelete(Context context, PantryItem item){
        new AlertDialog.Builder(context)
                .setTitle("Delete ingredient")
                .setMessage("Are you sure you want to delete " + item.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> deleteItemFromDatabase(context, item))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteItemFromDatabase(Context context, PantryItem item){
        PantryDataSource ds = new PantryDataSource(context);
        boolean didSucceed;

        try{
            ds.open();
            didSucceed = ds.deleteItem(item.getItemId());
            ds.close();
        }catch(Exception e){
            didSucceed = false;
        }

        if(didSucceed){
            Toast.makeText(context,"Ingredient deleted",Toast.LENGTH_SHORT).show();
            deleteListener.onItemDeleted();
        }else{
            Toast.makeText(context,"Could not delete ingredient. Please try again",
                    Toast.LENGTH_LONG).show();
        }
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

