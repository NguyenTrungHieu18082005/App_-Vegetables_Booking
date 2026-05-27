package com.example.btl_ltuddd.admin.auth;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.admin.AdminActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText   edtEmailOrId, edtPassword;
    private ImageView  btnTogglePwd;
    private Button     btnLogin;
    private TextView   tvGoRegister;
    private boolean    showPwd = false;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nếu đã login admin rồi → vào thẳng dashboard
        SharedPreferences prefs = getSharedPreferences("admin_auth", MODE_PRIVATE);
        if (prefs.getLong("adminId", -1) != -1) {
            goToDashboard();
            return;
        }

        setContentView(R.layout.activity_admin_login);
        db = DatabaseHelper.getInstance(this);

        edtEmailOrId = findViewById(R.id.edtAdminEmailOrId);
        edtPassword  = findViewById(R.id.edtAdminPassword);
        btnTogglePwd = findViewById(R.id.btnToggleAdminPwd);
        btnLogin     = findViewById(R.id.btnAdminLogin);
        tvGoRegister = findViewById(R.id.tvGoToAdminRegister);

        btnTogglePwd.setOnClickListener(v -> {
            showPwd = !showPwd;
            edtPassword.setInputType(showPwd
                    ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edtPassword.setSelection(edtPassword.length());
        });

        btnLogin.setOnClickListener(v -> doLogin());

        tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, AdminRegisterActivity.class)));
    }

    private void doLogin() {
        String id  = edtEmailOrId.getText().toString().trim();
        String pwd = edtPassword.getText().toString();

        if (TextUtils.isEmpty(id)) {
            edtEmailOrId.setError("Nhập email hoặc Staff ID");
            edtEmailOrId.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            edtPassword.setError("Nhập mật khẩu");
            edtPassword.requestFocus();
            return;
        }

        long adminId = db.loginAdmin(id, pwd);
        if (adminId == -1) {
            Toast.makeText(this, "Email/ID hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu session
        getSharedPreferences("admin_auth", MODE_PRIVATE)
                .edit().putLong("adminId", adminId).apply();

        goToDashboard();
    }

    private void goToDashboard() {
        startActivity(new Intent(this, AdminActivity.class));
        finish();
    }
}