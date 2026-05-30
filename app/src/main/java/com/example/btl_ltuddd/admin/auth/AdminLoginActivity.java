package com.example.btl_ltuddd.admin.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.admin.AdminActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText edtEmailOrId, edtPassword;
    private Button btnLogin;
    private TextView tvGoRegister, tvForgotPwd;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra phiên đăng nhập hiện tại
        SharedPreferences prefs = getSharedPreferences("admin_auth", MODE_PRIVATE);
        if (prefs.getLong("adminId", -1) != -1) {
            goToDashboard();
            return;
        }

        setContentView(R.layout.activity_admin_login);
        db = DatabaseHelper.getInstance(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        edtEmailOrId = findViewById(R.id.edtAdminEmailOrId);
        edtPassword  = findViewById(R.id.edtAdminPassword);
        btnLogin     = findViewById(R.id.btnAdminLogin);
        tvGoRegister = findViewById(R.id.tvGoToAdminRegister);
        tvForgotPwd  = findViewById(R.id.tvForgotAdminPwd);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> doLogin());

        tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, AdminRegisterActivity.class))
        );

        tvForgotPwd.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng đang được cập nhật", Toast.LENGTH_SHORT).show();
            // Xử lý logic quên mật khẩu ở đây
        });
    }

    private void doLogin() {
        String id  = edtEmailOrId.getText().toString().trim();
        String pwd = edtPassword.getText().toString();

        if (TextUtils.isEmpty(id)) {
            edtEmailOrId.setError("Vui lòng nhập Email hoặc ID");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        long adminId = db.loginAdmin(id, pwd);
        if (adminId == -1) {
            Toast.makeText(this, "Thông tin đăng nhập không chính xác!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu session vào SharedPreferences
        getSharedPreferences("admin_auth", MODE_PRIVATE)
                .edit()
                .putLong("adminId", adminId)
                .apply();

        goToDashboard();
    }

    private void goToDashboard() {
        startActivity(new Intent(this, AdminActivity.class));
        finish();
    }
}