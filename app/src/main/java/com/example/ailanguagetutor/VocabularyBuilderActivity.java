package com.example.ailanguagetutor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VocabularyBuilderActivity extends AppCompatActivity {

    // Integrate the provided OpenRouter API Key
    private static final String OPENROUTER_API_KEY = "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";
    private static final String SITE_URL = "https://ailanguagetutor.example.com"; // Optional, for OpenRouter rankings
    private static final String APP_NAME = "AI Language Tutor"; // Optional

    private LinearLayout vocabListLayout;
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

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

        cardGreetings.setOnClickListener(v -> generateVocabulary("Greetings", "HSK 1"));
        cardFood.setOnClickListener(v -> generateVocabulary("Food & Dining", "HSK 2"));
        cardShopping.setOnClickListener(v -> generateVocabulary("Shopping", "HSK 2"));
        cardTravel.setOnClickListener(v -> generateVocabulary("Travel", "HSK 3"));
        cardNumbers.setOnClickListener(v -> generateVocabulary("Numbers", "HSK 1"));
    }

    private void generateVocabulary(String category, String level) {
        if (OPENROUTER_API_KEY.equals("YOUR_OPENROUTER_API_KEY_HERE")) {
            Toast.makeText(this, "Please set your OpenRouter API Key in VocabularyBuilderActivity.java", Toast.LENGTH_LONG).show();
            return;
        }

        // Show loading dialog
        AlertDialog loadingDialog = new AlertDialog.Builder(this)
                .setTitle("Generating Vocabulary")
                .setMessage("Creating a word list for " + category + " (" + level + ")...")
                .setCancelable(false)
                .create();
        loadingDialog.show();

        String prompt = "Generate 5 Chinese words related to '" + category + "' suitable for " + level + " level. " +
                        "Format the output strictly as a list where each line is: " +
                        "Hanzi | Pinyin | English Meaning | Example Sentence (Chinese)";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "mistralai/mistral-7b-instruct:free"); 
            
            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messages.put(message);
            
            jsonBody.put("messages", messages);
        } catch (JSONException e) {
            e.printStackTrace();
            loadingDialog.dismiss();
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + OPENROUTER_API_KEY)
                .addHeader("HTTP-Referer", SITE_URL)
                .addHeader("X-Title", APP_NAME)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    Toast.makeText(VocabularyBuilderActivity.this, "Network error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        loadingDialog.dismiss();
                        Toast.makeText(VocabularyBuilderActivity.this, "API Error: " + response.code(), Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                try {
                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);
                    JSONArray choices = jsonResponse.getJSONArray("choices");
                    JSONObject firstChoice = choices.getJSONObject(0);
                    JSONObject message = firstChoice.getJSONObject("message");
                    String content = message.getString("content");

                    runOnUiThread(() -> {
                        loadingDialog.dismiss();
                        showVocabularyDialog(category, content);
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        loadingDialog.dismiss();
                        Toast.makeText(VocabularyBuilderActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void showVocabularyDialog(String title, String content) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("Close", null)
                .show();
    }
}