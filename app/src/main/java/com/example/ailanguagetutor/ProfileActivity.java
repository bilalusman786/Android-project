package com.example.ailanguagetutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ProfileActivity extends BaseActivity {

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
        
        // Handle Edit Profile Button
        MaterialButton btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
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
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Language Setting
        findViewById(R.id.setting_language).setOnClickListener(v -> showLanguageDialog());

        // Help & Support Setting
        findViewById(R.id.setting_help).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SupportActivity.class);
            startActivity(intent);
        });

        // Set dynamic user info
        setUserInfo();
    }

    private void showLanguageDialog() {
        final String[] languages = {"English", "Español", "中文"};
        final String[] languageCodes = {"en", "es", "zh"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Language");
        builder.setItems(languages, (dialog, which) -> {
            setLocale(languageCodes[which]);
        });
        builder.show();
    }

    private void setLocale(String lang) {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("My_Lang", lang);
        editor.apply();
        
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserInfo();
    }

    private void setUserInfo() {
        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = userPrefs.getString("userName", "User");
        String userEmail = userPrefs.getString("userEmail", "alex.chen@email.com"); 
        String profileImageUri = userPrefs.getString("profileImage", null);

        TextView tvProfileName = findViewById(R.id.tvProfileName);
        TextView tvProfileEmail = findViewById(R.id.tvProfileEmail);
        ImageView ivProfileImage = findViewById(R.id.ivProfileImage);
        ImageView ivProfilePlaceholder = findViewById(R.id.ivProfilePlaceholder);

        tvProfileName.setText(userName);
        tvProfileEmail.setText(userEmail);
        
        if (profileImageUri != null) {
            ivProfileImage.setImageURI(Uri.parse(profileImageUri));
            ivProfilePlaceholder.setVisibility(View.GONE);
        } else {
            ivProfilePlaceholder.setVisibility(View.VISIBLE);
        }
    }
}