package com.example.btl_ltuddd.client.listproduct.detail;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.client.listproduct.CategoriesActivity;

public class ProductDetailActivity extends AppCompatActivity {

    TextView btnMinus, btnPlus, txtQty;
    Button btnAddToCart;
    ImageView btnBack;
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        btnBack = findViewById(R.id.btnBack);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        txtQty = findViewById(R.id.txtQty);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // ==========================================
        // NHẬN DATA TỪ INTENT
        // ==========================================
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String tag = getIntent().getStringExtra("tag");
        int image = getIntent().getIntExtra("image", R.drawable.tomato);

        // Gán vào View
        ((TextView) findViewById(R.id.txtDetailName)).setText(name);
        ((TextView) findViewById(R.id.txtDetailPrice)).setText(price);
        ((ImageView) findViewById(R.id.imgDetail)).setImageResource(image);

        // Ẩn tag nếu rỗng
        TextView txtTag = findViewById(R.id.txtTagDetail);
        if (tag != null && !tag.isEmpty()) {
            txtTag.setText(tag);
        } else {
            txtTag.setVisibility(android.view.View.GONE);
        }

        // Quay lại CategoriesActivity
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoriesActivity.class));
            finish();
        });

        // Tăng giảm số lượng
        btnPlus.setOnClickListener(v -> {
            quantity++;
            txtQty.setText(String.valueOf(quantity));
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                txtQty.setText(String.valueOf(quantity));
            }
        });

        // Thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v ->
                Toast.makeText(this, "Đã thêm " + quantity + " sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
        );
    }

}