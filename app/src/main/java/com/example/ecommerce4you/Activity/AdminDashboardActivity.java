package com.example.ecommerce4you.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce4you.Adapter.CategoryAdminAdapter;
import com.example.ecommerce4you.Adapter.PopularAdapter;
import com.example.ecommerce4you.Domain.CategoryModel;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.ViewModel.MainViewModel;
import com.example.ecommerce4you.databinding.ActivityAdminDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends BaseAdminActivity {

    private MainViewModel viewModel;
    private ActivityAdminDashboardBinding binding;
    private EditText newCategoryEditText;
    private Button addCategoryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.viewOrdersBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminOrdersActivity.class);
            startActivity(intent);
        });


        Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show();

        // Bouton pour ajouter un item
        binding.addItemBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddItemsActivity.class);
            startActivity(intent);
        });
        viewModel = new MainViewModel();
        initPopular();
        initCategory();

        newCategoryEditText = findViewById(R.id.newCategoryEditText);
        addCategoryButton = findViewById(R.id.addCategoryButton);

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category");

        addCategoryButton.setOnClickListener(v -> {
            String newCategory = newCategoryEditText.getText().toString().trim();
            if (!newCategory.isEmpty()) {
                String categoryId = categoryRef.push().getKey();
                CategoryModel categoryModel = new CategoryModel(newCategory);
                categoryRef.child(categoryId).setValue(categoryModel)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show();
                            newCategoryEditText.setText("");
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(this, "Please enter a category title", Toast.LENGTH_SHORT).show();
            }
        });



        setupBottomAdminNavigation(R.id.nav_dashboard, this);
        binding.logout.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(AdminDashboardActivity.this,"Logged out successfully",Toast.LENGTH_SHORT).show();

            // Redirection vers LoginActivity
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Efface la backstack
            startActivity(intent);
            finish(); // Ferme MainActivity
        });

    }


    private void initCategory(){
        binding.progressBarCategory.setVisibility(View.VISIBLE);

        viewModel.loadCategory().observeForever(categoryModels -> {
            binding.categoriesRecyclerView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.categoriesRecyclerView.setAdapter(
                    new CategoryAdminAdapter((ArrayList<CategoryModel>) categoryModels));
            binding.categoriesRecyclerView.setNestedScrollingEnabled(true);
            binding.progressBarCategory.setVisibility(View.GONE);
        });
    }


    private void initPopular() {
        // Affiche un loader pendant le chargement
        binding.progressBarPopular.setVisibility(View.VISIBLE);

        // Observe les données populaires depuis le ViewModel
        viewModel.loadPopular().observeForever(itemsModels -> {
            if (!itemsModels.isEmpty()) {
                // Affiche les produits populaires dans un RecyclerView horizontal
                binding.popularView.setLayoutManager(
                        new LinearLayoutManager(AdminDashboardActivity.this, LinearLayoutManager.HORIZONTAL, false)
                );
                binding.popularView.setAdapter(new PopularAdapter(itemsModels));
                binding.popularView.setNestedScrollingEnabled(true);
            }

            // Cache le loader une fois les données chargées
            binding.progressBarPopular.setVisibility(View.GONE);
        });

        // Appelle la méthode de chargement (au cas où elle déclenche aussi un cache ou autre logique)
        viewModel.loadPopular();
    }


}