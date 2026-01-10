package com.example.ailanguagetutor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_MODEL = 2;
    private static final String OPENROUTER_API_KEY = "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";

    private List<ChatMessage> messageList;
    private TextToSpeech tts;
    private Context context;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
        // Initialize TextToSpeech
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.CHINESE);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(context, "Chinese language not supported for TTS", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "TTS Initialization failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        if (message.getSender() == ChatMessage.Sender.USER) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_MODEL;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_model, parent, false);
            return new ModelViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(message, context);
        } else if (holder instanceof ModelViewHolder) {
            ((ModelViewHolder) holder).bind(message, tts);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatMessage;
        TextView tvTranslate;
        TextView tvTranslatedMessage;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatMessage = itemView.findViewById(R.id.tvChatMessage);
            tvTranslate = itemView.findViewById(R.id.tvTranslate);
            tvTranslatedMessage = itemView.findViewById(R.id.tvTranslatedMessage);
        }

        void bind(ChatMessage message, Context context) {
            tvChatMessage.setText(message.getMessage());

            if (message.isTranslated()) {
                tvTranslatedMessage.setVisibility(View.VISIBLE);
                tvTranslatedMessage.setText(message.getTranslatedMessage());
                tvTranslate.setText("Hide Translation");
            } else {
                tvTranslatedMessage.setVisibility(View.GONE);
                tvTranslate.setText("Translate to Chinese");
            }

            tvTranslate.setOnClickListener(v -> {
                if (message.isTranslated()) {
                    message.setTranslated(false);
                    tvTranslatedMessage.setVisibility(View.GONE);
                    tvTranslate.setText("Translate to Chinese");
                } else {
                    if (message.getTranslatedMessage() != null) {
                        message.setTranslated(true);
                        tvTranslatedMessage.setVisibility(View.VISIBLE);
                        tvTranslate.setText("Hide Translation");
                    } else {
                        // Call API to translate
                        tvTranslate.setText("Translating...");
                        translateText(message.getMessage(), context, message, this);
                    }
                }
            });
        }

        private void translateText(String text, Context context, ChatMessage message, UserViewHolder holder) {
            OkHttpClient client = new OkHttpClient();
            String prompt = "Translate the following English text to Chinese: '" + text + "'. Only provide the translation, no other text.";

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("model", "mistralai/mistral-7b-instruct:free");
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
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Translation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        holder.tvTranslate.setText("Translate to Chinese");
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(context, "Translation error: " + response.code(), Toast.LENGTH_SHORT).show();
                            holder.tvTranslate.setText("Translate to Chinese");
                        });
                        return;
                    }

                    try {
                        String responseData = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String content = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");

                        new Handler(Looper.getMainLooper()).post(() -> {
                            message.setTranslatedMessage(content);
                            message.setTranslated(true);
                            holder.tvTranslatedMessage.setText(content);
                            holder.tvTranslatedMessage.setVisibility(View.VISIBLE);
                            holder.tvTranslate.setText("Hide Translation");
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        new Handler(Looper.getMainLooper()).post(() -> {
                             Toast.makeText(context, "Parsing error", Toast.LENGTH_SHORT).show();
                             holder.tvTranslate.setText("Translate to Chinese");
                        });
                    }
                }
            });
        }
    }

    static class ModelViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatMessage;
        TextView tvTranslate;
        TextView tvTranslatedMessage;
        ImageView ivSpeak;

        ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatMessage = itemView.findViewById(R.id.tvChatMessage);
            tvTranslate = itemView.findViewById(R.id.tvTranslate);
            tvTranslatedMessage = itemView.findViewById(R.id.tvTranslatedMessage);
            ivSpeak = itemView.findViewById(R.id.ivSpeak);
        }

        void bind(ChatMessage message, TextToSpeech tts) {
            tvChatMessage.setText(message.getMessage());
            
            if (message.isTranslated()) {
                tvTranslatedMessage.setVisibility(View.VISIBLE);
                tvTranslatedMessage.setText(message.getTranslatedMessage());
                tvTranslate.setText("Hide Translation");
            } else {
                tvTranslatedMessage.setVisibility(View.GONE);
                tvTranslate.setText("Translate");
            }

            tvTranslate.setOnClickListener(v -> {
                if (message.isTranslated()) {
                    message.setTranslated(false);
                    tvTranslatedMessage.setVisibility(View.GONE);
                    tvTranslate.setText("Translate");
                } else {
                    if (message.getTranslatedMessage() == null) {
                         message.setTranslatedMessage("[Translation] " + message.getMessage());
                    }
                    message.setTranslated(true);
                    tvTranslatedMessage.setText(message.getTranslatedMessage());
                    tvTranslatedMessage.setVisibility(View.VISIBLE);
                    tvTranslate.setText("Hide Translation");
                }
            });
            
            ivSpeak.setOnClickListener(v -> {
                if (tts != null) {
                    // Only speak the Chinese part (message content)
                    tts.speak(message.getMessage(), TextToSpeech.QUEUE_FLUSH, null, null);
                }
            });
        }
    }
}