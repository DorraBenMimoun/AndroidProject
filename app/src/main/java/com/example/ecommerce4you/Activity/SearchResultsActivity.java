package com.example.ecommerce4you.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.ecommerce4you.Adapter.PopularAdapter;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.Repository.MainRepository;
import com.example.ecommerce4you.databinding.ActivitySearchResultsBinding;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    private ActivitySearchResultsBinding binding;
    private MainRepository repository = new MainRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Récupérer la requête initiale
        String initialQuery = getIntent().getStringExtra("search_query");
        if (initialQuery != null) {
            binding.searchText.setText(initialQuery);
            searchForItems(initialQuery);
        }

        // Gérer une nouvelle recherche quand on appuie sur Entrée
        binding.searchText.setOnEditorActionListener((v, actionId, event) -> {
                String query = binding.searchText.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchForItems(query);
                } else {
                    Toast.makeText(this, "Tape quelque chose !", Toast.LENGTH_SHORT).show();
                }
                return true;
        });

        binding.backBtn.setOnClickListener(v -> {
            finish();
        });

    }

    private void searchForItems(String query) {
        repository.locadItems().observe(this, items -> {
            ArrayList<ItemsModel> filteredList = new ArrayList<>();
            for (ItemsModel item : items) {
                if (item.getTitle() != null &&
                        item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }

            binding.searchResultsRecycler.setLayoutManager(new GridLayoutManager(this, 2));
            binding.searchResultsRecycler.setAdapter(new PopularAdapter(filteredList));
        });
    }


}
