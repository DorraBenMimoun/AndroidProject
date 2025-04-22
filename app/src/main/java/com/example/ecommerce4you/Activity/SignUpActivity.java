package com.example.ecommerce4you.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerce4you.Domain.UserModel;
import com.example.ecommerce4you.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText signupName, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_btn);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        signupButton.setOnClickListener(v -> {
            String name = signupName.getText().toString().trim();
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Étape 1 : Créer l'utilisateur avec Auth
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = auth.getCurrentUser().getUid();
                            String role = "user"; // rôle par défaut

                            // Étape 2 : Enregistrer les autres infos dans la DB
                            UserModel userModel = new UserModel(userId, name, email, role);

                            reference.child(userId).setValue(userModel)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(this, "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(this, "Erreur lors de l'enregistrement : " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(this, "Erreur Auth : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        loginRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });
    }
}
