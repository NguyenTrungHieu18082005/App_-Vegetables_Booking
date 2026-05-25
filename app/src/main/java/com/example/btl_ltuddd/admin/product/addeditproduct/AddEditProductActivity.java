package com.example.btl_ltuddd.admin.product.addeditproduct;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Product;

public class AddEditProductActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "product_id";
    private static final int NO_ID = -1;

    private DatabaseHelper dbHelper;

    // Views
    private ImageView ivProductImage;
    private TextView tvImageHint, tvImageSub;
    private EditText etName, etPrice, etUnit, etStock, etDescription;
    private Spinner spinnerCategory;
    private Switch switchVisible;

    // State
    private Uri selectedImageUri = null;
    private String selectedCategory = "Rau củ quả";
    private boolean isEdit = false;
    private int productId = NO_ID;

    private final String[] CATEGORIES = {
            "Rau củ quả", "Trái cây", "Thảo mộc", "Hữu cơ", "Khác"
    };

    // Launcher chọn ảnh từ thư viện
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();

                            // Xin quyền persistent để URI không bị mất
                            getContentResolver().takePersistableUriPermission(
                                    selectedImageUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );

                            ivProductImage.setImageURI(selectedImageUri);
                            ivProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            tvImageHint.setVisibility(View.GONE);
                            tvImageSub.setVisibility(View.GONE);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        dbHelper = DatabaseHelper.getInstance(this);

        bindViews();
        setupToolbar();
        setupCategorySpinner();
        setupImagePicker();

        // Kiểm tra mode: Edit hay Add
        productId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, NO_ID);
        isEdit = productId != NO_ID;

        if (isEdit) {
            // Load data sản phẩm vào form
            Product p = dbHelper.getProductById(productId);
            if (p != null) fillForm(p);
        }

        findViewById(R.id.btn_save).setOnClickListener(v -> saveProduct());
    }

    private void bindViews() {
        ivProductImage  = findViewById(R.id.iv_product_image);
        tvImageHint     = findViewById(R.id.tv_image_hint);
        tvImageSub      = findViewById(R.id.tv_image_sub);
        etName          = findViewById(R.id.et_name);
        etPrice         = findViewById(R.id.et_price);
        etUnit          = findViewById(R.id.et_unit);
        etStock         = findViewById(R.id.et_stock);
        etDescription   = findViewById(R.id.et_description);
        spinnerCategory = findViewById(R.id.spinner_category);
        switchVisible   = findViewById(R.id.switch_visible);
    }

    private void setupToolbar() {
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(isEdit ? "Sửa sản phẩm" : "Thêm sản phẩm mới");
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, CATEGORIES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedCategory = CATEGORIES[pos];
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupImagePicker() {
        findViewById(R.id.layout_image_picker).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); // đổi từ ACTION_PICK
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImageLauncher.launch(intent);
        });
    }

    private void fillForm(Product p) {
        etName.setText(p.getName());
        etPrice.setText(String.valueOf((int) p.getPrice()));
        etStock.setText(String.valueOf(p.getStock()));
        etUnit.setText(p.getUnit());
        etDescription.setText(p.getDescription());
        switchVisible.setChecked(p.isVisible());

        // Set spinner đúng category
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i].equals(p.getCategory())) {
                spinnerCategory.setSelection(i);
                break;
            }
        }

        // Load ảnh nếu có
        if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
            try {
                selectedImageUri = Uri.parse(p.getImageUrl());
                ivProductImage.setImageURI(selectedImageUri);
                ivProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                tvImageHint.setVisibility(View.GONE);
                tvImageSub.setVisibility(View.GONE);
            } catch (Exception ignored) {}
        }
    }

    private void saveProduct() {
        String name  = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String stock = etStock.getText().toString().trim();
        String unit  = etUnit.getText().toString().trim();
        String desc  = etDescription.getText().toString().trim();
        boolean visible = switchVisible.isChecked();
        String imageUrl = selectedImageUri != null ? selectedImageUri.toString() : "";

        // Validate
        if (name.isEmpty()) {
            etName.setError("Vui lòng nhập tên sản phẩm");
            etName.requestFocus();
            return;
        }
        if (price.isEmpty()) {
            etPrice.setError("Vui lòng nhập giá");
            etPrice.requestFocus();
            return;
        }
        if (stock.isEmpty()) {
            etStock.setError("Vui lòng nhập số lượng");
            etStock.requestFocus();
            return;
        }

        try {
            double p = Double.parseDouble(price);
            int s    = Integer.parseInt(stock);

            if (isEdit) {
                dbHelper.updateProduct(productId, name, p, desc,
                        selectedCategory, imageUrl, s, unit, visible);
                Toast.makeText(this, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addProduct(name, p, desc,
                        selectedCategory, imageUrl, s, unit, visible);
                Toast.makeText(this, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show();
            }

            setResult(RESULT_OK);
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá hoặc số lượng không hợp lệ",
                    Toast.LENGTH_SHORT).show();
        }
    }
}