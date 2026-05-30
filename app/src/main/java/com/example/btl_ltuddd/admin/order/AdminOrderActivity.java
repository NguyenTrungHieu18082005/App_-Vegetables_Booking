package com.example.btl_ltuddd.admin.order;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.admin.AdminActivity;
import com.example.btl_ltuddd.admin.product.AdminProductActivity;
import com.example.btl_ltuddd.admin.profile.AdminProfileActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class AdminOrderActivity extends AppCompatActivity {
    private RecyclerView rvOrders;
    private OrderAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        dbHelper = DatabaseHelper.getInstance(this);

        // Ánh xạ View
        rvOrders = findViewById(R.id.rv_orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        // Tải dữ liệu
        loadData();

        // Thiết lập Bottom Navigation
        setupBottomNav();
    }

    private void loadData() {
        List<Order> orderList = dbHelper.getAllOrders();
        adapter = new OrderAdapter(orderList);
        rvOrders.setAdapter(adapter);
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_orders);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, AdminActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_categories) {
                startActivity(new Intent(this, AdminProductActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, AdminProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}