package com.example.ailanguagetutor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GrammarCorrectionActivity extends AppCompatActivity {

    // Updated with the new API Key provided
    private static final String API_KEY = "AIzaSyA2eF-FQ2QxLa6tRgQILExWCu9rlQmOW0g";

    private CardView cardResult;
    private TextView tvResult;
    private ProgressBar progressBar;
    private EditText etInput;
    private Button btnCheckGrammar;

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
        if (API_KEY.equals("YOUR_API_KEY_HERE")) {
            Toast.makeText(this, "Please set your API Key in GrammarCorrectionActivity.java", Toast.LENGTH_LONG).show();
            return;
        }

        // Show loading state
        btnCheckGrammar.setEnabled(false);
        cardResult.setVisibility(View.VISIBLE);
        tvResult.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // Initialize Gemini
        // Using "gemini-1.5-flash" as the standard model for the new API key
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", API_KEY);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        // Updated prompt to ask for specific format
        String prompt = "Check the following text for grammar errors. If there are errors, identify the mistake first, and then provide the correct sentence. Use the format:\n" +
                        "Mistake: [Description of mistake]\n" +
                        "Correction: [Corrected sentence]\n" +
                        "If there are no errors, just say 'No errors found'.\n\n" +
                        "Text: " + text;

        Content content = new Content.Builder()
                .addText(prompt)
                .build();

        Executor executor = Executors.newSingleThreadExecutor();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvResult.setVisibility(View.VISIBLE);
                    tvResult.setText(resultText);
                    btnCheckGrammar.setEnabled(true);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvResult.setVisibility(View.VISIBLE);
                    String errorMsg = t.getMessage();
                    if (errorMsg != null && errorMsg.contains("404")) {
                         tvResult.setText("Error: Model not found (404). \n\nThis usually means the API key is restricted or the API is not enabled in Google Cloud Console.\n\nRaw Error: " + errorMsg);
                    } else {
                         tvResult.setText("Error checking grammar: " + errorMsg);
                    }
                    btnCheckGrammar.setEnabled(true);
                    t.printStackTrace();
                });
            }
        }, executor);
    }
}