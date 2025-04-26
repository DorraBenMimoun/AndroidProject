package com.example.ecommerce4you.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce4you.Adapter.OrderAdapter;
import com.example.ecommerce4you.Domain.OrderModel;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityOrdersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends BaseAdminActivity {
    private ActivityOrdersBinding binding;
    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private List<OrderModel> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toast.makeText(this, "Orders Activity!", Toast.LENGTH_SHORT).show();


        setupBottomAdminNavigation(R.id.nav_orders, this);

        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList,this);
        orderRecyclerView.setAdapter(orderAdapter);

        loadOrdersFromFirebase();
    }

    private void loadOrdersFromFirebase() {
        FirebaseDatabase.getInstance().getReference("Orders")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderList.clear();
                        for (DataSnapshot orderSnap : snapshot.getChildren()) {
                            OrderModel order = orderSnap.getValue(OrderModel.class);
                            if (order != null) {
                                orderList.add(order);
                            }
                        }
                        orderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Gérer l'erreur ici
                    }
                });
    }
}