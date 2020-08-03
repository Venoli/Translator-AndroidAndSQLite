package com.example.translator.model;

public class Phrase {
    private long id;
    private String  phrase;

    public Phrase(long id, String phrase) {
        this.id = id;
        this.phrase = phrase;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}
