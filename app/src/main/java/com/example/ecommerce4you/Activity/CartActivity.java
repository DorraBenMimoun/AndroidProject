package com.example.ecommerce4you.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce4you.Adapter.CartAdapter;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.Domain.OrderModel;
import com.example.ecommerce4you.Helper.ChangeNumberItemsListener;
import com.example.ecommerce4you.Helper.ManagmentCart;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityCartBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;
    private CartAdapter adapter;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        // Récupération du userId depuis les SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = prefs.getString("userId", null);

        if (userId == null) {
            Log.e("CartActivity", "UserId is null !");

            // Si l'utilisateur n'est pas connecté, on ferme l'activité
            finish();
            return;
        }
        initCartList();
        calculateCart();
        setListeners();
    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);

            binding.cartView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CartAdapter(managmentCart.getListCart(), this, new ChangeNumberItemsListener() {
                @Override
                public void changed() {
                    calculateCart();
                }
            }, null); // pas besoin de userId ici car on ne travaille pas avec Firebase
            binding.cartView.setAdapter(adapter);
        }
    }

    private void calculateCart() {
        double total = managmentCart.getTotalFee();
        double delivery = 10; // tu peux changer si tu veux

        double totalWithDelivery = total + delivery;

        binding.totalFeeTxt.setText("$" + String.format("%.2f", total));
        binding.deliveryTxt.setText("$" + String.format("%.2f", delivery));
        binding.totalTxt.setText("$" + String.format("%.2f", totalWithDelivery));
    }

    private void setListeners() {
        binding.backBtn.setOnClickListener(v -> finish());
       binding.orderNowBtn.setOnClickListener(v -> showOrderBottomSheet());

        /*binding.orderNowBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
             managmentCart.clearCart();
        });*/
    }

    private void showOrderBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.order_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);

        EditText etAddress = view.findViewById(R.id.etAddress);
        EditText etPhone = view.findViewById(R.id.etPhone);
        TextView tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        Button btnConfirmOrder = view.findViewById(R.id.btnConfirmOrder);

        tvTotalPrice.setText("Total: $" + String.format("%.2f", managmentCart.getTotalFee()));

        btnConfirmOrder.setOnClickListener(v1 -> {
            String address = etAddress.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            placeOrder(address, phone);

            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void placeOrder(String address, String phone) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ordersRef = database.getReference("Orders");
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userId = auth.getCurrentUser().getUid();
        String orderId = ordersRef.push().getKey(); // Crée un ID unique pour la commande

        if (orderId == null) {
            Toast.makeText(this, "Failed to generate order ID", Toast.LENGTH_SHORT).show();
            return;
        }

        double subtotal = managmentCart.getTotalFee();
        double tax = 0; // pour l'instant, fixe à 0, à toi d'ajouter un calcul si besoin
        double deliveryFee = 10; // frais de livraison
        double total = subtotal + tax + deliveryFee;
        long timestamp = System.currentTimeMillis();

        // Prendre la liste actuelle des articles du panier
        ArrayList<ItemsModel> itemsList = new ArrayList<>(managmentCart.getListCart());

        // Créer l'objet OrderModel
        OrderModel order = new OrderModel(
                orderId,
                userId,
                itemsList,
                subtotal,
                tax,
                deliveryFee,
                total,
                timestamp,
                address,
                phone
        );

        // Enregistrer dans Firebase
        ordersRef.child(orderId).setValue(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    managmentCart.clearCart();
                    initCartList(); // pour rafraîchir la vue du panier
                    startActivity(new Intent(CartActivity.this, MainActivity.class))   ;             })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}