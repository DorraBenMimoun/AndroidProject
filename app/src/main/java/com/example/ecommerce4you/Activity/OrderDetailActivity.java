package com.example.ecommerce4you.Activity;

import static android.view.View.GONE; // Import statique pour rendre invisible un élément

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce4you.Adapter.OrderDetailAdapter;
import com.example.ecommerce4you.Domain.OrderModel;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityOrderDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding; // Liaison avec le layout XML via ViewBinding
    private OrderModel orderModel; // Objet représentant la commande
    private OrderDetailAdapter orderDetailAdapter; // Adaptateur pour afficher les détails de la commande

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation du ViewBinding
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Récupération de l'objet OrderModel envoyé par l'intent (depuis OrdersActivity)
        orderModel = (OrderModel) getIntent().getSerializableExtra("order");

        // Si l'objet n'est pas nul, on affiche ses détails et on configure la RecyclerView et le bouton
        if (orderModel != null) {
            bindOrderDetails();      // Remplir l'interface avec les détails de la commande
            setupRecyclerView();     // Afficher les produits de la commande
            setupConfirmButton();    // Configurer le bouton pour confirmer la commande
        }

        // Action du bouton retour
        binding.backBtn.setOnClickListener(v -> finish());
    }

    // Affiche les informations principales de la commande dans l'interface
    @SuppressLint("ResourceAsColor")
    private void bindOrderDetails() {
        binding.orderIdText.setText("Order ID: #" + orderModel.getOrderId());
        binding.userEmailText.setText("User Email: " + orderModel.getUserEmail());
        binding.totalAmountText.setText("Total: $" + String.format("%.2f", orderModel.getTotal()));
        binding.taxAmountText.setText("Tax: $" + String.format("%.2f", orderModel.getTax()));
        binding.tvaAmountText.setText("TVA: " + String.format("%.2f", orderModel.getTva()) + " %");
        binding.deliveryAmountText.setText("Delivery: $" + String.format("%.2f", orderModel.getDeliveryFee()));

     binding.addressText.setText("Address: " + orderModel.getAdress());
            binding.phoneText.setText("Phone: " + orderModel.getPhone());



        // Affichage de la date formatée à partir du timestamp
        binding.orderDate.setText("Order Date: " + getFormattedDate(orderModel.getTimestamp()));

        // Vérifie si la commande est confirmée ou non
        if (orderModel.isConfirmed()) {
            binding.orderConfirmed.setText("Confirmed: Yes");
            binding.btnConfirmOrderAdmin.setVisibility(GONE); // Cache le bouton si déjà confirmé
        } else {
            binding.orderConfirmed.setText("Confirmed: No");
        }
    }

    // Initialise la RecyclerView pour afficher les produits de la commande
    private void setupRecyclerView() {
        orderDetailAdapter = new OrderDetailAdapter(orderModel.getItems()); // Adaptateur avec la liste d’articles
        binding.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Layout vertical
        binding.itemsRecyclerView.setAdapter(orderDetailAdapter); // Liaison de l’adaptateur à la RecyclerView
    }

    // Configure l'action du bouton pour confirmer la commande
    private void setupConfirmButton() {
        binding.btnConfirmOrderAdmin.setOnClickListener(v -> {
            confirmOrder(orderModel.getOrderId()); // Appel de la méthode de confirmation
        });
    }

    // Met à jour la commande dans Firebase pour la marquer comme confirmée
    private void confirmOrder(String orderId) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("confirmed", true); // Met à jour le champ "confirmed" à true

        orderRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order confirmed successfully!", Toast.LENGTH_SHORT).show();
                    // Mettre à jour l'affichage
                    binding.orderConfirmed.setText("Confirmed: Yes");
                    binding.btnConfirmOrderAdmin.setVisibility(GONE);                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to confirm order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Méthode utilitaire pour convertir un timestamp en date lisible (ex. 09/05/2025)
    private String getFormattedDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
