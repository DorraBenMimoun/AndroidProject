package com.example.ecommerce4you.Helper;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WishlistManager {

    private static final String TAG = "WishlistManager";
    private static final String NODE_NAME = "Wishlists";

    private DatabaseReference dbRef;
    private String userId;

    public WishlistManager() {
        dbRef = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void addToWishlist(String productId) {
        dbRef.child(NODE_NAME)
                .child(userId)
                .child(productId)
                .setValue(true)  // juste "true"
                .addOnSuccessListener(unused -> Log.d(TAG, "Produit ajouté à la wishlist"))
                .addOnFailureListener(e -> Log.e(TAG, "Erreur ajout wishlist", e));
    }

    public void removeFromWishlist(String productId) {
        dbRef.child(NODE_NAME)
                .child(userId)
                .child(productId)
                .removeValue()
                .addOnSuccessListener(unused -> Log.d(TAG, "Produit retiré de la wishlist"))
                .addOnFailureListener(e -> Log.e(TAG, "Erreur retrait wishlist", e));
    }
}
