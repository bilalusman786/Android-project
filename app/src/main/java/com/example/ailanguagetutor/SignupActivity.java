package com.example.ailanguagetutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText etFullName = findViewById(R.id.etFullName);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        TextView tvSignIn = findViewById(R.id.tvSignIn);

        btnCreateAccount.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            if (fullName.isEmpty()) {
                Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the user's name
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("userName", fullName);
            editor.apply();

            Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();
            
            // Navigate to HomeActivity
            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        tvSignIn.setOnClickListener(v -> {
            finish(); // Go back to LoginActivity
        });
    }
}