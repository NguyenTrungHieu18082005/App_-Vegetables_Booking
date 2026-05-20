package com.example.btl_ltuddd.client.cart;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.client.dashboard.ClientActivity;
import com.example.btl_ltuddd.client.dashboard.Product;
import com.example.btl_ltuddd.client.listproduct.CategoriesActivity;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerCart;
    CartAdapter adapter;
    List<Product> cartList;

    Button btnOrder;
    LinearLayout btnNavHome, btnNavCategories, btnNavOrders, btnNavProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Data mẫu
        cartList = new ArrayList<>();
        cartList.add(new Product("Cải xoăn Kale hữu cơ", "45.000đ", "250g / Túi", R.drawable.tomato, ""));
        cartList.add(new Product("Cà chua bi hữu cơ", "38.000đ", "500g / Hộp", R.drawable.tomato, ""));

        recyclerCart = findViewById(R.id.recyclerCart);
        adapter = new CartAdapter(this, cartList);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(adapter);

        // Nút đặt hàng
        btnOrder = findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(v ->
                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show()
        );

        // Bottom nav
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavCategories = findViewById(R.id.btnNavCategories);
        btnNavOrders = findViewById(R.id.btnNavOrders);
        btnNavProfile = findViewById(R.id.btnNavProfile);

        btnNavHome.setOnClickListener(v -> {
            startActivity(new Intent(this, ClientActivity.class));
            finish(); // Đóng Màn Hình Cart
        });

        btnNavCategories.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoriesActivity.class));
            finish();
        });

        btnNavOrders.setOnClickListener(v ->
                Toast.makeText(this, "Bạn đang ở Giỏ hàng", Toast.LENGTH_SHORT).show()
        );

        btnNavProfile.setOnClickListener(v ->
                Toast.makeText(this, "Mở màn hình Cá nhân", Toast.LENGTH_SHORT).show()
        );
    }
}