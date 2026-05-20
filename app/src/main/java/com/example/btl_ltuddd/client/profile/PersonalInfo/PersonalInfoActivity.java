package com.example.btl_ltuddd.client.profile.PersonalInfo;


import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;

public class PersonalInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        Button btnSave = findViewById(R.id.btnSaveInfo);
        btnSave.setOnClickListener(v ->
                Toast.makeText(this, "Đã lưu thông tin!", Toast.LENGTH_SHORT).show()
        );
    }
}