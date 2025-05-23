package com.example.ecommerce4you.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce4you.Domain.OrderModel;
import com.example.ecommerce4you.Adapter.OrderAdapter;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityAdminOrdersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminOrdersActivity extends BaseAdminActivity {

    private ActivityAdminOrdersBinding binding;
    private ArrayList<OrderModel> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupBottomAdminNavigation(R.id.nav_orders, this);

        setupRecyclerView();

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadOrders(); // Recharge les données à chaque fois que l'activité devient visible
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(orderList, order -> {
            Toast.makeText(this, "Order ID: " + order.getId(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminOrdersActivity.this, OrderDetailActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });

        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.ordersRecyclerView.setAdapter(orderAdapter);
    }

    private void loadOrders() {
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminOrdersActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}