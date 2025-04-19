package com.example.ecommerce4you.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.ecommerce4you.Adapter.CategoryAdapter;
import com.example.ecommerce4you.Adapter.PopularAdapter;
import com.example.ecommerce4you.Adapter.SliderAdpater;
import com.example.ecommerce4you.Domain.BannerModel;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.ViewModel.MainViewModel;
import com.example.ecommerce4you.databinding.ActivityMainBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Liaison avec la vue (utilise ViewBinding pour accéder aux éléments de layout)
    private ActivityMainBinding binding;

    // ViewModel utilisé pour récupérer les données (catégories, bannières, produits populaires)
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Active le mode "Edge-to-Edge" pour une meilleure gestion du plein écran
        EdgeToEdge.enable(this);

        // Initialise le ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialise le ViewModel
        viewModel = new MainViewModel();

        // Initialise les différentes sections de l'application
        initCategory();   // Chargement des catégories
        initSlider();     // Chargement du carrousel (slider)
        initPopular();    // Chargement des produits populaires
        bottomNavigation(); // Gestion de la navigation et du bouton panier
    }

    // Méthode pour gérer la barre de navigation inférieure et le bouton panier
    private void bottomNavigation() {
        // Sélectionne l'onglet "home" par défaut
        binding.bottomNavigation.setItemSelected(R.id.home, true);

        // Listener sur les onglets de la barre de navigation
        binding.bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                // Tu peux gérer la navigation vers d'autres activités ici
            }
        });

        // Redirection vers l'activité "CartActivity" quand on clique sur le bouton panier
        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
    }

    // Méthode pour initialiser les produits populaires
    private void initPopular() {
        // Affiche un loader pendant le chargement
        binding.progressBarPopular.setVisibility(View.VISIBLE);

        // Observe les données populaires depuis le ViewModel
        viewModel.loadPopular().observeForever(itemsModels -> {
            if (!itemsModels.isEmpty()) {
                // Affiche les produits populaires dans un RecyclerView horizontal
                binding.popularView.setLayoutManager(
                        new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false)
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

    // Méthode pour initialiser le slider (carrousel de bannières)
    private void initSlider() {
        binding.progressBarSlider.setVisibility(View.VISIBLE);

        // Observe les données de bannières depuis le ViewModel
        viewModel.loadBanner().observeForever(bannerModels -> {
            if (bannerModels != null && !bannerModels.isEmpty()) {
                // Si les bannières existent, on les passe au composant slider
                banners(bannerModels);
                binding.progressBarSlider.setVisibility(View.GONE);
            }
        });

        viewModel.loadBanner(); // même logique que plus haut
    }

    // Affiche les bannières dans un ViewPager2 avec effet de transformation
    private void banners(ArrayList<BannerModel> bannerModels) {
        // Adapter personnalisé pour afficher les bannières
        binding.viewPagerSlider.setAdapter(new SliderAdpater(bannerModels, binding.viewPagerSlider));

        // Configuration pour un slider plus esthétique
        binding.viewPagerSlider.setClipToPadding(false);
        binding.viewPagerSlider.setClipChildren(false);
        binding.viewPagerSlider.setOffscreenPageLimit(3);
        binding.viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        // Ajoute un effet de marge entre les pages (pour le style carrousel)
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPagerSlider.setPageTransformer(compositePageTransformer);
    }

    // Méthode pour initialiser les catégories
    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);

        // Observe les catégories depuis le ViewModel
        viewModel.loadCategory().observeForever(categoryModels -> {
            // Affiche les catégories dans un RecyclerView horizontal
            binding.categoryView.setLayoutManager(
                    new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
            binding.categoryView.setAdapter(new CategoryAdapter(categoryModels));
            binding.categoryView.setNestedScrollingEnabled(true);

            // Cache le loader
            binding.progressBarCategory.setVisibility(View.GONE);
        });
    }
}
