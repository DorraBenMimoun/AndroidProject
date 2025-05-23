package com.example.ecommerce4you.Activity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;
    private FirebaseFirestore db;
    private String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupBottomNavigation(R.id.profile, binding.bottomNavigation, this);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();



        // Charger les données utilisateur
        db.collection("Users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        binding.editTextName.setText(documentSnapshot.getString("name"));
                        binding.editTextEmail.setText(documentSnapshot.getString("email"));
                        binding.editTextAddress.setText(documentSnapshot.getString("address"));
                        // Gestion du téléphone
                        String phone = documentSnapshot.getString("phone");
                        binding.editTextPhone.setText(phone != null ? phone : "");

                        // Gestion du countryCode
                        String countryCode = documentSnapshot.getString("countryCode");

                        if (countryCode != null && !countryCode.isEmpty()) {
                            binding.countryCodePicker.setCountryForNameCode(countryCode );
                        }

                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erreur de récupération des données", Toast.LENGTH_SHORT).show()
                );

        binding.buttonSave.setOnClickListener(v -> {
            String name = binding.editTextName.getText().toString().trim();
            String email = binding.editTextEmail.getText().toString().trim();
            String address = binding.editTextAddress.getText().toString().trim();
            String phone = binding.editTextPhone.getText().toString().trim();
            String countryCode = binding.countryCodePicker.getSelectedCountryNameCode();
            String countryNumber = binding.countryCodePicker.getSelectedCountryCodeWithPlus();

            if (name.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier si l'email est déjà utilisé
            String finalPhone = phone;
            db.collection("Users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        boolean emailTaken = false;
                        for (var doc : queryDocumentSnapshots) {
                            if (!doc.getId().equals(uid)) {
                                emailTaken = true;
                                break;
                            }
                        }

                        if (emailTaken) {
                            Toast.makeText(this, "Cet email est déjà utilisé", Toast.LENGTH_SHORT).show();
                        } else {
                            // Mettre à jour les données
                            db.collection("Users").document(uid)
                                    .update(
                                            "name", name,
                                            "email", email,
                                            "address", address,
                                            "phone", finalPhone,
                                            "countryCode", countryCode,
                                            "countryNumber", countryNumber
                                    )
                                    .addOnSuccessListener(aVoid -> {
                                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email);
                                        Toast.makeText(this, "Profil mis à jour", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Erreur de mise à jour", Toast.LENGTH_SHORT).show()
                                    );
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Erreur lors de la vérification de l'email", Toast.LENGTH_SHORT).show()
                    );
        });
    }
}
