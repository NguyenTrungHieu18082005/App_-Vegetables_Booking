package com.example.btl_ltuddd.client.cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.client.dashboard.ClientActivity;
import com.example.btl_ltuddd.client.listproduct.CategoriesActivity;
import com.example.btl_ltuddd.client.profile.ProfileActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Cart;
import com.example.btl_ltuddd.model.User;  // FIX: import User

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private CartAdapter adapter;
    private List<Cart> cartList;
    private DatabaseHelper dbHelper;
    private Button btnOrder;
    private LinearLayout btnNavHome;
    private LinearLayout btnNavCategories;
    private LinearLayout btnNavOrders;
    private LinearLayout btnNavProfile;
    private TextView txtSubtotal;
    private TextView txtTotal;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initViews();
        dbHelper = DatabaseHelper.getInstance(this);
        cartList = new ArrayList<>();
        loadUser();
        loadCart();
        setupEvents();
        setupNavigation();
    }

    private void initViews() {
        recyclerCart    = findViewById(R.id.recyclerCart);
        txtSubtotal     = findViewById(R.id.txtSubtotal);
        txtTotal        = findViewById(R.id.txtTotal);
        btnOrder        = findViewById(R.id.btnOrder);
        btnNavHome      = findViewById(R.id.btnNavHome);
        btnNavCategories= findViewById(R.id.btnNavCategories);
        btnNavOrders    = findViewById(R.id.btnNavOrders);
        btnNavProfile   = findViewById(R.id.btnNavProfile);
    }

    private void loadUser() {
        userId = (int) getSharedPreferences("auth", MODE_PRIVATE)
                .getLong("userId", -1);
    }

    private void loadCart() {
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        cartList = dbHelper.getCartItems(userId);
        adapter = new CartAdapter(this, cartList, () -> updateSubtotal());
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(adapter);
        updateSubtotal();
    }

    /**
     * Xử lý click nút đặt hàng
     * Flow:
     *  1. Lấy User object (tên, email, sđt) từ DB
     *  2. Tạo đơn hàng + order_items + xóa giỏ
     *  3. Hiển thị dialog chi tiết đơn hàng
     */
    private void setupEvents() {
        btnOrder.setOnClickListener(v -> {

            if (cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
                return;
            }

            // FIX: dùng User object thay vì String[]
            User user = dbHelper.getUserById(userId);
            if (user == null) {
                Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            String customerName  = user.getFullname();
            String customerEmail = user.getEmail();
            String customerPhone = user.getPhone() != null ? user.getPhone() : "Chưa cập nhật";

            // Tính tổng tiền
            double subtotal = 0;
            for (Cart item : cartList) {
                subtotal += item.getProductPrice() * item.getQuantity();
            }
            double ship  = 15000;
            double total = subtotal + ship;

            // Tạo đơn hàng (transaction: insert order + order_items + xóa giỏ)
            long orderId = dbHelper.placeOrder(userId, cartList, subtotal, total);

            if (orderId == -1) {
                Toast.makeText(this, "Đặt hàng thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                return;
            }

            String orderCode = "#FH-" + String.format("%04d", orderId);

            // Build nội dung dialog
            StringBuilder detail = new StringBuilder();
            detail.append("✅ Đặt hàng thành công!\n\n");
            detail.append("📋 Mã đơn: ").append(orderCode).append("\n");
            detail.append("👤 Khách hàng: ").append(customerName).append("\n");
            detail.append("📧 Email: ").append(customerEmail).append("\n");
            detail.append("📞 SĐT: ").append(customerPhone).append("\n\n");
            detail.append("🛒 Sản phẩm (").append(cartList.size()).append(" loại):\n");

            for (Cart item : cartList) {
                detail.append("  • ").append(item.getProductName())
                        .append(" x").append(item.getQuantity())
                        .append(" = ").append(String.format("%,.0fđ",
                                item.getProductPrice() * item.getQuantity()))
                        .append("\n");
            }

            detail.append("\n💰 Tiền hàng: ").append(String.format("%,.0fđ", subtotal));
            detail.append("\n🚚 Phí ship: ").append(String.format("%,.0fđ", ship));
            detail.append("\n💳 Tổng cộng: ").append(String.format("%,.0fđ", total));

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Chi tiết đơn hàng")
                    .setMessage(detail.toString())
                    .setPositiveButton("OK", (dialog, which) -> {
                        cartList.clear();
                        adapter.notifyDataSetChanged();
                        updateSubtotal();
                    })
                    .setCancelable(false)
                    .show();
        });
    }

    private void setupNavigation() {
        btnNavHome.setOnClickListener(v -> {
            startActivity(new Intent(this, ClientActivity.class));
            finish();
        });
        btnNavCategories.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoriesActivity.class));
            finish();
        });
        btnNavOrders.setOnClickListener(v ->
                Toast.makeText(this, "Bạn đang ở Giỏ hàng", Toast.LENGTH_SHORT).show());
        btnNavProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
    }

    private void updateSubtotal() {
        double subtotal = 0;
        for (Cart item : cartList) {
            subtotal += item.getProductPrice() * item.getQuantity();
        }
        double ship  = 15000;
        double total = subtotal + ship;
        txtSubtotal.setText(String.format("%,.0fđ", subtotal));
        txtTotal.setText(String.format("%,.0fđ", total));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }
}