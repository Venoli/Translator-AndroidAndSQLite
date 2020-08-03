package com.example.translator.model;

public class Language {
    private long id;
   private String languageName;
    private String languageCode;
    private int isSubscribed;//1 = subscribed, 0 = not subscribed

    public Language(String languageName, String languageCode, int isSubscribed) {
        this.languageName = languageName;
        this.languageCode = languageCode;
        this.isSubscribed = isSubscribed;
    }

    public Language(long id, String languageName, String languageCode, int isSubscribed) {
        this.id = id;
        this.languageName = languageName;
        this.languageCode = languageCode;
        this.isSubscribed = isSubscribed;
    }

    public long getId() {
        return id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public int getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(int isSubscribed) {
        this.isSubscribed = isSubscribed;
    }
}
