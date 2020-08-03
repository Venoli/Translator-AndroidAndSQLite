package com.example.translator.model;

public class PhraseInLanguage {
    private long languageId;
    private long phraseId;
    private String translatedPhrase;

    public PhraseInLanguage(long languageId, long phraseId) {
        this.languageId = languageId;
        this.phraseId = phraseId;
    }

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    public long getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(long phraseId) {
        this.phraseId = phraseId;
    }

    public String getTranslatedPhrase() {
        return translatedPhrase;
    }

    public void setTranslatedPhrase(String translatedPhrase) {
        this.translatedPhrase = translatedPhrase;
    }
}
