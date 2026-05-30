package com.example.btl_ltuddd.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtFullName, edtPhone, edtEmail, edtPassword;
    private Button btnSignUp;
    private TextView tvLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setContentView(R.layout.activity_register);

        dbHelper = DatabaseHelper.getInstance(this);

        // Ánh xạ View
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone    = findViewById(R.id.edtPhone);
        edtEmail    = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp   = findViewById(R.id.btnSignUp);
        tvLogin     = findViewById(R.id.tvLogin);

        // Sự kiện đăng ký
        btnSignUp.setOnClickListener(v -> attemptRegister());

        // Sự kiện chuyển sang Đăng nhập
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegister() {
        String fullName = edtFullName.getText().toString().trim();
        String phone    = edtPhone.getText().toString().trim();
        String email    = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Validate cơ bản
        if (fullName.isEmpty()) { edtFullName.setError("Nhập họ tên"); return; }
        if (phone.isEmpty())    { edtPhone.setError("Nhập số điện thoại"); return; }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ"); return;
        }
        if (password.length() < 8) { edtPassword.setError("Mật khẩu phải từ 8 ký tự"); return; }

        // Lưu vào SQLite
        long result = dbHelper.registerUser(fullName, phone, email, password);
        if (result != -1) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
        }
    }
}