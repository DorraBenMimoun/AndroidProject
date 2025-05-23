package com.example.ecommerce4you.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerce4you.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmail, loginPassword;
    TextView signupRedirectText;
    Button loginButton;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_btn);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(v -> {
            if (validateEmail() & validatePassword()) {
                loginUser();
            }
        });

        signupRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }

    public Boolean validateEmail() {
        String val = loginEmail.getText().toString();
        if (val.isEmpty()) {
            loginEmail.setError("Email cannot be empty");
            return false;
        } else {
            loginEmail.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    private void loginUser() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();

                        FirebaseFirestore.getInstance()
                                .collection("Users")
                                .document(userId)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        String role = documentSnapshot.getString("role");
                                        String username = documentSnapshot.getString("username");

                                        // zeyed pour linstant vu que firebase auth soccupe de recuperer luser connecté
//                                        getSharedPreferences("Ecommerce4YouApp", MODE_PRIVATE)
//                                                .edit()
//                                                .putString("userId", userId)
//                                                .apply();

                                        // Redirection selon le rôle
                                        if ("admin".equals(role)) {
                                            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                                        } else {
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        }
                                        finish();
                                    } else {
                                        loginEmail.setError("Utilisateur non trouvé dans Firestore.");
                                        loginEmail.requestFocus();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FIRESTORE", "Erreur Firestore: " + e.getMessage());
                                    loginEmail.setError("Erreur Firestore");
                                    loginEmail.requestFocus();
                                });

                    } else {
                        loginPassword.setError("Email ou mot de passe invalide");
                        loginPassword.requestFocus();
                    }
                });
    }

}
