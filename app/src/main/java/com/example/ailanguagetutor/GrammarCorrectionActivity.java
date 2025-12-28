package com.example.ailanguagetutor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GrammarCorrectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_correction);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());
        
        EditText etInput = findViewById(R.id.etInput);
        Button btnCheckGrammar = findViewById(R.id.btnCheckGrammar);
        
        btnCheckGrammar.setOnClickListener(v -> {
            String text = etInput.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(this, "Please enter a sentence first.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Checking grammar for: " + text, Toast.LENGTH_SHORT).show();
                // Simulation of result
                Toast.makeText(this, "No errors found! Great job!", Toast.LENGTH_LONG).show();
            }
        });
    }
}