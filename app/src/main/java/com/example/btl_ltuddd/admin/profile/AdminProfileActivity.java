package com.example.btl_ltuddd.admin.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.admin.auth.AdminLoginActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.AdminAccount;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.btl_ltuddd.admin.AdminActivity;

import com.example.btl_ltuddd.admin.order.AdminOrderActivity;

import com.example.btl_ltuddd.admin.product.AdminProductActivity;

public class AdminProfileActivity
        extends AppCompatActivity {
    private TextView tvName;
    private TextView tvStaffId;
    private TextView tvEmail;

    private Button btnLogout;
    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile_activity);

        initViews();

        loadAdminInfo();

        setupLogout();

        setupBottomNav();

    }

    /**
     * Ánh xạ view
     */
    private void initViews() {

        tvName =
                findViewById(
                        R.id.tvAdminProfileName
                );



        tvStaffId =
                findViewById(
                        R.id.tvAdminStaffId
                );

        tvEmail =
                findViewById(
                        R.id.tvAdminEmail
                );


        btnLogout =
                findViewById(
                        R.id.btnAdminLogout
                );

    }

    /**
     * Load thông tin admin
     */
    private void loadAdminInfo() {

        SharedPreferences prefs =

                getSharedPreferences(
                        "admin_auth",
                        MODE_PRIVATE
                );

        long adminId =
                prefs.getLong(
                        "adminId",
                        -1
                );

        if (
                adminId == -1
        ) {

            startActivity(

                    new Intent(
                            this,
                            AdminLoginActivity.class
                    )

            );

            finish();

            return;

        }

        AdminAccount admin =

                DatabaseHelper
                        .getInstance(this)
                        .getAdminById(adminId);

        if (
                admin == null
        ) {

            return;

        }

        tvName.setText(
                admin.getFullName()
        );



        tvStaffId.setText(
                admin.getStaffId()
        );

        tvEmail.setText(
                admin.getEmail()
        );


    }

    /**
     * Đăng xuất
     */
    private void setupLogout() {

        btnLogout.setOnClickListener(v -> {

            SharedPreferences prefs =

                    getSharedPreferences(
                            "admin_auth",
                            MODE_PRIVATE
                    );

            prefs
                    .edit()
                    .clear()
                    .apply();

            Intent intent =

                    new Intent(
                            this,
                            AdminLoginActivity.class
                    );

            intent.setFlags(

                    Intent.FLAG_ACTIVITY_NEW_TASK
                            |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK

            );

            startActivity(
                    intent
            );

            finish();

        });

    }


    //Chuyển trang
    private void setupBottomNav() {

        BottomNavigationView bottomNav =
                findViewById(R.id.admin_bottom_nav);

        if (bottomNav == null) {
            return;
        }

        bottomNav.setSelectedItemId(
                R.id.nav_profile
        );

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {

                startActivity(
                        new Intent(
                                this,
                                AdminActivity.class
                        )
                );

                finish();

                return true;

            }

            else if (id == R.id.nav_categories) {

                startActivity(
                        new Intent(
                                this,
                                AdminProductActivity.class
                        )
                );

                finish();

                return true;

            }

            else if (id == R.id.nav_orders) {

                startActivity(
                        new Intent(
                                this,
                                AdminOrderActivity.class
                        )
                );

                finish();

                return true;

            }

            return true;

        });

    }

}