package com.example.ailanguagetutor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private static final String OPENROUTER_API_KEY = "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";
    // Switched to a more standard/available model ID
    private static final String MODEL_ID = "mistralai/mistral-7b-instruct:free";

    private RecyclerView chatRecyclerView;
    private EditText etChatMessage;
    private FloatingActionButton btnSendMessage;

    private List<ChatMessage> messageList;
    private ChatAdapter chatAdapter;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        etChatMessage = findViewById(R.id.etChatMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

        btnSendMessage.setOnClickListener(v -> sendMessage());

        // Add initial greeting from the AI
        addMessage("你好! Let's practice. What would you like to talk about?", ChatMessage.Sender.MODEL);
    }

    private void sendMessage() {
        String messageText = etChatMessage.getText().toString().trim();
        if (messageText.isEmpty()) {
            return;
        }

        // Add user message to UI
        addMessage(messageText, ChatMessage.Sender.USER);
        etChatMessage.setText("");

        // Update stats
        updateMessagesSentCounter();

        // Get AI response
        getAiResponse(messageText);
    }

    private void updateMessagesSentCounter() {
        SharedPreferences prefs = getSharedPreferences("LinguaAiPrefs", MODE_PRIVATE);
        int currentCount = prefs.getInt("messages_sent", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("messages_sent", currentCount + 1);
        editor.apply();
    }

    private void addMessage(String message, ChatMessage.Sender sender) {
        messageList.add(new ChatMessage(message, sender));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void getAiResponse(String userMessage) {
        // Add a placeholder for the AI response
        addMessage("Typing...", ChatMessage.Sender.MODEL);

        // Updated prompt to request translation separated by |||
        String prompt = "You are a friendly Chinese language tutor. The user said: '" + userMessage + "'. " +
                        "Respond naturally in Chinese. Then provide the English translation at the end, separated by '|||'. " +
                        "Example format: [Chinese response]|||[English translation]. " +
                        "Keep your response to one or two short sentences.";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", MODEL_ID);
            JSONArray messages = new JSONArray();
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "user"); 
            systemMessage.put("content", prompt);
            messages.put(systemMessage);
            
            jsonBody.put("messages", messages);
        } catch (JSONException e) {
            e.printStackTrace();
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
                Log.e("ChatActivity", "API Call Failed", e);
                updateLastMessage("Sorry, network error: " + e.getMessage(), null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "No details";
                    Log.e("ChatActivity", "API Error: " + response.code() + " " + errorBody);
                    
                    String errorMessage = "API error: " + response.code();
                    if (response.code() == 401) {
                         errorMessage = "Invalid API Key. Please update OPENROUTER_API_KEY in ChatActivity.java";
                    } else if (response.code() == 404) {
                        errorMessage = "Model not found/available. Please try again later.";
                    } else {
                        try {
                            JSONObject errorJson = new JSONObject(errorBody);
                            if (errorJson.has("error")) {
                                JSONObject errorObj = errorJson.getJSONObject("error");
                                errorMessage += "\n" + errorObj.optString("message", "Unknown error");
                            }
                        } catch (JSONException e) {
                             if (errorBody.length() < 100) errorMessage += "\n" + errorBody;
                        }
                    }
                    
                    updateLastMessage("Sorry, " + errorMessage, null);
                    return;
                }

                try {
                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);
                    String content = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    
                    // Parse content to separate Chinese and English
                    String chineseText = content;
                    String englishText = null;
                    
                    if (content.contains("|||")) {
                        String[] parts = content.split("\\|\\|\\|");
                        chineseText = parts[0].trim();
                        if (parts.length > 1) {
                            englishText = parts[1].trim();
                        }
                    }
                    
                    updateLastMessage(chineseText, englishText);
                } catch (JSONException e) {
                    Log.e("ChatActivity", "Parsing Error", e);
                    updateLastMessage("Sorry, could not parse response.", null);
                }
            }
        });
    }

    private void updateLastMessage(String message, String translation) {
        runOnUiThread(() -> {
            ChatMessage chatMessage = messageList.get(messageList.size() - 1);
            chatMessage.setMessage(message);
            if (translation != null) {
                chatMessage.setTranslatedMessage(translation);
            }
            chatAdapter.notifyItemChanged(messageList.size() - 1);
        });
    }
}