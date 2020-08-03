package com.example.translator.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import com.example.translator.R;
import com.example.translator.controller.DbQueries;
import com.example.translator.model.Language;
import com.example.translator.model.Phrase;
import com.example.translator.model.PhraseInLanguage;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;

import java.util.HashMap;
import java.util.List;

public class TranslateAll extends AppCompatActivity {
    DbQueries dbQueries = new DbQueries();
    String selectedLanguage;
    boolean isButtonSelectable = false;
    ListView listView;
TranslateAllAdapter translateAllAdapter;
    private LanguageTranslator translationService;
    Language language;
    List<Phrase> phrasesArray;//all phrases
    HashMap<String,String> phraseAndTranslatedPhrase;
    boolean isIBMError = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_all);

        //setup tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            mTitle.setText(R.string.translate_all);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
            getSupportActionBar().setDisplayShowTitleEnabled(false);//remove title
        }

        listView = (ListView) findViewById(R.id.listView);
        List<String> subscribedLanguages = dbQueries.getSubscribedLanguageNames(getApplicationContext());//get names for dropdown list
        subscribedLanguages.add(0, "Select Language");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_selected, subscribedLanguages);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    selectedLanguage = parent.getItemAtPosition(position).toString();
                    isButtonSelectable = true;
                } else {
                    selectedLanguage = null;
                    isButtonSelectable = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void translateAll(View view) {
        if(isButtonSelectable){
            phrasesArray = dbQueries.getPhrases(getApplicationContext());
            phraseAndTranslatedPhrase = new HashMap<>();
            language =dbQueries.getLanguageByName(selectedLanguage,getApplicationContext());//selected language
            for(Phrase p:phrasesArray){
                PhraseInLanguage phraseInLanguage = new PhraseInLanguage(language.getId(),p.getId());
                phraseInLanguage = dbQueries.getTranslatedPhraseSavedInDb(phraseInLanguage,getApplicationContext());

                if(phraseInLanguage.getTranslatedPhrase()==null&&isNetworkConnected()){
                    translate(phraseInLanguage);

                }
                phraseAndTranslatedPhrase.put(p.getPhrase(),phraseInLanguage.getTranslatedPhrase());

            }




            translateAllAdapter = new TranslateAllAdapter(getApplicationContext(), phraseAndTranslatedPhrase,phrasesArray,false);
            listView.setAdapter(translateAllAdapter);
            listView.setVisibility(View.VISIBLE);

        }else {
            Toast.makeText(getApplicationContext(),"Select a language",Toast.LENGTH_SHORT).show();

        }

    }

    public void translate(PhraseInLanguage phraseInLanguage) {
        Authenticator authenticator
                = new
                IamAuthenticator(getString(R.string.language_translator_apikey));
        translationService = new LanguageTranslator("2018-05-01", authenticator);
        translationService.setServiceUrl(getString(R.string.language_translator_url));
        new TranslationTask().execute(phraseInLanguage);

    }



    private class TranslationTask extends AsyncTask<PhraseInLanguage, Void,
            PhraseInLanguage> {
        String text="";

        @Override
        protected PhraseInLanguage doInBackground(PhraseInLanguage... params) {
            for(Phrase p: phrasesArray){
                if(p.getId()==params[0].getPhraseId()){
                    text=p.getPhrase();
                    break;
                }
            }
            TranslateOptions translateOptions = new
                    TranslateOptions.Builder()
                    .addText(text)
                    .source(com.ibm.watson.language_translator.v3.util.Language.ENGLISH)
                    .target(language.getLanguageCode())
                    .build();
            String firstTranslation ="";
            try {
                TranslationResult result
                        =
                        translationService.translate(translateOptions).execute().getResult();
                 firstTranslation =
                        result.getTranslations().get(0).getTranslation();
                isIBMError =false;
            }catch (Exception e){
                firstTranslation = e.getMessage();
                isIBMError =true;
        }
            PhraseInLanguage phraseInLanguage = params[0];
            phraseInLanguage.setTranslatedPhrase(firstTranslation);
            return phraseInLanguage;

        }
        @Override
        protected void onPostExecute(PhraseInLanguage s) {
            super.onPostExecute(s);
            phraseAndTranslatedPhrase.put(text,s.getTranslatedPhrase());
            if(!isIBMError){
            dbQueries.addPhraseInLanguage(getApplicationContext(),s);
            }
            translateAllAdapter = new TranslateAllAdapter(getApplicationContext(), phraseAndTranslatedPhrase,phrasesArray,isIBMError);
            listView.setAdapter(translateAllAdapter);
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
