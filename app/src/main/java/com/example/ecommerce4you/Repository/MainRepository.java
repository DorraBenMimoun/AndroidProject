package com.example.ecommerce4you.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce4you.Domain.BannerModel;
import com.example.ecommerce4you.Domain.CategoryModel;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainRepository {
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private MutableLiveData<ArrayList<ItemsModel>> wishList = new MutableLiveData<>();

    public LiveData<ArrayList<CategoryModel>> locadCategory(){
        MutableLiveData<ArrayList<CategoryModel>> listData =new MutableLiveData<>();
        DatabaseReference ref= firebaseDatabase.getReference("Category");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CategoryModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    CategoryModel item = childSnapshot.getValue(CategoryModel.class);
                    if(item != null) list.add(item);
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listData;
    }

    public LiveData<ArrayList<BannerModel>> locadBanner(){
        MutableLiveData<ArrayList<BannerModel>> listData =new MutableLiveData<>();
        DatabaseReference ref= firebaseDatabase.getReference("Banner");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<BannerModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    BannerModel item = childSnapshot.getValue(BannerModel.class);
                    if(item != null) list.add(item);
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listData;
    }

    public LiveData<ArrayList<ItemsModel>> locadItems(){
        MutableLiveData<ArrayList<ItemsModel>> listData =new MutableLiveData<>();
        DatabaseReference ref= firebaseDatabase.getReference("Items");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ItemsModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    ItemsModel item = childSnapshot.getValue(ItemsModel.class);
                    if(item != null)
                    {
                        item.setItemId(childSnapshot.getKey()); // ✅ Très important !
                        list.add(item);

                    }
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listData;
    }
    public LiveData<ArrayList<ItemsModel>> loadWishlist() {
        MutableLiveData<ArrayList<ItemsModel>> wishlistLiveData = new MutableLiveData<>();
        ArrayList<ItemsModel> wishlistItems = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // récupère l'ID de l'utilisateur connecté
        DatabaseReference wishlistRef = firebaseDatabase.getReference("Wishlists").child(userId);
        DatabaseReference itemsRef = firebaseDatabase.getReference("Items");

        wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    wishlistLiveData.setValue(new ArrayList<>()); // pas de wishlist
                    return;
                }

                ArrayList<String> itemIds = new ArrayList<>();

                // Récupère tous les IDs dans la wishlist
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String itemId = childSnapshot.getKey();
                    itemIds.add(itemId);
                }

                if (itemIds.isEmpty()) {
                    wishlistLiveData.setValue(new ArrayList<>());
                    return;
                }

                // Maintenant on récupère chaque item par son ID dans "Items"
                itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot itemsSnapshot) {
                        for (String itemId : itemIds) {
                            if (itemsSnapshot.hasChild(itemId)) {
                                ItemsModel item = itemsSnapshot.child(itemId).getValue(ItemsModel.class);
                                if (item != null) {
                                    item.setItemId(itemId);
                                    wishlistItems.add(item);
                                }
                            }
                        }
                        wishlistLiveData.setValue(wishlistItems);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return wishlistLiveData;
    }



}
