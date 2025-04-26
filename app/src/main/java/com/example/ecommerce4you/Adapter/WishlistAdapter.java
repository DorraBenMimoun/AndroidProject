package com.example.ecommerce4you.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerce4you.Activity.DetailActivity;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.databinding.ViewholderItemslistBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<ItemsModel> wishlistItems;
    private Context context;

    public WishlistAdapter(List<ItemsModel> wishlistItems, Context context) {
        this.wishlistItems = wishlistItems;
        this.context = context;
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderItemslistBinding binding = ViewholderItemslistBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        holder.binding.priceTxt.setText("$" + wishlistItems.get(position).getPrice());
        holder.binding.ratingTxt.setText("(" + wishlistItems.get(position).getRating() + ")");
        holder.binding.offPercentTxt.setText(wishlistItems.get(position).getOffPercent());
        holder.binding.oldPriceTxt.setText("$" + wishlistItems.get(position).getOldPrice());
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        RequestOptions options = new RequestOptions();
        options = options.transform(new CenterInside());

        Glide.with(context)
                .load(wishlistItems.get(position).getPicUrl().get(0))
                .apply(options)
                .into(holder.binding.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", wishlistItems.get(position));
            context.startActivity(intent);
        });

        // 🔥 Gestion suppression depuis la Wishlist
        holder.deleteButton.setOnClickListener(v -> {
            String itemId = wishlistItems.get(position).getItemId();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (itemId != null) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Voulez-vous vraiment supprimer cet article de votre wishlist ?")
                        .setPositiveButton("Oui", (dialog, which) -> {
                            DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("Wishlists").child(userId).child(itemId);
                            wishlistRef.removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Article retiré de la wishlist", Toast.LENGTH_SHORT).show();
                                        wishlistItems.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, wishlistItems.size());
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderItemslistBinding binding;
        ImageView deleteButton;

        public ViewHolder(ViewholderItemslistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.deleteButton = binding.deleteItem;
        }
    }
}
