package com.example.ailanguagetutor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());
        
        MaterialButton btnSignOut = findViewById(R.id.btnSignOut);
        // Note: I need to add an ID to the Sign Out button in XML first, assuming I missed it or used a generic one.
        // Let's check activity_profile.xml. It didn't have an ID for the sign out button.
        // I should have added one. I will do it now by guessing or rewriting the file if needed.
        // Actually, I can just update the XML quickly.
        // But for now, let's just find it by traversing or assume I will update the XML next.
        // Wait, I can't find by ID if it doesn't have one.
        // I will update activity_profile.xml to add android:id="@+id/btnSignOut" to the last button.
    }
}