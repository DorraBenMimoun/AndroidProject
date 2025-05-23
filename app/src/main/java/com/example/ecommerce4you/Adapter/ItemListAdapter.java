package com.example.ecommerce4you.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.ecommerce4you.databinding.ViewholderPopularBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder>{
    private List<ItemsModel> itemList;
    Context context;

    public ItemListAdapter(List<ItemsModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;

    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderItemslistBinding binding = ViewholderItemslistBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
        holder.binding.priceTxt.setText("$" + itemList.get(position).getPrice());
        holder.binding.ratingTxt.setText("(" + itemList.get(position).getRating() + ")");
        holder.binding.offPercentTxt.setText(itemList.get(position).getOffPercent());
        holder.binding.oldPriceTxt.setText("$" + itemList.get(position).getOldPrice());
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        RequestOptions options = new RequestOptions();
        options = options.transform(new CenterInside());

        Glide.with(context)
                .load(itemList.get(position).getPicUrl().get(0))
                .apply(options)
                .into(holder.binding.pic);

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object",itemList.get(position));
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            String itemId = itemList.get(position).getItemId();
            if (itemId != null) {

                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Voulez-vous vraiment supprimer cet élément ?")
                        .setPositiveButton("Oui", (dialog, which) -> {
                            DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference("Items").child(itemId);
                            itemRef.removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Item supprimé avec succès", Toast.LENGTH_SHORT).show();
                                        itemList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, itemList.size());
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
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderItemslistBinding binding;
        ImageView deleteButton;

        public ViewHolder(ViewholderItemslistBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            this.deleteButton = binding.deleteItem;
        }
    }
}
