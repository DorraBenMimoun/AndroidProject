package com.example.ecommerce4you.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce4you.Adapter.CartAdapter;
import com.example.ecommerce4you.Domain.CartModel;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.Helper.ChangeNumberItemsListener;
import com.example.ecommerce4you.Helper.ManagmentCart;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityCartBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;
    private String userId;
    private ArrayList<CartModel> cartItems = new ArrayList<>();


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
            // Si l'utilisateur n'est pas connecté, on ferme l'activité
            finish();
            return;
        }
        Log.d("AuthState", "Current user: " + (userId != null ? userId: "No user logged in"));

        setVariable();
        initCartList();

    }

    private void initCartList() {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // On vide la liste avant de la remplir avec les nouveaux items
                cartItems.clear();

                // On récupère tous les items du panier de l'utilisateur
                for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                    CartModel item = cartItemSnapshot.getValue(CartModel.class);
                    if (item != null) {
                        cartItems.add(item);
                    }
                }

                // Si le panier est vide
                if (cartItems.isEmpty()) {
                    binding.emptyTxt.setVisibility(View.VISIBLE);
                    binding.scrollView3.setVisibility(View.GONE);
                } else {
                    binding.emptyTxt.setVisibility(View.GONE);
                    binding.scrollView3.setVisibility(View.VISIBLE);
                    // Mise à jour de l'adapter avec les articles récupérés
                    binding.cartView.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                    binding.cartView.setAdapter(new CartAdapter(cartItems, CartActivity.this, CartActivity.this::calculatorCart,userId));
                }

                // Calcul des totaux
                calculatorCart();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("CART_ERROR", "Erreur de récupération des articles du panier: " + error.getMessage());
            }
        });
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v->finish());
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        double totalFee = 0;

        // Calcul du total des articles dans le panier
        for (CartModel item : cartItems) {
            Log.d("cart item calculater","price : "+item.getPrice());
            Log.d("cart item calculater","quantity : "+item.getQuantity());

            totalFee += item.getPrice() * item.getQuantity(); // Total des articles
        }
        Log.d("cart item calculater","total fee : "+totalFee);

        // Calcul de la taxe
        tax = Math.round((totalFee * percentTax * 100.0)) / 100.0;
        Log.d("cart item calculater","total tax : "+tax);


        // Calcul du total final
        double total = Math.round((totalFee + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round((totalFee * 100.0)) / 100.0;

        // Mise à jour des TextViews
        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }

}