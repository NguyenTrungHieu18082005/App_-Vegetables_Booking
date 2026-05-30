package com.example.btl_ltuddd.admin.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.database.DatabaseHelper;

public class AdminRegisterActivity extends AppCompatActivity {

    private EditText edtFullName, edtStaffId, edtEmail, edtPassword;
    private Button btnRegister;
    private TextView tvGoLogin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        db = DatabaseHelper.getInstance(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        edtFullName  = findViewById(R.id.edtAdminFullName);
        edtStaffId   = findViewById(R.id.edtAdminStaffId);
        edtEmail     = findViewById(R.id.edtAdminEmail);
        edtPassword  = findViewById(R.id.edtAdminTempPassword);
        btnRegister  = findViewById(R.id.btnCompleteRegistration);
        tvGoLogin    = findViewById(R.id.tvGoToAdminLogin);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> doRegister());

        tvGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
        });
    }

    private void doRegister() {
        String fullName = edtFullName.getText().toString().trim();
        String staffId  = edtStaffId.getText().toString().trim();
        String email    = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

        // Validate dữ liệu
        if (TextUtils.isEmpty(fullName)) {
            edtFullName.setError("Nhập họ tên"); return;
        }
        if (TextUtils.isEmpty(staffId)) {
            edtStaffId.setError("Nhập ID nhân viên"); return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ"); return;
        }
        if (password.length() < 6) {
            edtPassword.setError("Mật khẩu phải từ 6 ký tự"); return;
        }

        // Gọi hàm registerAdmin từ DatabaseHelper
        // Tham số: fullName, staffId, department, email, password, accessLevel
        // Ở đây ta dùng giá trị mặc định cho dept và level vì UI không có nhập
        long result = db.registerAdmin(fullName, staffId, "Chưa xác định", email, password, "staff");

        if (result != -1) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            // Chuyển về màn hình đăng nhập
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại! ID hoặc Email đã tồn tại.", Toast.LENGTH_LONG).show();
        }
    }
}