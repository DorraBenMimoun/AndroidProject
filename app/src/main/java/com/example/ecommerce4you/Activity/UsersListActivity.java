package com.example.ecommerce4you.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityItemListBinding;
import com.example.ecommerce4you.databinding.ActivityUsersListBinding;

public class UsersListActivity extends BaseAdminActivity {
    private ActivityUsersListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUsersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toast.makeText(this, "Users List Activity", Toast.LENGTH_SHORT).show();


        setupBottomNavigation(R.id.nav_users, this);

    }
}