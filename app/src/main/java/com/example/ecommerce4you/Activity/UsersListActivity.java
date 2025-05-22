package com.example.ecommerce4you.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce4you.Adapter.UserAdapter;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityUsersListBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class UsersListActivity extends BaseAdminActivity {

    private ActivityUsersListBinding binding;
    private ArrayList<QueryDocumentSnapshot> usersList = new ArrayList<>();
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomAdminNavigation(com.example.ecommerce4you.R.id.nav_users, this);

        binding.usersRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(usersList);
        binding.usersRecycler.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        FirebaseFirestore.getInstance().collection("Users")
                .get()
                .addOnSuccessListener(query -> {
                    usersList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        usersList.add(doc);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erreur Firestore : " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
