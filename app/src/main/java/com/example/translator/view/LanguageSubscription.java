package com.example.translator.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translator.controller.DbQueries;
import com.example.translator.model.Language;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;

import com.example.translator.R;

import java.util.List;

public class LanguageSubscription extends AppCompatActivity {
    //    private LanguageTranslator translationService;
    IdentifiableLanguages identifiableLanguagesService;
    DbQueries dbQueries = new DbQueries();
    LanguageTranslator translator;
    ScrollView scrollView;
    ListView listView;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);

        //setup tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            mTitle.setText(R.string.lang_subscription);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
            getSupportActionBar().setDisplayShowTitleEnabled(false);//remove title
        }

        listView = (ListView) findViewById(R.id.languagesListView);
        IamAuthenticator authenticator = new IamAuthenticator("EXSnOHTLhvrKeP3gijTz1jbft6UiMeSMFaaGF3OJnhMJ");
        translator = new LanguageTranslator("2018-05-01", authenticator);
        translator.setServiceUrl("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/b3f5fd33-7efc-403f-95eb-01bda5eea2e2");
        if (dbQueries.isTableEmpty(getApplicationContext())) {
            identifiableLanguagesService = new IdentifiableLanguages();
            new LanguageSubscription.IdentifiableLanguagesTask().execute();
        } else {
            customAdapter = new CustomAdapter(getApplicationContext(), dbQueries.getLanguages(getApplicationContext()));
            listView.setAdapter(customAdapter);
        }

    }

    public void update(View view) {
        for (Language l : customAdapter.getLanguagesList()) {
            dbQueries.updateLanguageSubscription(getApplicationContext(), l.getLanguageCode(), l.getIsSubscribed());
        }
    }

//    private LanguageTranslator initLanguageTranslatorService() {
//        Authenticator authenticator
//                = new
//                IamAuthenticator(getString(R.string.language_translator_apikey));
//        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
//
//        service.setServiceUrl(getString(R.string.language_translator_url));
//        return service;
//    }

//    private class TranslationTask extends AsyncTask<String, Void,
//            String> {
//        @Override
//        protected String doInBackground(String... params) {
//            TranslateOptions translateOptions = new
//                    TranslateOptions.Builder()
//                    .addText(params[0])
//                    .source(Language.ENGLISH)
//                    .target("es")
//                    .build();
//            TranslationResult result = translationService.translate(translateOptions).execute().getResult();
//            String firstTranslation = result.getTranslations().get(0).getTranslation();
//            return firstTranslation;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            tv.setText(s);
//        }
//    }

    private class IdentifiableLanguagesTask extends AsyncTask<Void, Void,
            String> {
        @Override
        protected String doInBackground(Void... voids) {
            if (isNetworkConnected()) {
                IdentifiableLanguages languages = translator.listIdentifiableLanguages().execute().getResult();
                List<IdentifiableLanguage> identifiableLanguages = languages.getLanguages();

                if (identifiableLanguages != null)
                    for (IdentifiableLanguage iL : identifiableLanguages) {
                        Language language = new Language(iL.getName(), iL.getLanguage(), 0);
                        dbQueries.addLanguage(getApplicationContext(), language);
                    }
            }
            return "Saved all languages in db";
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            customAdapter = new CustomAdapter(getApplicationContext(), dbQueries.getLanguages(getApplicationContext()));
            listView.setAdapter(customAdapter);
            if(!isNetworkConnected()) {
                Toast.makeText(getApplicationContext(), "No internet. Try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
