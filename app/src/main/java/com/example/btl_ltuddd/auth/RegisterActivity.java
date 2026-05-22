package com.example.btl_ltuddd.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtFullName, edtPhone, edtEmail, edtPassword;
    private CheckBox checkboxTerms;
    private Button btnSignUp;
    private TextView tvLogin;
    private ImageButton btnTogglePassword;
    private boolean isPasswordVisible = false;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Xóa session cũ khi vào trang đăng ký
        getSharedPreferences("auth", MODE_PRIVATE).edit().clear().apply();

        setContentView(R.layout.activity_register);

        dbHelper = DatabaseHelper.getInstance(this);

        edtFullName       = findViewById(R.id.edtFullName);
        edtPhone          = findViewById(R.id.edtPhone);
        edtEmail          = findViewById(R.id.edtEmail);
        edtPassword       = findViewById(R.id.edtPassword);
        checkboxTerms     = findViewById(R.id.checkboxTerms);
        btnSignUp         = findViewById(R.id.btnSignUp);
        tvLogin           = findViewById(R.id.tvLogin);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);

        styleTermsText();
        btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        btnSignUp.setOnClickListener(v -> attemptRegister());
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void styleTermsText() {
        TextView tvTerms = findViewById(R.id.tvTerms);
        String fullText = "I agree to the Terms of Service and Privacy Policy.";
        SpannableString spannable = new SpannableString(fullText);
        int s1 = fullText.indexOf("Terms of Service");
        spannable.setSpan(new StyleSpan(Typeface.BOLD), s1, s1 + 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int s2 = fullText.indexOf("Privacy Policy");
        spannable.setSpan(new StyleSpan(Typeface.BOLD), s2, s2 + 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTerms.setText(spannable);
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        edtPassword.setInputType(isPasswordVisible
                ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtPassword.setSelection(edtPassword.getText().length());
    }

    private void attemptRegister() {
        String fullName = edtFullName.getText().toString().trim();
        String phone    = edtPhone.getText().toString().trim();
        String email    = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Validate
        if (fullName.isEmpty()) { edtFullName.setError("Vui lòng nhập họ tên"); edtFullName.requestFocus(); return; }
        if (phone.isEmpty())    { edtPhone.setError("Vui lòng nhập số điện thoại"); edtPhone.requestFocus(); return; }
        if (email.isEmpty())    { edtEmail.setError("Vui lòng nhập email"); edtEmail.requestFocus(); return; }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ"); edtEmail.requestFocus(); return;
        }
        if (password.length() < 8) { edtPassword.setError("Mật khẩu tối thiểu 8 ký tự"); edtPassword.requestFocus(); return; }
        if (!checkboxTerms.isChecked()) {
            Toast.makeText(this, "Vui lòng đồng ý với Điều khoản dịch vụ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu vào SQLite
        long result = dbHelper.registerUser(fullName, phone, email, password);
        if (result != -1) {
            Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Email đã tồn tại, vui lòng dùng email khác.", Toast.LENGTH_SHORT).show();
        }
    }
}
