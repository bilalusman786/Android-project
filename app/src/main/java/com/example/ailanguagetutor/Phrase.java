package com.example.ailanguagetutor;

public class Phrase {
    private String hanzi;
    private String pinyin;
    private String english;

    public Phrase(String hanzi, String pinyin, String english) {
        this.hanzi = hanzi;
        this.pinyin = pinyin;
        this.english = english;
    }

    public String getHanzi() {
        return hanzi;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getEnglish() {
        return english;
    }
}