package com.example.ecommerce4you.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.Helper.ChangeNumberItemsListener;
import com.example.ecommerce4you.Helper.ManagmentCart;
import com.example.ecommerce4you.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<ItemsModel> listItemSelected;
    ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCart managmentCart;

    public CartAdapter(ArrayList<ItemsModel> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.changeNumberItemsListener = changeNumberItemsListener;
        managmentCart = new ManagmentCart(context);
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        holder.binding.titleTxt.setText(listItemSelected.get(position).getTitle());
        holder.binding.feeEachItem.setText("$"+ listItemSelected.get(position).getPrice());
        holder.binding.totalEachItem.setText("$"+Math.round((listItemSelected.get(position).getNumberinCart()*listItemSelected.get(position).getPrice())));
        holder.binding.numberItemTxt.setText(String.valueOf(listItemSelected.get(position).getNumberinCart()));

        Glide.with(holder.itemView.getContext())
                .load(listItemSelected.get(position).getPicUrl().get(0))
                .into(holder.binding.pic);

        holder.binding.plusCartBtn.setOnClickListener(v->{
            managmentCart.plusItem(listItemSelected,position,()->{
                notifyDataSetChanged();
                changeNumberItemsListener.changed();
            });
        });
        holder.binding.minusICartBtn.setOnClickListener(v->{
            managmentCart.minusItem(listItemSelected,position,()->{
                notifyDataSetChanged();
                changeNumberItemsListener.changed();
            });
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
