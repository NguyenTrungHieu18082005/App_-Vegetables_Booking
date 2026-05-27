package com.example.btl_ltuddd.admin.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
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
    private List<Order> orderList;
    private DatabaseHelper dbHelper;

    // Các TextView hiển thị thống kê
    private TextView tvTotal, tvPending, tvShipping, tvDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order); // Giữ nguyên layout file của bạn

        // Khởi tạo Database đúng cách (Singleton)
        dbHelper = DatabaseHelper.getInstance(this);

        // Ánh xạ View
        rvOrders = findViewById(R.id.rv_orders); // ID khớp với XML của bạn
        tvTotal = findViewById(R.id.tv_total_orders);
        tvPending = findViewById(R.id.tv_pending_orders);
        tvShipping = findViewById(R.id.tv_shipping_orders);
        tvDone = findViewById(R.id.tv_done_orders);

        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        loadData();

        // ... bên trong onCreate sau khi setContentView
        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_nav);

// Thiết lập icon đang được chọn là "Orders"
        bottomNav.setSelectedItemId(R.id.nav_orders);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, AdminActivity.class));
                finish(); // Đóng Activity hiện tại để tránh chồng chéo
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

    private void loadData() {
        orderList = dbHelper.getAllOrders();
        adapter = new OrderAdapter(orderList);
        rvOrders.setAdapter(adapter);

        // Cập nhật thống kê
        tvTotal.setText(String.valueOf(dbHelper.getOrderCount()));
        tvPending.setText(String.valueOf(dbHelper.getPendingOrderCount()));
        tvShipping.setText(String.valueOf(dbHelper.getShippingOrderCount()));
        tvDone.setText(String.valueOf(dbHelper.getDoneOrderCount()));
    }
}