package com.example.ecommerce4you.Activity;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce4you.Adapter.OrderDetailAdapter;
import com.example.ecommerce4you.Domain.OrderModel;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityOrderDetailBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private OrderModel orderModel;
    private OrderDetailAdapter orderDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Récupérer l'order envoyé
        orderModel = (OrderModel) getIntent().getSerializableExtra("order");

        if (orderModel != null) {
            bindOrderDetails();
            setupRecyclerView();
            setupConfirmButton();
        }

        binding.backBtn.setOnClickListener(v -> finish());

    }

    @SuppressLint("ResourceAsColor")
    private void bindOrderDetails() {
        binding.orderIdText.setText("Order ID: #" + orderModel.getOrderId());
        binding.userEmailText.setText("User Email: " + orderModel.getUserEmail());
        binding.totalAmountText.setText("Total: $" + String.format("%.2f", orderModel.getTotal()));
        binding.taxAmountText.setText("Tax: $" + String.format("%.2f", orderModel.getTax())); // Nouveau Tax
        binding.tvaAmountText.setText("TVA: " + String.format("%.2f", orderModel.getTva())+" %"); // Nouveau Tax

        binding.deliveryAmountText.setText("Delivery: $" + String.format("%.2f", orderModel.getDeliveryFee())); // Nouveau Tax

        binding.addressText.setText("Address: " + orderModel.getAdress());
        binding.phoneText.setText("Phone: " + orderModel.getPhone());
        binding.orderDate.setText("Order Date: " + getFormattedDate(orderModel.getTimestamp()));
        if(orderModel.isConfirmed()) {
            binding.orderConfirmed.setText("Confirmed: Yes");
            binding.btnConfirmOrderAdmin.setVisibility(GONE);
        }
        else {
            binding.orderConfirmed.setText("Confirmed: No");

        }



    }

    private void setupRecyclerView() {
        orderDetailAdapter = new OrderDetailAdapter(orderModel.getItems());
        binding.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.itemsRecyclerView.setAdapter(orderDetailAdapter);
    }

    private void setupConfirmButton() {
        binding.btnConfirmOrderAdmin.setOnClickListener(v -> {
            confirmOrder(orderModel.getOrderId());
        });
    }

    private void confirmOrder(String orderId) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("confirmed", true);

        orderRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order confirmed successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to confirm order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String getFormattedDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
