package com.example.ecommerce4you.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce4you.Activity.OrderDetailActivity;
import com.example.ecommerce4you.Domain.OrderModel;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ViewholderItemOrderBinding;
import com.example.ecommerce4you.databinding.ViewholderItemslistBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        holder.binding.userEmail.setText("User: " + order.getUserEmail());
        holder.binding.totalAmountText.setText("Total: $" + order.getTotal());
        holder.binding.adressText.setText("Address: " + order.getAdress());
        holder.binding.phoneText.setText("Phone: " + order.getPhone());
        if ((order.isConfirmed()))
        {
            holder.binding.confirmed.setText("Confirmed: Yes");
        }
        else
        {
            holder.binding.confirmed.setText("Confirmed: No");
        }

        // Quand l'admin clique sur un order
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), OrderDetailActivity.class);
            intent.putExtra("order", order); // OrderModel doit être Serializable
            holder.itemView.getContext().startActivity(intent);
        });


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
