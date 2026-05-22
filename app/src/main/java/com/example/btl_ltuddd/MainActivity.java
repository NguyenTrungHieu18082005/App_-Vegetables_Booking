package com.example.btl_ltuddd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_ltuddd.auth.RegisterActivity;
import com.example.btl_ltuddd.client.dashboard.ClientActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    Button btnClient1, btnClient2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Thêm dòng này để mở DB ngay khi app khởi động
        DatabaseHelper.getInstance(this).getWritableDatabase();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ánh xạ button từ XML
        btnClient1 = findViewById(R.id.btnClient1);
        btnClient2 = findViewById(R.id.btnClient2);

//        // Sự kiện click button 1
//        btnClient1.setOnClickListener(v -> {
//
//            Intent intent = new Intent(
//                    MainActivity.this,
//                    ClientActivity.class
//            );
//
//            startActivity(intent);
//
//        });

        // Sự kiện click button 2
        btnClient1.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);

        });


    }
}