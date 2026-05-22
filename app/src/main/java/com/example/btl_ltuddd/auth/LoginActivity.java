package com.example.btl_ltuddd.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.client.dashboard.ClientActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;

public class  LoginActivity extends AppCompatActivity {

    private EditText edtEmailOrPhone, edtPassword;
    private Button btnSignIn;
    private ImageButton btnTogglePassword;
    private TextView tvForgotPassword, tvSignUp;
    private CardView btnGoogle, btnFacebook;
    private boolean isPasswordVisible = false;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Nếu đã login rồi thì vào thẳng
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        long savedId = prefs.getLong("userId", -1);
        if (savedId != -1)  {
            navigateToClient();
            return;
        }

        setContentView(R.layout.activity_login);
        dbHelper = DatabaseHelper.getInstance(this);
        initViews();
        setupListeners();
    }

    private void initViews() {
        edtEmailOrPhone  = findViewById(R.id.edtEmailOrPhone);
        edtPassword      = findViewById(R.id.edtPassword);
        btnSignIn        = findViewById(R.id.btnSignIn);
        btnTogglePassword= findViewById(R.id.btnTogglePassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp         = findViewById(R.id.tvSignUp);
        btnGoogle        = findViewById(R.id.btnGoogle);
        btnFacebook      = findViewById(R.id.btnFacebook);
    }

    private void setupListeners() {
        btnTogglePassword.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            edtPassword.setInputType(isPasswordVisible
                    ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edtPassword.setSelection(edtPassword.getText().length());
        });

        btnSignIn.setOnClickListener(v -> attemptLogin());
        tvSignUp.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show());
        btnGoogle.setOnClickListener(v ->
                Toast.makeText(this, "Đang phát triển", Toast.LENGTH_SHORT).show());
        btnFacebook.setOnClickListener(v ->
                Toast.makeText(this, "Đang phát triển", Toast.LENGTH_SHORT).show());
    }

    private void attemptLogin() {
        String email    = edtEmailOrPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        long userId = dbHelper.loginUser(email, password);
        if (userId != -1) {
            // Lưu session bằng SharedPreferences
            getSharedPreferences("auth", MODE_PRIVATE).edit()
                    .putLong("userId", userId)
                    .apply();
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            navigateToClient();
        } else {
            Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToClient() {
        Intent intent = new Intent(this, ClientActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}