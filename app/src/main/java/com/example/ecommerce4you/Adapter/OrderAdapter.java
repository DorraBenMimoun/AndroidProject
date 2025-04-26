package com.example.ecommerce4you.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce4you.Domain.OrderModel;
import com.example.ecommerce4you.databinding.ViewholderItemOrderBinding;
import com.example.ecommerce4you.databinding.ViewholderItemslistBinding;

import java.util.List;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderModel> orderList;
    Context context;

    public OrderAdapter(List<OrderModel> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;

    }
    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderItemOrderBinding binding = ViewholderItemOrderBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);   }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        holder.binding.orderIdText.setText("Order ID: #" + order.getOrderId());
        holder.binding.userIdText.setText("User: " + order.getUserId());
        holder.binding.totalAmountText.setText("Total: $" + order.getTotal());
        holder.binding.adressText.setText("Address: " + order.getAdress());
        holder.binding.phoneText.setText("Phone: " + order.getPhone());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderItemOrderBinding binding;
        public ViewHolder(ViewholderItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
    }
}
