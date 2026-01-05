package com.example.ailanguagetutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;

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

        // Repurposing "Select Language" card to open the AI Roleplay Chat
        cardLanguage.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        updateHomeStats();
        updateUserName();
    }

    private void updateUserName() {
        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = userPrefs.getString("userName", "User");
        
        TextView tvUserName = findViewById(R.id.tvUserName);
        tvUserName.setText(userName);
    }

    private void updateHomeStats() {
        SharedPreferences prefs = getSharedPreferences("LinguaAiPrefs", MODE_PRIVATE);
        
        // Retrieve dynamic values
        int wordsLearned = prefs.getInt("words_learned", 0);
        // Note: In a real app, 'streak' might be calculated based on daily activity dates.
        // For this demo, we can just use the 'messages_sent' as a proxy for engagement or keep the static 'day_streak'.
        // Let's use 'words_learned' as today's goal progress for demonstration.
        
        TextView tvStreakValue = findViewById(R.id.tvStreakValue);
        TextView tvGoalValue = findViewById(R.id.tvGoalValue);

        // Just an example mapping:
        // Streak comes from prefs
        int streak = prefs.getInt("day_streak", 1); 
        tvStreakValue.setText(String.valueOf(streak));

        // Goal: Let's say the goal is 10 words. We show wordsLearned / 10
        // Or if you want "Words Practiced" to be exactly 'words_learned'
        tvGoalValue.setText(String.valueOf(wordsLearned));
    }
}