package com.example.btl_ltuddd.client.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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
import com.example.btl_ltuddd.client.profile.ProfileActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private DatabaseHelper dbHelper;
    private EditText edtSearch;

    LinearLayout btnNavHome, btnNavCategories, btnNavOrders, btnNavProfile;
    LinearLayout bgHome, bgCategories, bgOrders, bgProfile;
    ImageView icHome, icCategories, icOrders, icProfile;
    TextView tvHome, tvCategories, tvOrders, tvProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        dbHelper     = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerProducts);
        edtSearch    = findViewById(R.id.edtSearch);

        // Khởi tạo adapter với danh sách rỗng trước
        adapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        loadProducts();
        setupSearch();
        initBottomNavViews();
        selectTab(1);

        btnNavHome.setOnClickListener(v -> {
            selectTab(1);
            Toast.makeText(this, "Bạn đang ở Trang chủ", Toast.LENGTH_SHORT).show();
        });
        btnNavCategories.setOnClickListener(v -> {
            selectTab(2);
            startActivity(new Intent(this, CategoriesActivity.class));
        });
        btnNavOrders.setOnClickListener(v -> {
            selectTab(3);
            startActivity(new Intent(this, CartActivity.class));
        });
        btnNavProfile.setOnClickListener(v -> {
            selectTab(4);
            startActivity(new Intent(this, ProfileActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
        // Giữ nguyên text đang search sau khi resume
        if (edtSearch != null) {
            adapter.filter(edtSearch.getText().toString());
        }
    }

    private void loadProducts() {
        List<Product> list = dbHelper.getAllProducts();
        adapter.updateData(list);
    }

    private void setupSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void initBottomNavViews() {
        btnNavHome       = findViewById(R.id.btnNavHome);
        btnNavCategories = findViewById(R.id.btnNavCategories);
        btnNavOrders     = findViewById(R.id.btnNavOrders);
        btnNavProfile    = findViewById(R.id.btnNavProfile);
        bgHome           = findViewById(R.id.bgHome);
        bgCategories     = findViewById(R.id.bgCategories);
        bgOrders         = findViewById(R.id.bgOrders);
        bgProfile        = findViewById(R.id.bgProfile);
        icHome           = findViewById(R.id.icHome);
        icCategories     = findViewById(R.id.icCategories);
        icOrders         = findViewById(R.id.icOrders);
        icProfile        = findViewById(R.id.icProfile);
        tvHome           = findViewById(R.id.tvHome);
        tvCategories     = findViewById(R.id.tvCategories);
        tvOrders         = findViewById(R.id.tvOrders);
        tvProfile        = findViewById(R.id.tvProfile);
    }

    private void selectTab(int tabNumber) {
        bgHome.setSelected(tabNumber == 1);
        icHome.setSelected(tabNumber == 1);
        tvHome.setSelected(tabNumber == 1);
        bgCategories.setSelected(tabNumber == 2);
        icCategories.setSelected(tabNumber == 2);
        tvCategories.setSelected(tabNumber == 2);
        bgOrders.setSelected(tabNumber == 3);
        icOrders.setSelected(tabNumber == 3);
        tvOrders.setSelected(tabNumber == 3);
        bgProfile.setSelected(tabNumber == 4);
        icProfile.setSelected(tabNumber == 4);
        tvProfile.setSelected(tabNumber == 4);
    }
}