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

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    // Recycler hiển thị danh sách sản phẩm
    private RecyclerView recyclerCart;

    // Adapter đổ dữ liệu
    private CartAdapter adapter;

    // Danh sách sản phẩm trong giỏ
    private List<Cart> cartList;

    // Kết nối Database
    private DatabaseHelper dbHelper;

    // Nút đặt hàng
    private Button btnOrder;

    // Thanh điều hướng dưới
    private LinearLayout btnNavHome;

    private LinearLayout btnNavCategories;

    private LinearLayout btnNavOrders;

    private LinearLayout btnNavProfile;

    // Text tổng tiền
    private TextView txtSubtotal;

    private TextView txtTotal;

    // User đang đăng nhập
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_cart
        );

        // Ánh xạ View
        initViews();

        // Kết nối DB
        dbHelper =
                DatabaseHelper.getInstance(
                        this
                );

        cartList =
                new ArrayList<>();

        // Lấy user hiện tại
        loadUser();

        // Load dữ liệu giỏ
        loadCart();

        // Xử lý sự kiện
        setupEvents();

        // Điều hướng
        setupNavigation();

    }

    /**
     * Ánh xạ toàn bộ View
     */
    private void initViews() {

        recyclerCart =
                findViewById(
                        R.id.recyclerCart
                );

        txtSubtotal =
                findViewById(
                        R.id.txtSubtotal
                );

        txtTotal =
                findViewById(
                        R.id.txtTotal
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

    /**
     * Lấy user đang đăng nhập
     */
    private void loadUser() {

        userId =

                (int)

                        getSharedPreferences(
                                "auth",
                                MODE_PRIVATE
                        )

                                .getLong(
                                        "userId",
                                        -1
                                );

    }

    /**
     * Load dữ liệu giỏ hàng từ DB
     */
    private void loadCart() {

        if (
                userId == -1
        ) {

            Toast.makeText(
                    this,
                    "Vui lòng đăng nhập",
                    Toast.LENGTH_SHORT
            ).show();

            return;

        }

        cartList =
                dbHelper.getCartItems(
                        userId
                );

        adapter =
                new CartAdapter(

                        this,

                        cartList,

                        () -> updateSubtotal()

                );

        recyclerCart.setLayoutManager(

                new LinearLayoutManager(
                        this
                )

        );

        recyclerCart.setAdapter(
                adapter
        );

        // cập nhật tổng tiền
        updateSubtotal();

    }

    /**
     * Xử lý click nút đặt hàng
     * Flow:
     *  1. Lấy thông tin user (tên, email, sđt) từ DB
     *  2. Tạo đơn hàng + order_items + xóa giỏ
     *  3. Hiển thị thông tin đơn hàng vừa tạo
     */
    private void setupEvents() {

        btnOrder.setOnClickListener(v -> {

            if (cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy thông tin user
            String[] userInfo = dbHelper.getUserById(userId);
            if (userInfo == null) {
                Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            String customerName  = userInfo[0];
            String customerEmail = userInfo[1];
            String customerPhone = (userInfo[2] != null) ? userInfo[2] : "Chưa cập nhật";

            // Tính tổng tiền
            double subtotal = 0;
            for (com.example.btl_ltuddd.model.Cart item : cartList) {
                subtotal += item.getProductPrice() * item.getQuantity();
            }
            double ship  = 15000;
            double total = subtotal + ship;

            // Gọi DB: tạo đơn + lưu items + xóa giỏ (transaction)
            long orderId = dbHelper.placeOrder(userId, cartList, subtotal, total);

            if (orderId == -1) {
                Toast.makeText(this, "Đặt hàng thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo mã đơn theo format app
            String orderCode = "#FH-" + String.format("%04d", orderId);

            // Build thông báo chi tiết
            StringBuilder detail = new StringBuilder();
            detail.append("✅ Đặt hàng thành công!\n\n");
            detail.append("📋 Mã đơn: ").append(orderCode).append("\n");
            detail.append("👤 Khách hàng: ").append(customerName).append("\n");
            detail.append("📧 Email: ").append(customerEmail).append("\n");
            detail.append("📞 SĐT: ").append(customerPhone).append("\n\n");
            detail.append("🛒 Sản phẩm (").append(cartList.size()).append(" loại):\n");

            for (com.example.btl_ltuddd.model.Cart item : cartList) {
                detail.append("  • ").append(item.getProductName())
                        .append(" x").append(item.getQuantity())
                        .append(" = ").append(String.format("%,.0fđ", item.getProductPrice() * item.getQuantity()))
                        .append("\n");
            }

            detail.append("\n💰 Tiền hàng: ").append(String.format("%,.0fđ", subtotal));
            detail.append("\n🚚 Phí ship: ").append(String.format("%,.0fđ", ship));
            detail.append("\n💳 Tổng cộng: ").append(String.format("%,.0fđ", total));

            // Hiển thị dialog xác nhận
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Chi tiết đơn hàng")
                    .setMessage(detail.toString())
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Reload giỏ (đã bị xóa) và cập nhật UI
                        cartList.clear();
                        adapter.notifyDataSetChanged();
                        updateSubtotal();
                    })
                    .setCancelable(false)
                    .show();
        });

    }

    /**
     * Điều hướng Bottom Navigation
     */
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

    /**
     * Tính tổng tiền
     *
     * subtotal = tiền hàng
     * ship = phí ship
     * total = subtotal + ship
     */
    /**
     * Tính tiền giỏ hàng
     *
     * txtSubtotal = tổng tiền sản phẩm
     * txtTotal = subtotal + 15k ship
     */
    private void updateSubtotal() {

        // Tổng tiền sản phẩm
        double subtotal = 0;

        for (
                Cart item :
                cartList
        ) {

            subtotal +=

                    item.getProductPrice()

                            *

                            item.getQuantity();

        }

        // Phí ship cố định
        double ship = 15000;

        // Tổng cuối
        double total =
                subtotal
                        +
                        ship;

        // Hiển thị subtotal
        txtSubtotal.setText(

                String.format(
                        "%,.0fđ",
                        subtotal
                )

        );

        // Hiển thị total
        txtTotal.setText(

                String.format(
                        "%,.0fđ",
                        total
                )

        );

    }

    /**
     * Khi quay lại màn hình
     * reload giỏ
     */
    @Override
    protected void onResume() {

        super.onResume();

        loadCart();

    }

}