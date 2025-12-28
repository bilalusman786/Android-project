package com.example.ailanguagetutor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
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
        LinearLayout btnGoogle = findViewById(R.id.btnGoogle);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
        
        btnGoogle.setOnClickListener(v -> 
            Toast.makeText(this, "Google Sign In clicked", Toast.LENGTH_SHORT).show()
        );

        tvForgotPassword.setOnClickListener(v -> 
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
        );
    }
}