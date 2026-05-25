package com.example.btl_ltuddd.client.listproduct;


import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ProductAdapter adapter;

    private List<Product> productList;

    private DatabaseHelper dbHelper;

    private LinearLayout btnNavHome;

    private LinearLayout btnNavOrders;

    private LinearLayout btnNavProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_categories
        );

        recyclerView =
                findViewById(
                        R.id.recyclerCategories
                );

        btnNavHome =
                findViewById(
                        R.id.btnNavHome
                );

        btnNavOrders =
                findViewById(
                        R.id.btnNavOrders
                );

        btnNavProfile =
                findViewById(
                        R.id.btnNavProfile
                );

        dbHelper =
                DatabaseHelper.getInstance(
                        this
                );

        loadProducts();

        setupNavigation();
    }

    private void loadProducts() {

        productList =
                dbHelper.getAllProducts();

        adapter =
                new ProductAdapter(
                        this,
                        productList
                );

        recyclerView.setLayoutManager(
                new GridLayoutManager(
                        this,
                        2
                )
        );

        recyclerView.setAdapter(
                adapter
        );

        if (productList.isEmpty()) {

            Toast.makeText(
                    this,
                    "Không có sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void setupNavigation() {

        btnNavHome.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            CategoriesActivity.this,
                            ClientActivity.class
                    )
            );

            finish();
        });

        btnNavOrders.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            CategoriesActivity.this,
                            CartActivity.class
                    )
            );

            finish();
        });

        btnNavProfile.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            CategoriesActivity.this,
                            ProfileActivity.class
                    )
            );

            finish();
        });
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadProducts();
    }
}