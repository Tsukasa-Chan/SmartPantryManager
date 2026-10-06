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

// adapter for the pantry which takes the list of pantry items and turns each one into a row
// the recycler view can show on the screen
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {
    // the pantry items that are shown in the list
    private ArrayList<PantryItem> pantryItems;

    // interface so the adapter can tell the screen the item was deleted
    // mainActivity implements this and reloads the list when it is called
    public interface ItemDeleteListener{
        void onItemDeleted();
    }

    // the screen that listens for deletes
    private ItemDeleteListener deleteListener;

    // The view holder holds the vuews of one row, so they only have to be looked up once
    // and can be reused as the user scrolls
    public class PantryViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameTextView;
        private TextView itemQuantityTextView;
        private TextView itemExpiryTextView;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            // links the variables to the views in our item pantry xml
            itemNameTextView = itemView.findViewById(R.id.textItemName);
            itemQuantityTextView = itemView.findViewById(R.id.textItemQuantity);
            itemExpiryTextView = itemView.findViewById(R.id.textItemExpiry);

            // when the row is tapped, it will open the add/edit screen for that item
            itemView.setOnClickListener(v->{
                // finds which row was tapped
                int position = getAdapterPosition();

                // no position means the row is no longer in our list, so it does nothing
                if(position!=RecyclerView.NO_POSITION){
                    PantryItem selectedItem = pantryItems.get(position);

                    Context context = itemView.getContext();

                    // send the items id with the intent so the screen knows which item to load
                    Intent intent = new Intent(context, AddEditActivity.class);
                    intent.putExtra("itemId",selectedItem.getItemId());

                    context.startActivity(intent);
                }
            });

            // when the bin icon is tapped, ask the user to confirm before deleting
            ImageButton btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
            btnDeleteItem.setOnClickListener(v->{
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION){
                    PantryItem selectedItem = pantryItems.get(position);
                    confirmDelete(itemView.getContext(),selectedItem);
                }
            });

        }

        // getters so the adapter can reach the views of this row
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
        // constructor that receives the list of items to show
        // and the screen to tell about deletes
        public PantryAdapter(ArrayList<PantryItem> items, ItemDeleteListener listener){
            pantryItems = items;
            deleteListener = listener;
        }

    // show a pop up to confirm the user wants to delete the item
    private void confirmDelete(Context context, PantryItem item){
        new AlertDialog.Builder(context)
                .setTitle("Delete ingredient")
                .setMessage("Are you sure you want to delete " + item.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> deleteItemFromDatabase(context, item))
                .setNegativeButton("Cancel", null)
                .show();
    }

    // delete the item from the database
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
            // tell the user the item was deleted
            // tell the screen to reload the new list without the item
            Toast.makeText(context,"Ingredient deleted",Toast.LENGTH_SHORT).show();
            deleteListener.onItemDeleted();
        }else{
            // not deleted, tell the user it was not deleted
            Toast.makeText(context,"Could not delete ingredient. Please try again",
                    Toast.LENGTH_LONG).show();
        }
    }

    // called when the recycler view needs a new row
    // creates the row from item pantry xml and wraps it in a view holder
        @NonNull
        @Override
        public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_pantry,parent,false);
            PantryViewHolder pantryViewHolder = new PantryViewHolder(view);
            return pantryViewHolder;
        }

        // called to fill a row with the data of the item at this position
        @Override
        public void onBindViewHolder(@NonNull PantryViewHolder holder, int position){
            PantryItem currentItem = pantryItems.get(position);

            // show the name
            holder.getItemNameTextView().setText(currentItem.getName());

            // show the quantity and unit together
            String quantityText = currentItem.getQuantity()+" " + currentItem.getUnit();
            holder.getItemQuantityTextView().setText(quantityText);

            // if the item has an expiry date, show it or else leave it empty
            if(currentItem.getExpiryDate()!=null){
                String expiryText = "Expires " + DateFormat.format("dd MMM",currentItem.getExpiryDate());
                holder.getItemExpiryTextView().setText(expiryText);
            }else{
                holder.getItemExpiryTextView().setText("");
            }
        }

        // tels the recycle view how many rows there are
        @Override
        public int getItemCount(){
            return pantryItems.size();
        }
    }

