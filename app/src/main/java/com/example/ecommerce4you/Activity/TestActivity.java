package com.example.ecommerce4you.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tu peux afficher un écran vide ou un simple layout
        setContentView(android.R.layout.simple_list_item_1);

        migrateUsers();
    }

    private void migrateUsers() {
        DatabaseReference realtimeRef = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        realtimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    Object userData = userSnapshot.getValue();

                    firestore.collection("Users").document(userId).set(userData)
                            .addOnSuccessListener(aVoid ->
                                    Log.d("MIGRATION", "User " + userId + " migré avec succès"))
                            .addOnFailureListener(e ->
                                    Log.e("MIGRATION", "Erreur pour " + userId, e));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MIGRATION", "Erreur de lecture : " + error.getMessage());
            }
        });
    }
}
