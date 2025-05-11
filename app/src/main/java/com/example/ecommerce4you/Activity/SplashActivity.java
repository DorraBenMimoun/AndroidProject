package com.example.ecommerce4you.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce4you.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String role = documentSnapshot.getString("role");

                            if ("admin".equals(role)) {
                                startActivity(new Intent(SplashActivity.this, AdminDashboardActivity.class));
                            } else {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            }
                            finish();
                        } else {
                            // Utilisateur sans document Firestore, redirection par défaut
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // En cas d'erreur Firestore
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    });
        }


        binding.startBtn.setOnClickListener(v->
                startActivity(new Intent(SplashActivity.this,SignUpActivity.class)));
        binding.signinBtn.setOnClickListener(v->
                startActivity(new Intent(SplashActivity.this,LoginActivity.class)));

    }
}