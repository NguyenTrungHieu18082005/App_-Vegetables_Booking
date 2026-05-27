package com.example.btl_ltuddd.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.admin.auth.AdminLoginActivity;
import com.example.btl_ltuddd.admin.fragment.AdminDashboardFragment;
import com.example.btl_ltuddd.admin.order.AdminOrderActivity;
import com.example.btl_ltuddd.admin.product.AdminProductActivity;
import com.example.btl_ltuddd.admin.profile.AdminProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra session admin
        SharedPreferences prefs = getSharedPreferences("admin_auth", MODE_PRIVATE);
        if (prefs.getLong("adminId", -1) == -1) {
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_admin);

        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_nav);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_container, new AdminDashboardFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.admin_container, new AdminDashboardFragment())
                        .commit();
                return true;
            } else if (id == R.id.nav_categories) {
                startActivity(new Intent(this, AdminProductActivity.class));
                return true;
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(this, AdminOrderActivity.class));
                return true;
            }
//            else if (id == R.id.nav_profile) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.admin_container, new AdminProfileFragmentActivity())
//                        .commit();
//                return true;
//            }

//            else if (id == R.id.nav_profile) {
//                startActivity(new Intent(this, AdminProfileFragmentActivity.class));
//                return true;
//            }

            else if (id == R.id.nav_profile) {

                startActivity(new Intent(this, AdminProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}