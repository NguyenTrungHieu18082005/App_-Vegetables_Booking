package com.example.btl_ltuddd.client.cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.client.dashboard.ClientActivity;
import com.example.btl_ltuddd.client.listproduct.CategoriesActivity;
import com.example.btl_ltuddd.client.profile.ProfileActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Cart;
import com.example.btl_ltuddd.model.User;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private CartAdapter adapter;
    private List<Cart> cartList;
    private DatabaseHelper dbHelper;
    private Button btnOrder;
    private CardView cardAddress;
    private TextView tvUserAddress, txtSubtotal, txtTotal;
    private LinearLayout btnNavHome, btnNavCategories, btnNavOrders, btnNavProfile;
    private int userId;
    private String currentAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = DatabaseHelper.getInstance(this);
        initViews();
        loadUser();
        loadCart();
        setupNavigation();

        cardAddress.setOnClickListener(v -> showAddressDialog());
        btnOrder.setOnClickListener(v -> attemptOrder());
    }

    private void initViews() {
        recyclerCart    = findViewById(R.id.recyclerCart);
        txtSubtotal     = findViewById(R.id.txtSubtotal);
        txtTotal        = findViewById(R.id.txtTotal);
        btnOrder        = findViewById(R.id.btnOrder);
        cardAddress     = findViewById(R.id.cardAddress);
        tvUserAddress   = findViewById(R.id.tvUserAddress);
        btnNavHome      = findViewById(R.id.btnNavHome);
        btnNavCategories= findViewById(R.id.btnNavCategories);
        btnNavOrders    = findViewById(R.id.btnNavOrders);
        btnNavProfile   = findViewById(R.id.btnNavProfile);
    }

    private void showAddressDialog() {
        EditText input = new EditText(this);
        input.setPadding(40, 20, 40, 20);
        new AlertDialog.Builder(this)
                .setTitle("Cập nhật địa chỉ")
                .setView(input)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    currentAddress = input.getText().toString().trim();
                    if (!currentAddress.isEmpty()) {
                        tvUserAddress.setText(currentAddress);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    //Đăt Hang
    private void attemptOrder() {
        // 1. Kiểm tra giỏ hàng
        if (cartList == null || cartList.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra địa chỉ
        if (currentAddress == null || currentAddress.trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng cập nhật địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Lấy thông tin user để xác nhận đơn hàng
        User user = dbHelper.getUserById(userId);
        if (user == null) {
            Toast.makeText(this, "Lỗi xác thực người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Tính toán tổng tiền
        double subtotal = 0;
        for (Cart item : cartList) {
            subtotal += item.getProductPrice() * item.getQuantity();
        }
        double total = subtotal + 15000; // 15.000đ phí ship

        // 5. Gọi hàm lưu vào Database
        try {
            long orderId = dbHelper.placeOrder(userId, cartList, subtotal, total, currentAddress);

            if (orderId != -1) {
                // Đặt hàng thành công -> Hiện Dialog
                showOrderSuccessDialog(orderId, subtotal, total, user);
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi lưu đơn hàng!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showOrderSuccessDialog(long orderId, double subtotal, double total, User user) {
        String orderCode = "#FH-" + String.format("%04d", orderId);

        // Xây dựng nội dung Dialog
        StringBuilder message = new StringBuilder();
        message.append("✅ Đặt hàng thành công!\n\n");
        message.append("📋 Mã đơn: ").append(orderCode).append("\n\n");
        message.append("--- Thông tin khách hàng ---\n");
        message.append("👤 Tên: ").append(user.getFullname()).append("\n");
        message.append("📞 SĐT: ").append(user.getPhone() != null ? user.getPhone() : "Chưa cập nhật").append("\n");
        message.append("📧 Email: ").append(user.getEmail()).append("\n");
        message.append("📍 Địa chỉ: ").append(currentAddress).append("\n\n");
        message.append("--- Chi tiết thanh toán ---\n");
        message.append("💰 Tiền hàng: ").append(String.format("%,.0fđ", subtotal)).append("\n");
        message.append("🚚 Phí ship: 15.000đ\n");
        message.append("💳 Tổng cộng: ").append(String.format("%,.0fđ", total));

        // Khởi tạo Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đặt hàng");
        builder.setMessage(message.toString());

        // Không cho phép đóng bằng cách chạm ra ngoài
        builder.setCancelable(false);

        // Nút OK
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            cartList.clear(); // Xóa sạch list giỏ hàng
            finish();         // Thoát màn hình giỏ hàng
        });

        builder.show();
    }
    private void loadUser() {
        userId = (int) getSharedPreferences("auth", MODE_PRIVATE).getLong("userId", -1);
    }

    private void loadCart() {
        cartList = dbHelper.getCartItems(userId);
        adapter = new CartAdapter(this, cartList, this::updateSubtotal);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(adapter);
        updateSubtotal();
    }

    private void updateSubtotal() {
        double subtotal = 0;
        for (Cart item : cartList) subtotal += item.getProductPrice() * item.getQuantity();
        txtSubtotal.setText(String.format("%,.0fđ", subtotal));
        txtTotal.setText(String.format("%,.0fđ", subtotal + 15000));
    }

    private void setupNavigation() {
        btnNavHome.setOnClickListener(v -> { startActivity(new Intent(this, ClientActivity.class)); finish(); });
        btnNavCategories.setOnClickListener(v -> { startActivity(new Intent(this, CategoriesActivity.class)); finish(); });
        btnNavOrders.setOnClickListener(v -> Toast.makeText(this, "Bạn đang ở giỏ hàng", Toast.LENGTH_SHORT).show());
        btnNavProfile.setOnClickListener(v -> { startActivity(new Intent(this, ProfileActivity.class)); finish(); });
    }
}