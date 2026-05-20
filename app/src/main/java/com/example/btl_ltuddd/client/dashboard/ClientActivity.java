package com.example.btl_ltuddd.client.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_ltuddd.client.cart.CartActivity;
import com.example.btl_ltuddd.client.listproduct.CategoriesActivity;
import com.example.btl_ltuddd.R;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productList;

    // Khai báo các nút bấm cha trên thanh Menu dưới cùng
    LinearLayout btnNavHome, btnNavCategories, btnNavOrders, btnNavProfile;

    // KHAI BÁO THÊM: Các View con cần thay đổi trạng thái Highlight
    LinearLayout bgHome, bgCategories, bgOrders, bgProfile;
    ImageView icHome, icCategories, icOrders, icProfile;
    TextView tvHome, tvCategories, tvOrders, tvProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        // ==========================================
        // 1. CÀI ĐẶT RECYCLERVIEW SẢN PHẨM
        // ==========================================
        recyclerView = findViewById(R.id.recyclerProducts);
        productList = new ArrayList<>();

        productList.add(new Product("Cà chua hữu cơ", "45.000đ", "/500g", R.drawable.tomato, "ORGANIC"));
        productList.add(new Product("Xà lách thủy canh", "32.000đ", "/ Bó", R.drawable.tomato, "THỦY CANH"));
        productList.add(new Product("Cam sành", "55.000đ", "/ kg", R.drawable.tomato, "ĐẶC SẢN"));
        productList.add(new Product("Nấm đùi gà", "60.000đ", "/ 250g", R.drawable.tomato, ""));

        adapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // ==========================================
        // 2. KẾT NỐI VÀ ÁNH XẠ TOÀN BỘ VIEW BOTTOM BAR
        // ==========================================
        initBottomNavViews();

        // Mặc định ban đầu khi mở màn hình, chọn Tab Home (Tab 1)
        selectTab(1);

        // ==========================================
        // 3. XỬ LÝ SỰ KIỆN CLICK TỪNG TAB
        // ==========================================
        btnNavHome.setOnClickListener(v -> {
            selectTab(1); // Đổi giao diện Highlight sang Home
            Toast.makeText(ClientActivity.this, "Bạn đang ở Trang chủ", Toast.LENGTH_SHORT).show();
        });

        btnNavCategories.setOnClickListener(v -> {
            selectTab(2);
            Intent intent = new Intent(ClientActivity.this, CategoriesActivity.class);
            startActivity(intent);
        });

        btnNavOrders.setOnClickListener(v -> {
            selectTab(3); // Đổi giao diện Highlight sang Orders
             Intent intent = new Intent(ClientActivity.this, CartActivity.class);
             startActivity(intent);
        });

        btnNavProfile.setOnClickListener(v -> {
            selectTab(4); // Đổi giao diện Highlight sang Profile
            Toast.makeText(ClientActivity.this, "Chuyển sang màn hình Cá nhân", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(ClientActivity.this, ProfileActivity.class);
            // startActivity(intent);
        });
    }

    /**
     * Hàm phụ trách ánh xạ toàn bộ ID từ file XML (giúp cấu trúc onCreate gọn hơn)
     */
    private void initBottomNavViews() {
        // Ánh xạ layout nút bấm cha
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavCategories = findViewById(R.id.btnNavCategories);
        btnNavOrders = findViewById(R.id.btnNavOrders);
        btnNavProfile = findViewById(R.id.btnNavProfile);

        // Ánh xạ layout background bọc icon
        bgHome = findViewById(R.id.bgHome);
        bgCategories = findViewById(R.id.bgCategories);
        bgOrders = findViewById(R.id.bgOrders);
        bgProfile = findViewById(R.id.bgProfile);

        // Ánh xạ các Icon thực tế
        icHome = findViewById(R.id.icHome);
        icCategories = findViewById(R.id.icCategories);
        icOrders = findViewById(R.id.icOrders);
        icProfile = findViewById(R.id.icProfile);

        // Ánh xạ các văn bản text dưới icon
        tvHome = findViewById(R.id.tvHome);
        tvCategories = findViewById(R.id.tvCategories);
        tvOrders = findViewById(R.id.tvOrders);
        tvProfile = findViewById(R.id.tvProfile);
    }

    /**
     * Hàm quản lý trạng thái Highlight thông minh.
     * @param tabNumber: số thứ tự tab (1: Home, 2: Categories, 3: Orders, 4: Profile)
     */
    private void selectTab(int tabNumber) {
        // Nếu tabNumber trùng khớp với vị trí, view đó nhận true (Active), ngược lại nhận false (Inactive)

        // Cập nhật Tab Home
        bgHome.setSelected(tabNumber == 1);
        icHome.setSelected(tabNumber == 1);
        tvHome.setSelected(tabNumber == 1);

        // Cập nhật Tab Categories
        bgCategories.setSelected(tabNumber == 2);
        icCategories.setSelected(tabNumber == 2);
        tvCategories.setSelected(tabNumber == 2);

        // Cập nhật Tab Orders
        bgOrders.setSelected(tabNumber == 3);
        icOrders.setSelected(tabNumber == 3);
        tvOrders.setSelected(tabNumber == 3);

        // Cập nhật Tab Profile
        bgProfile.setSelected(tabNumber == 4);
        icProfile.setSelected(tabNumber == 4);
        tvProfile.setSelected(tabNumber == 4);
    }
}