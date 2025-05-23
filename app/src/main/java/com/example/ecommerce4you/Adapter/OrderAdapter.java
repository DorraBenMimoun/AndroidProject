package com.example.ecommerce4you.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce4you.Domain.OrderModel;
import com.example.ecommerce4you.R;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<OrderModel> orderList;
    private OnOrderClickListener onOrderClickListener;

    public interface OnOrderClickListener {
        void onOrderClick(OrderModel order);
    }

    public OrderAdapter(ArrayList<OrderModel> orderList, OnOrderClickListener listener) {
        this.orderList = orderList;
        this.onOrderClickListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        // Format timestamp to readable date
        String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                .format(new Date(order.getTimestamp()));

        holder.orderId.setText("Commande #" + order.getTimestamp());
        holder.orderTotal.setText(String.format("Total: %.2f TND", order.getTotal()));
        holder.orderDate.setText(formattedDate);
        holder.orderItems.setText(order.getNumberItems() + " articles");
        holder.orderStatus.setText(order.isConfirmed() ? "Confirmée" : "En attente");
        holder.orderAddress.setText(order.getAdress());

        // Set click listener
        holder.itemView.setOnClickListener(v -> onOrderClickListener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderTotal, orderDate, orderItems, orderStatus, orderAddress;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderTotal = itemView.findViewById(R.id.order_total);
            orderDate = itemView.findViewById(R.id.order_date);
            orderItems = itemView.findViewById(R.id.order_items);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderAddress = itemView.findViewById(R.id.order_address);
        }
    }
}