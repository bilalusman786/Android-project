package com.example.ailanguagetutor;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PronunciationTestActivity extends BaseActivity implements PhraseAdapter.OnPhraseClickListener {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    // API Key for OpenRouter
    private static final String OPENROUTER_API_KEY = "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";

    private RecyclerView phrasesRecyclerView;
    private TextView tvSelectedHanzi, tvSelectedPinyin;
    private MaterialButton btnSpeak, btnGenerateSentence;
    private TextView tvGeneratedSentence, tvGeneratedPinyin, tvTranslateSentence, tvTranslatedSentence;
    private ImageView btnSpeakSentence;
    
    private TextToSpeech tts;
    private OkHttpClient client = new OkHttpClient();
    private Phrase currentPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronunciation_test);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        tvSelectedHanzi = findViewById(R.id.tvSelectedHanzi);
        tvSelectedPinyin = findViewById(R.id.tvSelectedPinyin);
        btnSpeak = findViewById(R.id.btnSpeak);
        
        btnGenerateSentence = findViewById(R.id.btnGenerateSentence);
        tvGeneratedSentence = findViewById(R.id.tvGeneratedSentence);
        btnSpeakSentence = findViewById(R.id.btnSpeakSentence);
        tvGeneratedPinyin = findViewById(R.id.tvGeneratedPinyin);
        tvTranslateSentence = findViewById(R.id.tvTranslateSentence);
        tvTranslatedSentence = findViewById(R.id.tvTranslatedSentence);

        setupPhraseList();
        initializeTTS();

        btnSpeak.setOnClickListener(v -> {
            if (currentPhrase != null && tts != null) {
                tts.speak(currentPhrase.getHanzi(), TextToSpeech.QUEUE_FLUSH, null, null);
                // Simulate speaking practice boosting score
                updateSpeakingScore();
            }
        });
        
        btnGenerateSentence.setOnClickListener(v -> {
            if (currentPhrase != null) {
                generateSentence(currentPhrase.getHanzi());
            } else {
                 Toast.makeText(this, "Please select a phrase first", Toast.LENGTH_SHORT).show();
            }
        });

        btnSpeakSentence.setOnClickListener(v -> {
            String sentence = tvGeneratedSentence.getText().toString();
            if (!sentence.isEmpty() && tts != null) {
                tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        
        tvTranslateSentence.setOnClickListener(v -> {
            if (tvTranslatedSentence.getVisibility() == View.VISIBLE) {
                tvTranslatedSentence.setVisibility(View.GONE);
                tvTranslateSentence.setText("Translate");
            } else {
                tvTranslatedSentence.setVisibility(View.VISIBLE);
                tvTranslateSentence.setText("Hide Translation");
            }
        });
    }

    private void updateSpeakingScore() {
        SharedPreferences prefs = getSharedPreferences("LinguaAiPrefs", MODE_PRIVATE);
        float currentScore = prefs.getFloat("speaking_score", 7.5f);
        // Slowly increase score up to 10.0
        if (currentScore < 10.0f) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat("speaking_score", Math.min(10.0f, currentScore + 0.1f));
            editor.apply();
        }
    }

    private void setupPhraseList() {
        List<Phrase> phrases = new ArrayList<>();
        // Basic Greetings
        phrases.add(new Phrase("你好", "nǐ hǎo", "Hello"));
        phrases.add(new Phrase("谢谢", "xièxiè", "Thank you"));
        phrases.add(new Phrase("不客气", "bù kèqì", "You're welcome"));
        phrases.add(new Phrase("对不起", "duìbuqǐ", "I'm sorry"));
        phrases.add(new Phrase("早上好", "zǎoshang hǎo", "Good morning"));
        phrases.add(new Phrase("再见", "zàijiàn", "Goodbye"));
        phrases.add(new Phrase("晚上好", "wǎnshang hǎo", "Good evening"));
        
        // Polite Expressions
        phrases.add(new Phrase("请问", "qǐngwèn", "Excuse me / May I ask"));
        phrases.add(new Phrase("没关系", "méi guānxi", "It doesn't matter"));
        phrases.add(new Phrase("是", "shì", "Yes"));
        phrases.add(new Phrase("不是", "bú shì", "No"));
        phrases.add(new Phrase("好的", "hǎo de", "OK / Good"));
        
        // People & Pronouns
        phrases.add(new Phrase("我", "wǒ", "I / Me"));
        phrases.add(new Phrase("你", "nǐ", "You"));
        phrases.add(new Phrase("他", "tā", "He / Him"));
        phrases.add(new Phrase("她", "tā", "She / Her"));
        phrases.add(new Phrase("我们", "wǒmen", "We / Us"));
        phrases.add(new Phrase("朋友", "péngyou", "Friend"));
        phrases.add(new Phrase("老师", "lǎoshī", "Teacher"));
        phrases.add(new Phrase("学生", "xuésheng", "Student"));
        
        // Daily Life
        phrases.add(new Phrase("喝水", "hē shuǐ", "Drink water"));
        phrases.add(new Phrase("吃饭", "chī fàn", "Eat rice / Have a meal"));
        phrases.add(new Phrase("面条", "miàntiáo", "Noodles"));
        phrases.add(new Phrase("苹果", "píngguǒ", "Apple"));
        phrases.add(new Phrase("茶", "chá", "Tea"));
        phrases.add(new Phrase("咖啡", "kāfēi", "Coffee"));
        phrases.add(new Phrase("快乐", "kuàilè", "Happy"));
        phrases.add(new Phrase("喜欢", "xǐhuan", "Like"));
        phrases.add(new Phrase("爱", "ài", "Love"));
        
        // Time
        phrases.add(new Phrase("今天", "jīntiān", "Today"));
        phrases.add(new Phrase("明天", "míngtiān", "Tomorrow"));
        phrases.add(new Phrase("昨天", "zuótiān", "Yesterday"));
        phrases.add(new Phrase("现在", "xiànzài", "Now"));
        phrases.add(new Phrase("几点", "jǐ diǎn", "What time"));
        
        // Shopping & Questions
        phrases.add(new Phrase("多少钱", "duōshao qián", "How much money"));
        phrases.add(new Phrase("太贵了", "tài guì le", "Too expensive"));
        phrases.add(new Phrase("便宜", "piányi", "Cheap"));
        phrases.add(new Phrase("哪里", "nǎli", "Where"));
        phrases.add(new Phrase("什么", "shénme", "What"));
        
        // Adjectives & Descriptions
        phrases.add(new Phrase("名字", "míngzi", "Name"));
        phrases.add(new Phrase("高兴", "gāoxìng", "Happy / Glad"));
        phrases.add(new Phrase("认识", "rènshi", "Know / Recognize"));
        phrases.add(new Phrase("漂亮", "piàoliang", "Beautiful"));
        phrases.add(new Phrase("帅", "shuài", "Handsome"));
        
        // Weather
        phrases.add(new Phrase("天气", "tiānqì", "Weather"));
        phrases.add(new Phrase("热", "rè", "Hot"));
        phrases.add(new Phrase("冷", "lěng", "Cold"));
        phrases.add(new Phrase("下雨", "xià yǔ", "Rain"));
        
        // Actions
        phrases.add(new Phrase("学习", "xuéxí", "Study"));
        phrases.add(new Phrase("工作", "gōngzuò", "Work"));
        phrases.add(new Phrase("休息", "xiūxi", "Rest"));
        phrases.add(new Phrase("睡觉", "shuìjiào", "Sleep"));
        phrases.add(new Phrase("起床", "qǐchuáng", "Get up"));
        phrases.add(new Phrase("坐", "zuò", "Sit"));
        phrases.add(new Phrase("走", "zǒu", "Walk / Go"));

        // Instead of a RecyclerView, we use a dialog for simplicity for now
        tvSelectedHanzi.setOnClickListener(v -> showPhraseSelectionDialog(phrases));
        // Set a default phrase
        onPhraseClick(phrases.get(0));
    }
    
    private void showPhraseSelectionDialog(List<Phrase> phrases) {
        String[] items = new String[phrases.size()];
        for (int i = 0; i < phrases.size(); i++) {
            items[i] = phrases.get(i).getHanzi() + " (" + phrases.get(i).getEnglish() + ")";
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Select a Phrase")
                .setItems(items, (dialog, which) -> onPhraseClick(phrases.get(which)))
                .show();
    }

    @Override
    public void onPhraseClick(Phrase phrase) {
        this.currentPhrase = phrase;
        tvSelectedHanzi.setText(phrase.getHanzi());
        tvSelectedPinyin.setText(phrase.getPinyin());
        
        // Reset generated UI
        tvGeneratedSentence.setVisibility(View.GONE);
        btnSpeakSentence.setVisibility(View.GONE);
        tvGeneratedPinyin.setVisibility(View.GONE);
        tvTranslateSentence.setVisibility(View.GONE);
        tvTranslatedSentence.setVisibility(View.GONE);
    }

    private void initializeTTS() {
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
    }

    private void generateSentence(String word) {
        // Show loading state
        tvGeneratedSentence.setVisibility(View.VISIBLE);
        tvGeneratedSentence.setText("Generating sentence...");
        btnGenerateSentence.setEnabled(false);

        String prompt = "Create a simple Chinese sentence using the word '" + word + "'. " +
                        "Provide the output in the format: [Chinese Sentence] ||| [Pinyin] ||| [English Translation]";

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
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + OPENROUTER_API_KEY)
                .addHeader("HTTP-Referer", "https://ailanguagetutor.example.com") 
                .addHeader("X-Title", "AI Language Tutor")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 runOnUiThread(() -> {
                     tvGeneratedSentence.setText("Network Error");
                     btnGenerateSentence.setEnabled(true);
                 });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        tvGeneratedSentence.setText("API Error: " + response.code());
                        btnGenerateSentence.setEnabled(true);
                    });
                    return;
                }
                try {
                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);
                    String content = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    
                    runOnUiThread(() -> parseAndDisplaySentence(content));
                    
                } catch (JSONException e) {
                     runOnUiThread(() -> {
                         tvGeneratedSentence.setText("Parsing Error");
                         btnGenerateSentence.setEnabled(true);
                     });
                }
            }
        });
    }
    
    private void parseAndDisplaySentence(String content) {
        btnGenerateSentence.setEnabled(true);
        // Expected format: Chinese ||| Pinyin ||| English
        String[] parts = content.split("\\|\\|\\|");
        
        if (parts.length >= 3) {
            tvGeneratedSentence.setText(parts[0].trim());
            tvGeneratedSentence.setVisibility(View.VISIBLE);
            btnSpeakSentence.setVisibility(View.VISIBLE);
            
            tvGeneratedPinyin.setText(parts[1].trim());
            tvGeneratedPinyin.setVisibility(View.VISIBLE);
            
            tvTranslatedSentence.setText(parts[2].trim());
            tvTranslateSentence.setVisibility(View.VISIBLE);
            tvTranslateSentence.setText("Translate");
            tvTranslatedSentence.setVisibility(View.GONE);
        } else {
            // Fallback if format is weird
            tvGeneratedSentence.setText(content);
            tvGeneratedSentence.setVisibility(View.VISIBLE);
            btnSpeakSentence.setVisibility(View.VISIBLE);
        }
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