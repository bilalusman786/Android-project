package com.example.ailanguagetutor;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PronunciationTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronunciation_test);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());
        
        FrameLayout btnRecord = findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(v -> {
            Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show();
            // Simulate recording stop after 2 seconds
            btnRecord.postDelayed(() -> 
                Toast.makeText(this, "Recording stopped. Analyzing...", Toast.LENGTH_SHORT).show(), 
            2000);
        });
    }
}