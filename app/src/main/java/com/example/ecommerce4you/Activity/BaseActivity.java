package com.example.ecommerce4you.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce4you.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public abstract class BaseActivity extends AppCompatActivity {
    protected ChipNavigationBar bottomNavigation;

    protected void setupBottomNavigation(int selectedItemId, ChipNavigationBar bottomNavigation, Context context) {
        this.bottomNavigation = bottomNavigation;
        Log.d("base","selected id"+selectedItemId);
        Log.d("bottom navigation","bottom"+bottomNavigation);

        bottomNavigation.setItemSelected(selectedItemId, true);

        bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {

                Intent intent = null;
                if (id == R.id.home) {
                    Log.d("base activity","home");
                    intent = new Intent(context, MainActivity.class);
                } else if (id == R.id.favorites) {
                    Log.d("base activity","favorite");
                    intent = new Intent(context, WishListActivity.class);
                } else if (id == R.id.cart) {
                    intent = new Intent(context, CartActivity.class);
                } else if (id == R.id.profile) {
                    intent = new Intent(context, ProfileActivity.class);
                }
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });
    }


}
