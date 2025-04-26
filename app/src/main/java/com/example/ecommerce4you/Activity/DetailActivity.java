package com.example.ecommerce4you.Activity;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.ecommerce4you.Adapter.ColorAdapter;
import com.example.ecommerce4you.Adapter.PicListAdapter;
import com.example.ecommerce4you.Adapter.SizeAdapter;
import com.example.ecommerce4you.Domain.CartModel;
import com.example.ecommerce4you.Domain.ItemsModel;
import com.example.ecommerce4you.Helper.ManagmentCart;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private ItemsModel object;
    private int numberOrder=1;
    private ManagmentCart managmentCart;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding =ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        getBundles();
        initPicList();
        initSize();
        initColor();

    }

    private void initColor() {
        binding.recylclerColor.setAdapter(new ColorAdapter(object.getColor()));
        binding.recylclerColor.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
    }

    private void initSize() {
        binding.recylerSize.setAdapter(new SizeAdapter(object.getSize()));
        binding.recylerSize.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
    }

    private void initPicList() {
        ArrayList<String>picList = new ArrayList<>(object.getPicUrl());

        Glide.with(this)
                .load(picList.get(0))
                .into(binding.pic);

        binding.picList.setAdapter(new PicListAdapter(picList,binding.pic));
        binding.picList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void getBundles() {
        object= (ItemsModel) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$"+object.getPrice());
        binding.oldPriceTxt.setText("$"+object.getOldPrice());
        binding.oldPriceTxt.setPaintFlags(binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.descriptionTxt.setText(object.getDescription());

        binding.addToCartBtn.setOnClickListener(v->
        {
            // Récupération de l’ID utilisateur
           /* SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            userId = prefs.getString("userId", null);
            if (userId == null) {
                Toast.makeText(this, "Veuillez vous connecter", Toast.LENGTH_SHORT).show();
                return;
            }

            // Construction de l’objet CartModel
            CartModel cartItem = new CartModel();
            cartItem.setUserId(userId);
            cartItem.setItemId(object.getItemId());
            cartItem.setQuantity(numberOrder);

            */
            //addToCart(cartItem);



            object.setNumberinCart(numberOrder);
            managmentCart.insertItem(object);
        });

        binding.backBtn.setOnClickListener(v->finish());
    }

   /* private void addToCart(CartModel cartItem) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // utilisateur connecté
        String cartItemId = cartRef.child(userId).push().getKey(); // identifiant unique pour l'item

        if (cartItemId != null) {
            cartRef.child(userId).child(cartItemId).setValue(cartItem)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(DetailActivity.this, "Produit ajouté au panier", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(DetailActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        }
    }*/


}