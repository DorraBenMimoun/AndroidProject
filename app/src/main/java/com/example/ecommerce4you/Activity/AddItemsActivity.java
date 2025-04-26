package com.example.ecommerce4you.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ecommerce4you.Domain.CategoryModel;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityAddItemsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class AddItemsActivity extends BaseAdminActivity {

    private EditText titleEdit, descriptionEdit, priceEdit, oldPriceEdit, offPercentEdit, sizeEdit, colorEdit, picUrlEdit;
    private Button addItemBtn,backBtn;
    private Spinner spinnerCategory;
    ArrayList<String> categoryTitles;
    ArrayAdapter<String> adapter;
    private ActivityAddItemsBinding binding;

    private DatabaseReference itemsRef;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupBottomAdminNavigation(R.id.nav_dashboard, this);

        binding.backBtnAddItem.setOnClickListener(v->finish());

        // Initialisation des vues
        titleEdit = findViewById(R.id.titleEdit);
        descriptionEdit = findViewById(R.id.descriptionEdit);
        priceEdit = findViewById(R.id.priceEdit);
        oldPriceEdit = findViewById(R.id.oldPriceEdit);
        offPercentEdit = findViewById(R.id.offPercentEdit);
        sizeEdit = findViewById(R.id.sizeEdit);
        colorEdit = findViewById(R.id.colorEdit);
        picUrlEdit = findViewById(R.id.picUrlEdit);
        addItemBtn = findViewById(R.id.addItemBtn);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        categoryTitles = new ArrayList<>();
         adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        fetchCategoriesFromFirebase();

        // Firebase
        itemsRef = FirebaseDatabase.getInstance().getReference("Items");

        addItemBtn.setOnClickListener(v -> addItemToDatabase());
    }
    private void fetchCategoriesFromFirebase() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category");

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitles.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    CategoryModel category = snap.getValue(CategoryModel.class);
                    if (category != null) {
                        categoryTitles.add(category.getTitle());
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddItemsActivity.this, "Erreur : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addItemToDatabase() {
        try {
            String title = titleEdit.getText().toString();
            String description = descriptionEdit.getText().toString();
            double price = Double.parseDouble(priceEdit.getText().toString());
            double oldPrice = Double.parseDouble(oldPriceEdit.getText().toString());
            String offPercent = offPercentEdit.getText().toString();
            /*CategoryModel selectedCategory = (CategoryModel) spinnerCategory.getSelectedItem();
            String category = selectedCategory != null ? selectedCategory.getTitle() : "";
            */
            String category = spinnerCategory.getSelectedItem().toString();


            ArrayList<String> sizes = new ArrayList<>(Arrays.asList(sizeEdit.getText().toString().split(",")));
            ArrayList<String> colors = new ArrayList<>(Arrays.asList(colorEdit.getText().toString().split(",")));
            ArrayList<String> picUrls = new ArrayList<>(Arrays.asList(picUrlEdit.getText().toString().split(",")));

            ItemsModel newItem = new ItemsModel();
            newItem.setTitle(title);
            newItem.setDescription(description);
            newItem.setPrice(price);
            newItem.setOldPrice(oldPrice);
            newItem.setOffPercent(offPercent);
            newItem.setSize(sizes);
            newItem.setColor(colors);
            newItem.setPicUrl(picUrls);
            newItem.setReview(0);
            newItem.setRating(0);
            newItem.setNumberinCart(0);
            newItem.setCategory(category);

            String itemId = itemsRef.push().getKey();
            if (itemId != null) {
                newItem.setItemId(itemId);
                itemsRef.child(itemId).setValue(newItem)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddItemsActivity.this, AdminDashboardActivity.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());

            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }
}