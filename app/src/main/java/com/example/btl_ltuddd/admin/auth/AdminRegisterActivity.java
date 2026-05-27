package com.example.btl_ltuddd.admin.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.admin.AdminActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;

public class AdminRegisterActivity extends AppCompatActivity {

    private EditText   edtFullName, edtStaffId, edtEmail, edtPassword;
    private Spinner    spinnerDept;
    private ImageView  btnTogglePwd;
    private LinearLayout btnManager, btnStaff;
    private Button     btnRegister;
    private TextView   tvGoLogin;

    private boolean showPwd      = false;
    private String  accessLevel  = "manager"; // default selected
    private DatabaseHelper db;

    private static final String[] DEPARTMENTS = {
            "Select Dept", "Management", "Operations",
            "Logistics", "Sales", "IT", "Finance", "HR"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        db = DatabaseHelper.getInstance(this);

        edtFullName  = findViewById(R.id.edtAdminFullName);
        edtStaffId   = findViewById(R.id.edtAdminStaffId);
        edtEmail     = findViewById(R.id.edtAdminEmail);
        edtPassword  = findViewById(R.id.edtAdminTempPassword);
        spinnerDept  = findViewById(R.id.spinnerDept);
        btnTogglePwd = findViewById(R.id.btnToggleRegPwd);
        btnManager   = findViewById(R.id.btnAccessManager);
        btnStaff     = findViewById(R.id.btnAccessStaff);
        btnRegister  = findViewById(R.id.btnCompleteRegistration);
        tvGoLogin    = findViewById(R.id.tvGoToAdminLogin);

        setupSpinner();
        setupAccessLevel();

        btnTogglePwd.setOnClickListener(v -> {
            showPwd = !showPwd;
            edtPassword.setInputType(showPwd
                    ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edtPassword.setSelection(edtPassword.length());
        });

        btnRegister.setOnClickListener(v -> doRegister());
        tvGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
        });
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, DEPARTMENTS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDept.setAdapter(adapter);
    }

    private void setupAccessLevel() {
        // Manager = mặc định active (màu xanh lá)
        updateAccessUI("manager");

        btnManager.setOnClickListener(v -> updateAccessUI("manager"));
        btnStaff.setOnClickListener(v -> updateAccessUI("staff"));
    }

    private void updateAccessUI(String level) {
        accessLevel = level;
        if ("manager".equals(level)) {
            btnManager.setBackgroundColor(0xFF9DDB6E);
            btnStaff.setBackgroundColor(0xFFEEEEEE);
        } else {
            btnStaff.setBackgroundColor(0xFF9DDB6E);
            btnManager.setBackgroundColor(0xFFEEEEEE);
        }
    }

    private void doRegister() {
        String fullName = edtFullName.getText().toString().trim();
        String staffId  = edtStaffId.getText().toString().trim();
        String email    = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String dept     = spinnerDept.getSelectedItem().toString();

        if (TextUtils.isEmpty(fullName)) {
            edtFullName.setError("Nhập họ tên"); edtFullName.requestFocus(); return;
        }
        if (TextUtils.isEmpty(staffId)) {
            edtStaffId.setError("Nhập Staff ID"); edtStaffId.requestFocus(); return;
        }
        if (spinnerDept.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Vui lòng chọn phòng ban", Toast.LENGTH_SHORT).show(); return;
        }
        if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ"); edtEmail.requestFocus(); return;
        }
        if (password.length() < 6) {
            edtPassword.setError("Mật khẩu ít nhất 6 ký tự"); edtPassword.requestFocus(); return;
        }

        long adminId = db.registerAdmin(fullName, staffId, dept,
                email.isEmpty() ? null : email, password, accessLevel);

        if (adminId == -1) {
            Toast.makeText(this, "Staff ID hoặc Email đã tồn tại!", Toast.LENGTH_LONG).show();
            return;
        }

        // Lưu session luôn sau khi đăng ký
        getSharedPreferences("admin_auth", MODE_PRIVATE)
                .edit().putLong("adminId", adminId).apply();

        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, AdminActivity.class));
        finish();
    }
}