package com.example.btl_ltuddd.client.profile.PersonalInfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.User;

public class PersonalInfoActivity extends AppCompatActivity {

    private EditText edtFullName, edtPhone, edtEmail, edtNewPassword, edtConfirmPassword;
    private ImageView btnToggleNewPwd, btnToggleConfirmPwd;
    private Button btnSaveInfo;

    private DatabaseHelper dbHelper;
    private long userId;

    private boolean showNewPwd     = false;
    private boolean showConfirmPwd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        dbHelper = DatabaseHelper.getInstance(this);

        // Lấy userId từ SharedPreferences (cùng key với LoginActivity)
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        userId = prefs.getLong("userId", -1);

        initViews();
        loadUserData();
        setupListeners();
    }

    private void initViews() {
        edtFullName        = findViewById(R.id.edtFullName);
        edtPhone           = findViewById(R.id.edtPhone);
        edtEmail           = findViewById(R.id.edtEmail);
        edtNewPassword     = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnToggleNewPwd    = findViewById(R.id.btnToggleNewPwd);
        btnToggleConfirmPwd= findViewById(R.id.btnToggleConfirmPwd);
        btnSaveInfo        = findViewById(R.id.btnSaveInfo);
    }

    /** Load dữ liệu user từ DB và hiển thị lên các field */
    private void loadUserData() {
        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        User user = dbHelper.getUserById(userId);
        if (user == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        edtFullName.setText(user.getFullname());
        edtPhone.setText(user.getPhone() != null ? user.getPhone() : "");
        edtEmail.setText(user.getEmail());
    }

    private void setupListeners() {
        // Nút Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Toggle hiển thị mật khẩu mới
        btnToggleNewPwd.setOnClickListener(v -> {
            showNewPwd = !showNewPwd;
            edtNewPassword.setInputType(showNewPwd
                    ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edtNewPassword.setSelection(edtNewPassword.length());
        });

        // Toggle hiển thị xác nhận mật khẩu
        btnToggleConfirmPwd.setOnClickListener(v -> {
            showConfirmPwd = !showConfirmPwd;
            edtConfirmPassword.setInputType(showConfirmPwd
                    ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edtConfirmPassword.setSelection(edtConfirmPassword.length());
        });

        // Lưu thay đổi
        btnSaveInfo.setOnClickListener(v -> saveUserInfo());
    }

    private void saveUserInfo() {
        String fullname = edtFullName.getText().toString().trim();
        String phone    = edtPhone.getText().toString().trim();
        String email    = edtEmail.getText().toString().trim();
        String newPwd   = edtNewPassword.getText().toString();
        String confPwd  = edtConfirmPassword.getText().toString();

        // --- Validate ---
        if (TextUtils.isEmpty(fullname)) {
            edtFullName.setError("Vui lòng nhập họ tên");
            edtFullName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        // Validate mật khẩu chỉ khi người dùng nhập vào
        String passwordToSave = null;
        if (!TextUtils.isEmpty(newPwd) || !TextUtils.isEmpty(confPwd)) {
            if (newPwd.length() < 6) {
                edtNewPassword.setError("Mật khẩu phải ít nhất 6 ký tự");
                edtNewPassword.requestFocus();
                return;
            }
            if (!newPwd.equals(confPwd)) {
                edtConfirmPassword.setError("Xác nhận mật khẩu không khớp");
                edtConfirmPassword.requestFocus();
                return;
            }
            passwordToSave = newPwd;
        }

        // --- Cập nhật DB ---
        int rows = dbHelper.updateUserInfo(userId, fullname, phone, email, passwordToSave);
        if (rows > 0) {
            Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
            // Xóa field mật khẩu sau khi lưu
            edtNewPassword.setText("");
            edtConfirmPassword.setText("");
        } else {
            Toast.makeText(this, "Cập nhật thất bại. Email có thể đã được dùng!", Toast.LENGTH_LONG).show();
        }
    }
}