package com.example.ecommerce4you.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityOrdersBinding;

public class OrdersActivity extends BaseAdminActivity {
    private ActivityOrdersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toast.makeText(this, "Orders Activity!", Toast.LENGTH_SHORT).show();


        setupBottomNavigation(R.id.nav_orders, this);

    }
}