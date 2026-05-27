package com.example.btl_ltuddd.client.listproduct.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    TextView btnMinus, btnPlus, txtQty;
    TextView txtDetailName, txtDetailPrice, txtTagDetail, txtDescription;
    Button   btnAddToCart;
    ImageView btnBack, imgDetail;

    int quantity = 1;
    Product product;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ view
        btnBack       = findViewById(R.id.btnBack);
        btnMinus      = findViewById(R.id.btnMinus);
        btnPlus       = findViewById(R.id.btnPlus);
        txtQty        = findViewById(R.id.txtQty);
        btnAddToCart  = findViewById(R.id.btnAddToCart);
        txtDetailName = findViewById(R.id.txtDetailName);
        txtDetailPrice   = findViewById(R.id.txtDetailPrice);
        txtDescription = findViewById(R.id.txtDescription);
        txtTagDetail  = findViewById(R.id.txtTagDetail);
        imgDetail     = findViewById(R.id.imgDetail);

        dbHelper = DatabaseHelper.getInstance(this);

        // Nhận productId từ Intent (truyền từ ProductAdapter)
        int productId = getIntent().getIntExtra("productId", -1);

        if (productId == -1) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load sản phẩm từ DB
        product = dbHelper.getProductById(productId);

        if (product == null) {
            Toast.makeText(this, "Sản phẩm không tồn tại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin
        txtDetailName.setText(product.getName());
        txtDetailPrice.setText(String.format("%,.0fđ / %s", product.getPrice(), product.getUnit()));

        // Hiển thị mô tả
        txtDescription.setText(product.getDescription() != null ? product.getDescription() : "Chưa có mô tả");

        if (product.getCategory() != null && !product.getCategory().isEmpty()) {
            txtTagDetail.setText(product.getCategory());
        } else {
            txtTagDetail.setVisibility(View.GONE);
        }

        if (product.getImageUrl() != null) {
            imgDetail.setImageURI(android.net.Uri.parse(product.getImageUrl()));
        }

        // Quay lại
        btnBack.setOnClickListener(v -> finish());

        // Tăng / giảm số lượng
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

        // Thêm vào giỏ hàng thật
        btnAddToCart.setOnClickListener(v -> {
            long userId = getSharedPreferences("auth", Context.MODE_PRIVATE)
                    .getLong("userId", -1);

            if (userId == -1) {
                Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            // insertCart đã xử lý: nếu có rồi thì tăng quantity,
            // nên gọi quantity lần để đúng số lượng người dùng chọn
            for (int i = 0; i < quantity; i++) {
                dbHelper.insertCart((int) userId, product.getId());
            }

            Toast.makeText(this,
                    "Đã thêm " + quantity + " " + product.getName() + " vào giỏ hàng",
                    Toast.LENGTH_SHORT).show();
        });
    }
}