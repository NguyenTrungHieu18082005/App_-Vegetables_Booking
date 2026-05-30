package com.example.btl_ltuddd.admin.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.admin.AdminActivity;
import com.example.btl_ltuddd.admin.order.AdminOrderActivity;
import com.example.btl_ltuddd.admin.product.addeditproduct.AddEditProductActivity;
import com.example.btl_ltuddd.admin.profile.AdminProfileActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class AdminProductActivity extends AppCompatActivity implements ProductAdapter.OnProductActionListener {

    private DatabaseHelper dbHelper;
    private ProductAdapter adapter;

    private final ActivityResultLauncher<Intent> addEditLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    refreshData();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        dbHelper = DatabaseHelper.getInstance(this);

        // Khởi tạo các View từ XML
        EditText etSearch = findViewById(R.id.et_search);
        FloatingActionButton fab = findViewById(R.id.fab_add_product);
        RecyclerView rv = findViewById(R.id.rv_products);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, dbHelper.getAllProducts(), this);
        rv.setAdapter(adapter);

        // Xử lý tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String q = s.toString().trim();
                adapter.updateList(q.isEmpty() ? dbHelper.getAllProducts() : dbHelper.searchProducts(q));
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditProductActivity.class);
            addEditLauncher.launch(intent);
        });

        setupBottomNav();
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_categories);
        bottomNav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, AdminActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_orders) {
                startActivity(new Intent(this, AdminOrderActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, AdminProfileActivity.class));
                finish();
                return true;
            }
            return id == R.id.nav_categories;
        });
    }

    private void refreshData() {
        adapter.updateList(dbHelper.getAllProducts());
    }

    @Override
    public void onEdit(Product product) {
        Intent intent = new Intent(this, AddEditProductActivity.class);
        intent.putExtra(AddEditProductActivity.EXTRA_PRODUCT_ID, product.getId());
        addEditLauncher.launch(intent);
    }

    @Override
    public void onDelete(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa \"" + product.getName() + "\"?")
                .setPositiveButton("Xóa", (d, w) -> {
                    if (dbHelper.deleteProduct(product.getId())) {
                        refreshData();
                        Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}