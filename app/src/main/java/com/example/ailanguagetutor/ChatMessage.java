package com.example.ailanguagetutor;

public class ChatMessage {
    public enum Sender {
        USER, MODEL
    }

    private String message;
    private String translatedMessage;
    private Sender sender;
    private boolean isTranslated = false;

    public ChatMessage(String message, Sender sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTranslatedMessage() {
        return translatedMessage;
    }

    public void setTranslatedMessage(String translatedMessage) {
        this.translatedMessage = translatedMessage;
    }

    public boolean isTranslated() {
        return isTranslated;
    }

    public void setTranslated(boolean translated) {
        isTranslated = translated;
    }

    public Sender getSender() {
        return sender;
    }
}