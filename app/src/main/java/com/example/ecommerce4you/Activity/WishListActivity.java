package com.example.ecommerce4you.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.ecommerce4you.Adapter.ItemListAdapter;
import com.example.ecommerce4you.Adapter.WishlistAdapter;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.ViewModel.MainViewModel;
import com.example.ecommerce4you.databinding.ActivityItemListBinding;
import com.example.ecommerce4you.databinding.ActivityWishListBinding;

public class WishListActivity extends BaseActivity {

    private ActivityWishListBinding binding;
    private MainViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityWishListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel =new MainViewModel();

        initWishList();
        setupBottomNavigation(R.id.favorites,binding.bottomNavigation, this);

    }

    private void initWishList() {
        // Affiche un loader pendant le chargement
        binding.progressBarItems.setVisibility(View.VISIBLE);

        // Observe les données populaires depuis le ViewModel
        viewModel.loadWishlist().observeForever(itemsModels -> {
            if (!itemsModels.isEmpty()) {
                // Affiche les produits populaires dans un RecyclerView vertical
                binding.itemsView.setLayoutManager(
                        new GridLayoutManager(this, 2)
                );
                binding.itemsView.setAdapter(new WishlistAdapter(itemsModels,
                        this));
                binding.itemsView.setNestedScrollingEnabled(true);
            }

            // Cache le loader une fois les données chargées
            binding.progressBarItems.setVisibility(View.GONE);
        });

    }
}