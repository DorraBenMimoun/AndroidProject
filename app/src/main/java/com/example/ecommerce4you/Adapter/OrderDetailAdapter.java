package com.example.ecommerce4you.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.databinding.ViewholderOrderDetailsBinding;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private List<ItemsModel> itemsList;

    public OrderDetailAdapter(List<ItemsModel> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderOrderDetailsBinding binding = ViewholderOrderDetailsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemsModel item = itemsList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ViewholderOrderDetailsBinding binding;

        public ViewHolder(ViewholderOrderDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemsModel item) {
            binding.itemTitle.setText(item.getTitle());
            binding.itemPrice.setText("Price: $" + item.getPrice());
            binding.itemQuantity.setText("Quantity: " + item.getNumberinCart());
            double totalPrice = item.getPrice() * item.getNumberinCart();
            binding.itemTotalPrice.setText("Total: $" + String.format("%.2f", totalPrice));

            Glide.with(binding.getRoot().getContext())
                    .load(item.getPicUrl().get(0))
                    .into(binding.itemImage);
        }
    }
}
