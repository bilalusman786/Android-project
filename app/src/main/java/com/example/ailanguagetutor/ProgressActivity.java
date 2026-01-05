package com.example.ailanguagetutor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        // Load stats from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("LinguaAiPrefs", MODE_PRIVATE);
        
        // Retrieve dynamic values (defaults used if not found)
        int wordsLearned = prefs.getInt("words_learned", 0);
        int accuracy = prefs.getInt("accuracy", 85); // Default to 85% for demo
        float speakingScore = prefs.getFloat("speaking_score", 7.5f); // Default score
        int dayStreak = prefs.getInt("day_streak", 1); // Default streak

        // Find TextViews
        TextView tvWordsLearned = findViewById(R.id.tvWordsLearned);
        TextView tvAccuracy = findViewById(R.id.tvAccuracy);
        TextView tvSpeakingScore = findViewById(R.id.tvSpeakingScore);
        TextView tvDayStreak = findViewById(R.id.tvDayStreak);

        // Set values
        tvWordsLearned.setText(String.valueOf(wordsLearned));
        tvAccuracy.setText(accuracy + "%");
        tvSpeakingScore.setText(String.format("%.1f/10", speakingScore));
        tvDayStreak.setText(dayStreak + " Days");
    }
}