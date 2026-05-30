package com.example.btl_ltuddd.client.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.auth.LoginActivity;
import com.example.btl_ltuddd.client.cart.CartActivity;
import com.example.btl_ltuddd.client.dashboard.ClientActivity;
import com.example.btl_ltuddd.client.listproduct.CategoriesActivity;
import com.example.btl_ltuddd.client.profile.PersonalAddress.AddressActivity;
import com.example.btl_ltuddd.client.profile.PersonalInfo.PersonalInfoActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;

public class ProfileActivity extends AppCompatActivity {
    TextView txtUserName;
    DatabaseHelper dbHelper;
    LinearLayout menuPersonalInfo, menuAddress, btnLogout;
    LinearLayout btnNavHome, btnNavCategories, btnNavOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        dbHelper = DatabaseHelper.getInstance(this);

        setupClickListeners();
        loadUserData(); // Gọi hàm lấy tên
    }

    private void initViews() {
        txtUserName = findViewById(R.id.txtUserName);
        menuPersonalInfo = findViewById(R.id.menuPersonalInfo);
        menuAddress = findViewById(R.id.menuAddress);
        btnLogout = findViewById(R.id.btnLogout);
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavCategories = findViewById(R.id.btnNavCategories);
        btnNavOrders = findViewById(R.id.btnNavOrders);
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1);

        if (userId != -1) {
            String fullName = dbHelper.getUserNameById(userId);
            // Kiểm tra nếu chuỗi rỗng thì để tên mặc định
            if (fullName != null && !fullName.isEmpty()) {
                txtUserName.setText(fullName);
            } else {
                txtUserName.setText("Người dùng");
            }
        }
    }

    private void setupClickListeners() {
        menuPersonalInfo.setOnClickListener(v -> startActivity(new Intent(this, PersonalInfoActivity.class)));
        menuAddress.setOnClickListener(v -> startActivity(new Intent(this, AddressActivity.class)));

        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("auth", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnNavHome.setOnClickListener(v -> { startActivity(new Intent(this, ClientActivity.class)); finish(); });
        btnNavCategories.setOnClickListener(v -> { startActivity(new Intent(this, CategoriesActivity.class)); finish(); });
        btnNavOrders.setOnClickListener(v -> { startActivity(new Intent(this, CartActivity.class)); finish(); });
    }
}