package com.example.ecommerce4you.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce4you.Adapter.ItemListAdapter;
import com.example.ecommerce4you.Adapter.PopularAdapter;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.ViewModel.MainViewModel;
import com.example.ecommerce4you.databinding.ActivityItemListBinding;

public class ItemListActivity extends BaseAdminActivity {

    private ActivityItemListBinding binding;
    private MainViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityItemListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel =new MainViewModel();

        initItemList();
        setupBottomNavigation(R.id.nav_products, this);


    }

    private void initItemList() {
        // Affiche un loader pendant le chargement
        binding.progressBarItems.setVisibility(View.VISIBLE);

        // Observe les données populaires depuis le ViewModel
        viewModel.loadItems().observeForever(itemsModels -> {
            if (!itemsModels.isEmpty()) {
                // Affiche les produits populaires dans un RecyclerView vertical
                binding.itemsView.setLayoutManager(
                        new GridLayoutManager(this, 2)
                );
                binding.itemsView.setAdapter(new ItemListAdapter(itemsModels,
                        this));
                binding.itemsView.setNestedScrollingEnabled(true);
            }

            // Cache le loader une fois les données chargées
            binding.progressBarItems.setVisibility(View.GONE);
        });

        // Appelle la méthode de chargement (au cas où elle déclenche aussi un cache ou autre logique)
        viewModel.loadPopular();
    }
}