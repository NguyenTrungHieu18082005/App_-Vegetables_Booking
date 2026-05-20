package com.example.btl_ltuddd.client.profile.PersonalAddress;


import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_ltuddd.R;

public class AddressActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        Button btnSave = findViewById(R.id.btnSaveAddress);
        btnSave.setOnClickListener(v ->
                Toast.makeText(this, "Đã lưu địa chỉ!", Toast.LENGTH_SHORT).show()
        );
    }
}
