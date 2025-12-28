package com.example.ailanguagetutor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CardView cardLanguage = findViewById(R.id.cardLanguage);
        CardView cardPronunciation = findViewById(R.id.cardPronunciation);
        CardView cardVocab = findViewById(R.id.cardVocab);
        CardView cardGrammar = findViewById(R.id.cardGrammar);
        CardView cardProgress = findViewById(R.id.cardProgress);
        CardView cardProfile = findViewById(R.id.cardProfile);

        // For "Select Language" (Placeholder, maybe re-show language selection or go to settings?)
        cardLanguage.setOnClickListener(v -> {
            // No specific screen for this yet, maybe just show a toast or nothing.
            // Or maybe it goes to a language selection screen. 
            // For now, I'll link it to Vocabulary as a placeholder or nothing.
        });

        cardPronunciation.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PronunciationTestActivity.class);
            startActivity(intent);
        });

        cardVocab.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, VocabularyBuilderActivity.class);
            startActivity(intent);
        });

        cardGrammar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GrammarCorrectionActivity.class);
            startActivity(intent);
        });

        cardProgress.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProgressActivity.class);
            startActivity(intent);
        });

        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}