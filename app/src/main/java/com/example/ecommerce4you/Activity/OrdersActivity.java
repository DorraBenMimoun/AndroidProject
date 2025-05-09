package com.example.ecommerce4you.Activity;

// Importation des classes nécessaires
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

// Cette activité affiche la liste des commandes dans l'interface admin
public class OrdersActivity extends BaseAdminActivity {
    // Liaison avec le layout XML via ViewBinding
    private ActivityOrdersBinding binding;

    // Déclaration des composants de la RecyclerView
    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private List<OrderModel> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Active l'affichage bord-à-bord (pour l'UI immersive moderne)
        EdgeToEdge.enable(this);

        // Initialisation du binding pour accéder facilement aux vues XML
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Petit toast de test (peut être supprimé ensuite)
        Toast.makeText(this, "Orders Activity!", Toast.LENGTH_SHORT).show();

        // Initialise la barre de navigation du bas pour les admins (défini dans BaseAdminActivity)
        setupBottomAdminNavigation(R.id.nav_orders, this);

        // Initialisation de la RecyclerView pour les commandes
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Affichage en liste verticale

        // Création d'une liste vide de commandes
        orderList = new ArrayList<>();

        // Initialisation de l'adaptateur avec la liste vide pour l'instant
        orderAdapter = new OrderAdapter(orderList, this);

        // On connecte la RecyclerView à l'adaptateur
        orderRecyclerView.setAdapter(orderAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadOrdersFromFirebase(); // Cela force le rechargement à chaque fois qu'on revient sur l'activité
    }


    // Cette méthode lit les commandes dans Firebase Realtime Database
    private void loadOrdersFromFirebase() {
        FirebaseDatabase.getInstance().getReference("Orders") // Chemin de la table "Orders"
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderList.clear(); // On vide la liste avant de la remplir
                        for (DataSnapshot orderSnap : snapshot.getChildren()) {
                            // Chaque enfant de "Orders" est une commande
                            OrderModel order = orderSnap.getValue(OrderModel.class); // On le transforme en objet Java
                            if (order != null) {
                                // Affichage dans les logs pour débogage
                                Log.d("order list", "order list : " + order);
                                orderList.add(order); // On ajoute la commande à la liste
                            }
                        }
                        // Une fois les données récupérées, on avertit l'adaptateur que la liste a changé
                        orderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // En cas d'erreur avec Firebase (ex : pas de connexion), gérer ici
                        Log.e("OrdersActivity", "Erreur Firebase : " + error.getMessage());
                    }
                });
    }
}
