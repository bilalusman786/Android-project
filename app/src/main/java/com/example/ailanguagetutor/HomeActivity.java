package com.example.ailanguagetutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MaterialCardView cardLanguage = findViewById(R.id.cardLanguage);
        MaterialCardView cardPronunciation = findViewById(R.id.cardPronunciation);
        MaterialCardView cardVocab = findViewById(R.id.cardVocab);
        MaterialCardView cardGrammar = findViewById(R.id.cardGrammar);
        MaterialCardView cardNotepad = findViewById(R.id.cardNotepad);
        // Note: cardProgress and cardProfile are removed from the grid in the updated XML
        // as they are now in the bottom navigation.

        // Apply entry animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down);
        
        setupCard(cardLanguage, animation, v -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            startActivity(intent);
        });

        setupCard(cardPronunciation, animation, v -> {
            Intent intent = new Intent(HomeActivity.this, PronunciationTestActivity.class);
            startActivity(intent);
        });

        setupCard(cardVocab, animation, v -> {
            Intent intent = new Intent(HomeActivity.this, VocabularyBuilderActivity.class);
            startActivity(intent);
        });

        setupCard(cardGrammar, animation, v -> {
            Intent intent = new Intent(HomeActivity.this, GrammarCorrectionActivity.class);
            startActivity(intent);
        });

        setupCard(cardNotepad, animation, v -> {
            Intent intent = new Intent(HomeActivity.this, NotepadActivity.class);
            startActivity(intent);
        });
        
        // Also animate the stats card
        MaterialCardView cardStats = findViewById(R.id.cardStats);
        if (cardStats != null) {
            Animation statsAnim = AnimationUtils.loadAnimation(this, R.anim.slide_fade_in);
            cardStats.startAnimation(statsAnim);
        }

        // Setup Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_home); // Set Home as selected
        
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // We are already here
                return true;
            } else if (itemId == R.id.nav_chat) {
                startActivity(new Intent(HomeActivity.this, ChatActivity.class));
                return true;
            } else if (itemId == R.id.nav_progress) {
                startActivity(new Intent(HomeActivity.this, ProgressActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
        
        // Setup Continue Button
        View btnContinue = findViewById(R.id.btnContinueLearning);
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                // Default action: Go to Vocab builder as a good daily task
                Intent intent = new Intent(HomeActivity.this, VocabularyBuilderActivity.class);
                startActivity(intent);
            });
        }
    }

    private void setupCard(View card, Animation entryAnimation, View.OnClickListener listener) {
        if (card != null) {
            card.startAnimation(entryAnimation);
            card.setOnClickListener(listener);
            setTouchAnimation(card);
        }
    }

    private void setTouchAnimation(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false; // let the click listener handle the actual click
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHomeStats();
        updateUserInfo();
        
        // Reset bottom nav selection to Home when returning
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    private void updateUserInfo() {
        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = userPrefs.getString("userName", "User");
        String profileImageUri = userPrefs.getString("profileImage", null);
        
        TextView tvUserName = findViewById(R.id.tvUserName);
        if (tvUserName != null) {
            tvUserName.setText(userName);
        }

        ImageView ivProfile = findViewById(R.id.ivProfile);
        if (profileImageUri != null && ivProfile != null) {
            try {
                ivProfile.setImageURI(Uri.parse(profileImageUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateHomeStats() {
        SharedPreferences prefs = getSharedPreferences("LinguaAiPrefs", MODE_PRIVATE);
        
        int wordsLearned = prefs.getInt("words_learned", 0);
        int streak = prefs.getInt("day_streak", 1); 
        int dailyGoal = 10;
        
        TextView tvStreakValue = findViewById(R.id.tvStreakValue);
        TextView tvGoalValue = findViewById(R.id.tvGoalValue);
        TextView tvProgressPercentage = findViewById(R.id.tvProgressPercentage);
        CircularProgressIndicator circularProgress = findViewById(R.id.circularProgress);

        if (tvStreakValue != null) {
            tvStreakValue.setText(String.valueOf(streak));
        }

        if (tvGoalValue != null) {
            tvGoalValue.setText(wordsLearned + "/" + dailyGoal);
        }
        
        if (circularProgress != null) {
            // Calculate percentage
            int percentage = Math.min(100, (int)((float)wordsLearned / dailyGoal * 100));
            circularProgress.setProgress(percentage);
            
            if (tvProgressPercentage != null) {
                tvProgressPercentage.setText(percentage + "%");
            }
        }
    }
}