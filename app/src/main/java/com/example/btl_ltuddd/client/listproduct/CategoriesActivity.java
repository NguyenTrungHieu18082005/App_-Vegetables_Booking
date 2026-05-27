package com.example.btl_ltuddd.client.listproduct;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.client.cart.CartActivity;
import com.example.btl_ltuddd.client.dashboard.ClientActivity;
import com.example.btl_ltuddd.client.dashboard.ProductAdapter;
import com.example.btl_ltuddd.client.profile.ProfileActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private DatabaseHelper dbHelper;
    private EditText edtSearch;

    private LinearLayout btnNavHome, btnNavOrders, btnNavProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        dbHelper     = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerCategories);
        edtSearch    = findViewById(R.id.edtSearch);
        btnNavHome   = findViewById(R.id.btnNavHome);
        btnNavOrders = findViewById(R.id.btnNavOrders);
        btnNavProfile= findViewById(R.id.btnNavProfile);

        adapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        loadProducts();
        setupSearch();
        setupNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
        if (edtSearch != null) {
            adapter.filter(edtSearch.getText().toString());
        }
    }

    private void loadProducts() {
        List<Product> list = dbHelper.getAllProducts();
        adapter.updateData(list);
        if (list.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm", Toast.LENGTH_SHORT).show();
        }
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

    private void setupNavigation() {
        btnNavHome.setOnClickListener(v -> {
            startActivity(new Intent(this, ClientActivity.class));
            finish();
        });
        btnNavOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
            finish();
        });
        btnNavProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
    }
}