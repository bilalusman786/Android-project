package com.example.ailanguagetutor;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class VocabularyBuilderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_builder);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());
        
        CardView cardGreetings = findViewById(R.id.cardGreetings);
        CardView cardFood = findViewById(R.id.cardFood);
        CardView cardShopping = findViewById(R.id.cardShopping);
        CardView cardTravel = findViewById(R.id.cardTravel);
        CardView cardNumbers = findViewById(R.id.cardNumbers);

        cardGreetings.setOnClickListener(v -> Toast.makeText(this, "Opening Greetings...", Toast.LENGTH_SHORT).show());
        cardFood.setOnClickListener(v -> Toast.makeText(this, "Opening Food & Dining...", Toast.LENGTH_SHORT).show());
        cardShopping.setOnClickListener(v -> Toast.makeText(this, "Opening Shopping...", Toast.LENGTH_SHORT).show());
        cardTravel.setOnClickListener(v -> Toast.makeText(this, "Opening Travel...", Toast.LENGTH_SHORT).show());
        cardNumbers.setOnClickListener(v -> Toast.makeText(this, "Opening Numbers...", Toast.LENGTH_SHORT).show());
    }
}