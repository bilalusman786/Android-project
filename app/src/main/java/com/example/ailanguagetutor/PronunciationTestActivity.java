package com.example.ailanguagetutor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class PronunciationTestActivity extends AppCompatActivity implements PhraseAdapter.OnPhraseClickListener {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    // API Key for OpenRouter
    private static final String OPENROUTER_API_KEY = "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";

    private RecyclerView phrasesRecyclerView;
    private TextView tvSelectedHanzi, tvSelectedPinyin, tvUserTranscript, tvAiFeedback;
    private MaterialButton btnRecord;
    private View cardAiFeedback;

    private SpeechRecognizer speechRecognizer;
    private OkHttpClient client = new OkHttpClient();
    private Phrase currentPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronunciation_test);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        tvSelectedHanzi = findViewById(R.id.tvSelectedHanzi);
        tvSelectedPinyin = findViewById(R.id.tvSelectedPinyin);
        tvUserTranscript = findViewById(R.id.tvUserTranscript);
        tvAiFeedback = findViewById(R.id.tvAiFeedback);
        btnRecord = findViewById(R.id.btnRecord);
        cardAiFeedback = findViewById(R.id.cardAiFeedback);

        setupPhraseList();

        btnRecord.setOnClickListener(v -> {
            if (currentPhrase == null) {
                Toast.makeText(this, "Please select a phrase first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            } else {
                startListening();
            }
        });

        initializeSpeechRecognizer();
    }

    private void setupPhraseList() {
        List<Phrase> phrases = new ArrayList<>();
        phrases.add(new Phrase("你好", "nǐ hǎo", "Hello"));
        phrases.add(new Phrase("谢谢", "xièxiè", "Thank you"));
        phrases.add(new Phrase("不客气", "bù kèqì", "You're welcome"));
        phrases.add(new Phrase("对不起", "duìbuqǐ", "I'm sorry"));
        phrases.add(new Phrase("早上好", "zǎoshang hǎo", "Good morning"));

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
        tvUserTranscript.setText("Your pronunciation will appear here...");
        cardAiFeedback.setVisibility(View.GONE);
    }

    private void initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                btnRecord.setText("Listening...");
                btnRecord.setIconResource(R.drawable.ic_mic_active);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String userText = matches.get(0);
                    tvUserTranscript.setText(userText);
                    analyzePronunciation(currentPhrase.getHanzi(), userText);
                }
                btnRecord.setText("Tap to Record");
                btnRecord.setIconResource(R.drawable.ic_mic);
            }

            @Override
            public void onError(int error) {
                Toast.makeText(PronunciationTestActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                btnRecord.setText("Tap to Record");
                btnRecord.setIconResource(R.drawable.ic_mic);
            }

            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-CN");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the phrase now");
        speechRecognizer.startListening(intent);
    }

    private void analyzePronunciation(String correctPhrase, String userPhrase) {
        cardAiFeedback.setVisibility(View.VISIBLE);
        tvAiFeedback.setText("Analyzing...");

        // Improved prompt for detailed feedback
        String prompt = String.format(
            "You are a Chinese language pronunciation coach. The user is trying to say: '%s'. " +
            "The speech-to-text recognized their attempt as: '%s'. " +
            "Please provide an evaluation with a score out of 100, and a word-by-word breakdown of their pronunciation. " +
            "For each character in the original phrase, comment on the tones and clarity. Keep feedback concise and encouraging. " +
            "Format your response strictly as: \n" +
            "Overall Score: [score]/100\n\n" +
            "Word-by-word Feedback:\n" +
            "- [Character 1]: [Feedback on Character 1]\n" +
            "- [Character 2]: [Feedback on Character 2]\n" +
            "... \n\n" +
            "Quick Tip: [A single actionable tip]",
            correctPhrase, userPhrase
        );

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
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 runOnUiThread(() -> tvAiFeedback.setText("Network Error"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> tvAiFeedback.setText("API Error: " + response.code()));
                    return;
                }
                try {
                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);
                    String content = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    runOnUiThread(() -> tvAiFeedback.setText(content));
                } catch (JSONException e) {
                     runOnUiThread(() -> tvAiFeedback.setText("Parsing Error"));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
