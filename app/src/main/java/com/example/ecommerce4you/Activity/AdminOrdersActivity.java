package com.example.ecommerce4you.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce4you.Domain.OrderModel;
import com.example.ecommerce4you.Adapter.OrderAdapter;
import com.example.ecommerce4you.databinding.ActivityAdminOrdersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminOrdersActivity extends AppCompatActivity {

    private ActivityAdminOrdersBinding binding;
    private ArrayList<OrderModel> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecyclerView();
        loadOrders();
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(orderList, order -> {
            // Show toast when order is clicked
            Toast.makeText(this, "Order ID: " + order.getId(), Toast.LENGTH_SHORT).show();
            // Later you can replace this with navigation to order details
            // Intent intent = new Intent(this, OrderDetailsActivity.class);
            // intent.putExtra("ORDER_ID", order.getId());
            // startActivity(intent);
        });

        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.ordersRecyclerView.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("Orders")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            OrderModel order = data.getValue(OrderModel.class);
                            if (order != null) {
                                orderList.add(order);
                            }
                        }
                        orderAdapter.notifyDataSetChanged();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminOrdersActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }
}