package com.example.ailanguagetutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ProfileActivity extends AppCompatActivity {

    private SwitchMaterial switchDarkMode;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());
        
        MaterialButton btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(v -> {
            // In a real app, clear session data here
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();
            // Go to login screen
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Dark Mode Logic
        switchDarkMode = findViewById(R.id.switchDarkMode);
        
        // Initialize switch state based on saved preference
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        switchDarkMode.setChecked(isDarkMode);

        // Set listener to handle toggle events
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the new state
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isDarkMode", isChecked);
            editor.apply();

            // Apply the theme change
            if (isChecked) {
                // When button is ON (checked), theme should be DARK
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                // When button is OFF (unchecked), theme should be LIGHT
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }
}