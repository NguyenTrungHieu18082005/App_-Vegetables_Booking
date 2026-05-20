package com.example.btl_ltuddd.client.listproduct;


import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.client.cart.CartActivity;
import com.example.btl_ltuddd.client.dashboard.ClientActivity;
import com.example.btl_ltuddd.client.dashboard.Product;
import com.example.btl_ltuddd.client.dashboard.ProductAdapter;
import com.example.btl_ltuddd.client.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productList;

    TextView txtSort;
    LinearLayout btnNavHome, btnNavOrders, btnNavProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // ==========================================
        // 1. ĐỔ DATA SẢN PHẨM THEO GIAO DIỆN MỚI
        // ==========================================
        recyclerView = findViewById(R.id.recyclerCategories);
        productList = new ArrayList<>();

        // Thêm dữ liệu rau củ mới từ ảnh mẫu
        productList.add(new Product(
                "Súp lơ xanh",
                "35.000đ",
                "/ 500g / Túi",
                R.drawable.tomato, // Hãy đảm bảo bạn đã có ảnh súp lơ trong drawable nhé
                "ORGANIC"
        ));

        productList.add(new Product(
                "Cà rốt hữu cơ",
                "42.000đ",
                "/ 1kg / Túi",
                R.drawable.tomato,
                "ĐÀ LẠT"
        ));

        productList.add(new Product(
                "Cà chua bi đỏ",
                "28.000đ",
                "/ 300g / Hộp",
                R.drawable.tomato,
                "SALE"
        ));

        productList.add(new Product(
                "Cải xoăn Kale",
                "45.000đ",
                "/ 250g / Túi",
                R.drawable.tomato,
                ""
        ));

        // Tái sử dụng ProductAdapter đã xử lý nút thêm vào giỏ hàng ở câu trước
        adapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // ==========================================
//        // 2. XỬ LÝ SỰ KIỆN BỘ LỌC SẮP XẾP
//        // ==========================================
//        txtSort = findViewById(R.id.txtSort);
//        txtSort.setOnClickListener(v -> {
//            Toast.makeText(this, "Mở tính năng lọc theo giá/bán chạy", Toast.LENGTH_SHORT).show();
//        });

        // ==========================================
        // 3. ĐIỀU HƯỚNG QUAY LẠI TRANG CHỦ (HOME)
        // ==========================================
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavOrders = findViewById(R.id.btnNavOrders);
        btnNavProfile = findViewById(R.id.btnNavProfile);

        // Nhấn nút Home để quay lại ClientActivity trang chủ
        btnNavHome.setOnClickListener(v -> {
            Intent intent = new Intent(CategoriesActivity.this, ClientActivity.class);
            startActivity(intent);
            finish(); // Đóng màn hình danh mục này lại
        });

        btnNavOrders.setOnClickListener(v -> {
            Intent intent = new Intent(CategoriesActivity.this, CartActivity.class);
            startActivity(intent);
            finish(); // Đóng màn hình danh mục này lại
        });

        btnNavProfile.setOnClickListener(v -> {
            Intent intent = new Intent(CategoriesActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish(); // Đóng màn hình danh mục này lại        });
    });
}
}