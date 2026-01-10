package com.example.ailanguagetutor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GrammarCorrectionActivity extends BaseActivity {

    private static final String API_KEY = "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";
    private static final String SITE_URL = "https://ailanguagetutor.example.com"; // Optional
    private static final String APP_NAME = "AI Language Tutor"; // Optional

    private CardView cardResult;
    private TextView tvResult;
    private ProgressBar progressBar;
    private EditText etInput;
    private Button btnCheckGrammar;
    private TextToSpeech tts;
    private ImageView ivPlayAudio;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_correction);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());
        
        etInput = findViewById(R.id.etInput);
        btnCheckGrammar = findViewById(R.id.btnCheckGrammar);
        cardResult = findViewById(R.id.cardResult);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);
        ivPlayAudio = findViewById(R.id.ivPlayAudio);

        // Initialize TTS
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.CHINESE);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Chinese language not supported on this device", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "TTS Initialization failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Play audio listener
        if (ivPlayAudio != null) {
            ivPlayAudio.setOnClickListener(v -> {
                 String textToSpeak = tvResult.getText().toString();
                 if (!textToSpeak.isEmpty() && tts != null) {
                     tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                 }
            });
        }

        btnCheckGrammar.setOnClickListener(v -> {
            String text = etInput.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(this, "Please enter a sentence first.", Toast.LENGTH_SHORT).show();
            } else {
                performGrammarCheck(text);
            }
        });
    }

    private void performGrammarCheck(String text) {
        // Show loading state
        btnCheckGrammar.setEnabled(false);
        cardResult.setVisibility(View.VISIBLE);
        tvResult.setVisibility(View.GONE);
        if (ivPlayAudio != null) ivPlayAudio.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        String prompt = "Check the following text for grammar errors. If there are errors, identify the mistake first, and then provide the correct sentence. Use the format:\n" +
                        "Mistake: [Description of mistake]\n" +
                        "Correction: [Corrected sentence]\n" +
                        "If there are no errors, just say 'No errors found'.\n\n" +
                        "Text: " + text;

        JSONObject jsonBody = new JSONObject();
        try {
            // Switched to a stable and popular free model on OpenRouter
            jsonBody.put("model", "mistralai/mistral-7b-instruct:free");
            
            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messages.put(message);
            
            jsonBody.put("messages", messages);
        } catch (JSONException e) {
            e.printStackTrace();
            resetUIOnError("JSON Error");
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("HTTP-Referer", SITE_URL)
                .addHeader("X-Title", APP_NAME)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> resetUIOnError("Network error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    runOnUiThread(() -> resetUIOnError("API Error " + response.code() + ": " + errorBody));
                    return;
                }

                try {
                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);
                    JSONArray choices = jsonResponse.getJSONArray("choices");
                    if (choices.length() > 0) {
                        JSONObject firstChoice = choices.getJSONObject(0);
                        JSONObject message = firstChoice.getJSONObject("message");
                        String content = message.getString("content");

                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            tvResult.setVisibility(View.VISIBLE);
                            if (ivPlayAudio != null) ivPlayAudio.setVisibility(View.VISIBLE);
                            tvResult.setText(content);
                            btnCheckGrammar.setEnabled(true);
                            
                            // Increase accuracy slightly as user practices
                            updateAccuracyProgress();
                            
                            // Also set click listener on text for TTS as fallback
                            tvResult.setOnClickListener(v -> {
                                 if (tts != null) {
                                     tts.speak(content, TextToSpeech.QUEUE_FLUSH, null, null);
                                 }
                            });
                        });
                    } else {
                         runOnUiThread(() -> resetUIOnError("No response content found"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> resetUIOnError("Parsing error"));
                }
            }
        });
    }
    
    private void updateAccuracyProgress() {
        SharedPreferences prefs = getSharedPreferences("LinguaAiPrefs", MODE_PRIVATE);
        int currentAccuracy = prefs.getInt("accuracy", 85);
        // Cap accuracy at 100
        if (currentAccuracy < 100) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("accuracy", currentAccuracy + 1);
            editor.apply();
        }
    }

    private void resetUIOnError(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        tvResult.setVisibility(View.VISIBLE);
        tvResult.setText(errorMessage);
        btnCheckGrammar.setEnabled(true);
    }
    
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}