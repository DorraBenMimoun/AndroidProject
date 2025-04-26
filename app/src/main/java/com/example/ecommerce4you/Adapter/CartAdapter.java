package com.example.ecommerce4you.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.Helper.ChangeNumberItemsListener;
import com.example.ecommerce4you.Helper.ManagmentCart;
import com.example.ecommerce4you.databinding.ViewholderCartBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<ItemsModel> listItemSelected;
    ChangeNumberItemsListener changeNumberItemsListener;
    private Context context;
    private ManagmentCart managmentCart;

    public CartAdapter(ArrayList<ItemsModel> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener, String userId) {
        this.listItemSelected = listItemSelected;
        this.context = context;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.managmentCart = new ManagmentCart(context);
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        ItemsModel currentCartItem = listItemSelected.get(position);

        holder.binding.titleTxt.setText(currentCartItem.getTitle());
        holder.binding.feeEachItem.setText("$" + currentCartItem.getPrice());
        holder.binding.totalEachItem.setText("$" + Math.round(currentCartItem.getNumberinCart() * currentCartItem.getPrice()));
        holder.binding.numberItemTxt.setText(String.valueOf(currentCartItem.getNumberinCart()));

        Glide.with(holder.itemView.getContext())
                .load(currentCartItem.getPicUrl().get(0))
                .into(holder.binding.pic);

        holder.binding.plusCartBtn.setOnClickListener(v -> {
            managmentCart.plusItem(listItemSelected, holder.getAdapterPosition(), changeNumberItemsListener);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.binding.minusICartBtn.setOnClickListener(v -> {
            managmentCart.minusItem(listItemSelected, holder.getAdapterPosition(), changeNumberItemsListener);
            notifyItemChanged(holder.getAdapterPosition());
        });
    }
    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public ViewHolder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
