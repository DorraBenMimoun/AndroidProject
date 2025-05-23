package com.example.ecommerce4you.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce4you.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public abstract class BaseAdminActivity extends AppCompatActivity {
    protected ChipNavigationBar bottomAdminNavigation;

    protected void setupBottomAdminNavigation(int selectedItemId, Context context) {
        bottomAdminNavigation = findViewById(R.id.bottomAdminNavigation);
        bottomAdminNavigation.setItemSelected(selectedItemId, true);



        bottomAdminNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {

                Intent intent = null;
                if (id == R.id.nav_dashboard) {
                    intent = new Intent(context, AdminDashboardActivity.class);
                } else if (id == R.id.nav_products) {
                    intent = new Intent(context, ItemListActivity.class);
                } else if (id == R.id.nav_users) {
                    intent = new Intent(context, UsersListActivity.class);
                } else if (id == R.id.nav_orders) {
                    intent = new Intent(context, AdminOrdersActivity.class);
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
