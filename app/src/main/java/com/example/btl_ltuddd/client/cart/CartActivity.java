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
import com.example.btl_ltuddd.client.listproduct.CategoriesActivity;
import com.example.btl_ltuddd.client.profile.ProfileActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;

    private CartAdapter adapter;

    private List<Product> cartList;

    private DatabaseHelper dbHelper;

    private Button btnOrder;

    private LinearLayout btnNavHome;

    private LinearLayout btnNavCategories;

    private LinearLayout btnNavOrders;

    private LinearLayout btnNavProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_cart
        );

        initViews();

        dbHelper =
                DatabaseHelper.getInstance(
                        this
                );

        loadCart();

        setupEvents();

        setupNavigation();
    }

    private void initViews() {

        recyclerCart =
                findViewById(
                        R.id.recyclerCart
                );

        btnOrder =
                findViewById(
                        R.id.btnOrder
                );

        btnNavHome =
                findViewById(
                        R.id.btnNavHome
                );

        btnNavCategories =
                findViewById(
                        R.id.btnNavCategories
                );

        btnNavOrders =
                findViewById(
                        R.id.btnNavOrders
                );

        btnNavProfile =
                findViewById(
                        R.id.btnNavProfile
                );
    }

    private void loadCart() {

        long userId =

                getSharedPreferences(
                        "auth",
                        MODE_PRIVATE
                )

                        .getLong(
                                "userId",
                                -1
                        );

        if (userId == -1) {

            cartList =
                    new ArrayList<>();

        }

        else {

            cartList =
                    dbHelper.getCartProducts(
                            (int) userId
                    );
        }

        adapter =
                new CartAdapter(
                        this,
                        cartList
                );

        recyclerCart.setLayoutManager(
                new LinearLayoutManager(
                        this
                )
        );

        recyclerCart.setAdapter(
                adapter
        );

        if (cartList.isEmpty()) {

            Toast.makeText(
                    this,
                    "Giỏ hàng đang trống",
                    Toast.LENGTH_SHORT
            ).show();
        }

    }

    private void setupEvents() {

        btnOrder.setOnClickListener(v -> {

            if (cartList.isEmpty()) {

                Toast.makeText(
                        this,
                        "Không có sản phẩm để đặt",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                    this,
                    "Đặt hàng thành công",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }

    private void setupNavigation() {

        btnNavHome.setOnClickListener(v -> {

            startActivity(

                    new Intent(
                            this,
                            ClientActivity.class
                    )

            );

            finish();

        });

        btnNavCategories.setOnClickListener(v -> {

            startActivity(

                    new Intent(
                            this,
                            CategoriesActivity.class
                    )

            );

            finish();

        });

        btnNavOrders.setOnClickListener(v ->

                Toast.makeText(
                        this,
                        "Bạn đang ở Giỏ hàng",
                        Toast.LENGTH_SHORT
                ).show()

        );

        btnNavProfile.setOnClickListener(v -> {

            startActivity(

                    new Intent(
                            this,
                            ProfileActivity.class
                    )

            );

            finish();

        });

    }

    @Override
    protected void onResume() {

        super.onResume();

        loadCart();

    }
}