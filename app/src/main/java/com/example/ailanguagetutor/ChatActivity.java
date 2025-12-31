package com.example.ailanguagetutor;

import android.os.Bundle;
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

        // Get AI response
        getAiResponse(messageText);
    }

    private void addMessage(String message, ChatMessage.Sender sender) {
        messageList.add(new ChatMessage(message, sender));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void getAiResponse(String userMessage) {
        // Add a placeholder for the AI response
        addMessage("Typing...", ChatMessage.Sender.MODEL);

        String prompt = "You are a friendly Chinese language tutor. The user said: '" + userMessage + "'. Respond naturally in Chinese. Keep your response to one or two short sentences.";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "mistralai/mistral-7b-instruct:free");
            JSONArray messages = new JSONArray();
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", prompt);
            messages.put(systemMessage);
            // You can add chat history here for better context
            jsonBody.put("messages", messages);
        } catch (JSONException e) {
            e.printStackTrace();
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
                updateLastMessage("Sorry, network error.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    updateLastMessage("Sorry, API error.");
                    return;
                }

                try {
                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);
                    String content = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    updateLastMessage(content);
                } catch (JSONException e) {
                    updateLastMessage("Sorry, could not parse response.");
                }
            }
        });
    }

    private void updateLastMessage(String message) {
        runOnUiThread(() -> {
            messageList.get(messageList.size() - 1).setMessage(message);
            chatAdapter.notifyItemChanged(messageList.size() - 1);
        });
    }
}