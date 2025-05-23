package com.example.ecommerce4you.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.bumptech.glide.Glide;
import com.example.ecommerce4you.Adapter.CategoryAdapter;
import com.example.ecommerce4you.Adapter.PopularAdapter;
import com.example.ecommerce4you.Adapter.SliderAdpater;
import com.example.ecommerce4you.Domain.BannerModel;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.ViewModel.MainViewModel;
import com.example.ecommerce4you.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import android.view.inputmethod.EditorInfo;


public class MainActivity extends BaseActivity {

    // Liaison avec la vue
    private ActivityMainBinding binding;

    // ViewModel utilisé pour récupérer les données (catégories, bannières, produits populaires)
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Initialise le ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNavigation(R.id.home,binding.bottomNavigation,this);


        // recherche de produits
        binding.searchText.setOnEditorActionListener((v, actionId, event) -> {
            String text = binding.searchText.getText().toString().trim();
            if (!text.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("search_query", text);
                binding.searchText.setText("");
                startActivity(intent);
            }
            return true;
        });



        // Recuperer Utilisateur connecté
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name");

                            // Si pas de username on prend l'email
                            if (userName == null || userName.isEmpty()) {
                                userName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            }

                            String capitalizedName = userName.substring(0, 1).toUpperCase() + userName.substring(1).toLowerCase();

                            binding.textView5.setText(capitalizedName);

                            String imageUrl = "https://ui-avatars.com/api/?name=" + capitalizedName+ "&rounded=true&size=216" ;
                            Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.profile) // image temporaire pendant le chargement
                                    .into(binding.imageView2);

                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Erreur de lecture dans Firestore", Toast.LENGTH_SHORT).show();
                    });
        }
        // Initialise le ViewModel
        viewModel = new MainViewModel();
        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));

        // Initialise les différentes sections de l'application
        initCategory();   // Chargement des catégories
        initSlider();     // Chargement du carrousel (slider)
        initPopular();    // Chargement des produits populaires
        //bottomNavigation(); // Gestion de la navigation et du bouton panier

        binding.logout.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this,"Logged out successfully",Toast.LENGTH_SHORT).show();

            // Redirection vers LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Efface la backstack
            startActivity(intent);
            finish(); // Ferme MainActivity
        });

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
