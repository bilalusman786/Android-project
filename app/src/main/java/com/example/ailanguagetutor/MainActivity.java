package com.example.ailanguagetutor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Apply animations
        TextView title = findViewById(R.id.tvTitle);
        TextView subtitle = findViewById(R.id.tvSubtitle);
        // Note: activity_main.xml layout doesn't perfectly match the generic example provided.
        // It has ivLogo, tvTitle, tvSubtitle, and a LinearLayout with dots.
        // I'll try to animate what's there. 
        // Assuming you might want to animate the logo or title.
        
        if (title != null) {
             Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_fade_in);
             title.startAnimation(animation);
        }
        
        if (subtitle != null) {
             Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_fade_in);
             subtitle.startAnimation(animation);
        }

        // Delay for 3 seconds and then move to LoginActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}