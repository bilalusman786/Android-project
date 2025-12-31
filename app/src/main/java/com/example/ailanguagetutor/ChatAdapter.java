package com.example.ailanguagetutor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_MODEL = 2;

    private List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
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
            ((UserViewHolder) holder).bind(message);
        } else if (holder instanceof ModelViewHolder) {
            ((ModelViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatMessage;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatMessage = itemView.findViewById(R.id.tvChatMessage);
        }

        void bind(ChatMessage message) {
            tvChatMessage.setText(message.getMessage());
        }
    }

    static class ModelViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatMessage;
        TextView tvTranslate;
        TextView tvTranslatedMessage;

        ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatMessage = itemView.findViewById(R.id.tvChatMessage);
            tvTranslate = itemView.findViewById(R.id.tvTranslate);
            tvTranslatedMessage = itemView.findViewById(R.id.tvTranslatedMessage);
        }

        void bind(ChatMessage message) {
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
        }
    }
}