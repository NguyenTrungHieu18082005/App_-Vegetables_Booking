package com.example.btl_ltuddd.client.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_ltuddd.MainActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.client.dashboard.ClientActivity;
import com.example.btl_ltuddd.client.listproduct.CategoriesActivity;
import com.example.btl_ltuddd.client.profile.PersonalAddress.AddressActivity;
import com.example.btl_ltuddd.client.profile.PersonalInfo.PersonalInfoActivity;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout menuPersonalInfo, menuAddress, btnLogout;
    LinearLayout btnNavHome, btnNavCategories, btnNavOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        menuPersonalInfo = findViewById(R.id.menuPersonalInfo);
        menuAddress = findViewById(R.id.menuAddress);
        btnLogout = findViewById(R.id.btnLogout);
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavCategories = findViewById(R.id.btnNavCategories);
        btnNavOrders = findViewById(R.id.btnNavOrders);

        menuPersonalInfo.setOnClickListener(v ->
                startActivity(new Intent(this, PersonalInfoActivity.class))
        );

        menuAddress.setOnClickListener(v ->
                startActivity(new Intent(this, AddressActivity.class))
        );

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnNavHome.setOnClickListener(v -> {
            startActivity(new Intent(this, ClientActivity.class));
            finish();
        });

        btnNavCategories.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoriesActivity.class));
            finish();
        });

        btnNavOrders.setOnClickListener(v ->
                Toast.makeText(this, "Mở Đơn hàng", Toast.LENGTH_SHORT).show()
        );
    }
}