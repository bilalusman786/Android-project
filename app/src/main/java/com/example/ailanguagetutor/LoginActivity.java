package com.example.ailanguagetutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnSignIn = findViewById(R.id.btnSignIn);
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);

        // Simulate successful login
        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verify credentials from SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String savedPassword = prefs.getString("password_" + email, null);

            if (savedPassword != null && savedPassword.equals(password)) {
                // Login successful
                String savedName = prefs.getString("name_" + email, "User");
                
                // Update current user session
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userName", savedName);
                editor.putString("userEmail", email);
                editor.apply();

                Toast.makeText(this, "Welcome back, " + savedName + "!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate to Signup screen
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}