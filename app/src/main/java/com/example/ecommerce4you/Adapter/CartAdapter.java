package com.example.ecommerce4you.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce4you.Domain.CartModel;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.Helper.ChangeNumberItemsListener;
import com.example.ecommerce4you.databinding.ViewholderCartBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<CartModel> listItemSelected;
    ChangeNumberItemsListener changeNumberItemsListener;
    private String userId;
    public CartAdapter(ArrayList<CartModel> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener,String userId) {
        this.listItemSelected = listItemSelected;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.userId = userId;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartModel currentCartItem = listItemSelected.get(position);
        // Récupérer les détails de l'élément en utilisant itemId
        String itemId = currentCartItem.getItemId();
        if (itemId == null) {
            Log.e("CartAdapter", "Item ID is null at position " + position);
            return;
        }
        Log.d("CART_ITEM", "Item ID utilisé : " + currentCartItem.getItemId());

        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items").child(currentCartItem.getItemId());
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ItemsModel item = snapshot.getValue(ItemsModel.class);

                Log.d("CART_ITEM", "Item loaded: " + snapshot.getValue(ItemsModel.class));

                if (item != null) {
                    // Mettre à jour l'affichage avec les détails de l'item
                    holder.binding.titleTxt.setText(item.getTitle());
                    holder.binding.feeEachItem.setText("$" + item.getPrice());
                    holder.binding.totalEachItem.setText("$" + Math.round(currentCartItem.getQuantity() * item.getPrice()));
                    holder.binding.numberItemTxt.setText(String.valueOf(currentCartItem.getQuantity()));

                    // Charger l'image
                    Glide.with(holder.itemView.getContext())
                            .load(item.getPicUrl().get(0))
                            .into(holder.binding.pic);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("CART_ERROR", "Erreur de récupération des détails de l'article: " + error.getMessage());
            }
        });
        // Gérer l'augmentation du nombre d'articles
        holder.binding.plusCartBtn.setOnClickListener(v -> {
            Log.d("plus cart btn","marche");
            increaseItemQuantity(position);
        });

        // Gérer la diminution du nombre d'articles
        holder.binding.minusICartBtn.setOnClickListener(v -> {
            decreaseItemQuantity(position);
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    // Méthode pour augmenter la quantité d'un article
// Méthode pour augmenter la quantité d'un article
    private void increaseItemQuantity(int position) {

        CartModel cartItem = listItemSelected.get(position); // Récupérer l'élément du panier
        String itemId = cartItem.getItemId();

        if (itemId == null || userId == null) {
            Log.e("CART_ERROR", "itemId ou userId est null !");
            return;
        }
        // Récupérer l'itemId de l'élément et récupérer les informations détaillées dans Firebase

        int newQuantity = cartItem.getQuantity() + 1;
        cartItem.setQuantity(newQuantity); // Met à jour localement

        // Met à jour directement la base sans avoir besoin de lire avant
        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("carts")
                .child(userId)
                .child(itemId);

        cartRef.child("quantity").setValue(newQuantity)
                .addOnSuccessListener(aVoid -> {
                    Log.d("CART_UPDATE", "Quantité augmentée à " + newQuantity);
                    notifyItemChanged(position);
                    changeNumberItemsListener.changed();
                })
                .addOnFailureListener(e -> {
                    Log.e("CART_UPDATE", "Échec mise à jour quantité: " + e.getMessage());
                });
    }

    // Méthode pour diminuer la quantité d'un article
    private void decreaseItemQuantity(int position) {
        CartModel cartItem = listItemSelected.get(position);
        String itemId = cartItem.getItemId();

        if (itemId == null || userId == null) {
            Log.e("CART_ERROR", "itemId ou userId est null !");
            return;
        }

        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("carts")
                .child(userId)
                .child(itemId);

        if (cartItem.getQuantity() > 1) {
            int newQuantity = cartItem.getQuantity() - 1;
            cartItem.setQuantity(newQuantity);

            cartRef.child("quantity").setValue(newQuantity)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("CART_UPDATE", "Quantité diminuée à " + newQuantity);
                        notifyItemChanged(position);
                        changeNumberItemsListener.changed();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CART_UPDATE", "Échec diminution quantité: " + e.getMessage());
                    });
        } else {
            cartRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        listItemSelected.remove(position);
                        notifyItemRemoved(position);
                        changeNumberItemsListener.changed();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CART_UPDATE", "Erreur suppression: " + e.getMessage());
                    });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public ViewHolder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
