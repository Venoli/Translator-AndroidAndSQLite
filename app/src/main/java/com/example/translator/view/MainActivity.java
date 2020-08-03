package com.example.translator.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.example.translator.R;
import com.example.translator.view.AddPhrases;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addPhrases(View view) {
        Intent intent = new Intent(this, AddPhrases.class);
        startActivity(intent);
    }

    public void displayPhrases(View view) {
        Intent intent = new Intent(this, DisplayPhrases.class);
        startActivity(intent);
    }

    public void editPhrases(View view) {
        Intent intent = new Intent(this, EditPhrases.class);
        startActivity(intent);
    }

    public void languageSubscription(View view) {
        Intent intent = new Intent(this, LanguageSubscription.class);
        startActivity(intent);
    }


    public void translate(View view) {
        Intent intent = new Intent(this, Translate.class);
        startActivity(intent);
    }

    public void translateAll(View view) {
        Intent intent = new Intent(this, TranslateAll.class);
        startActivity(intent);
    }
}
