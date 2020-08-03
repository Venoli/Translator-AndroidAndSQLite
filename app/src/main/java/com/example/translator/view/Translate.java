package com.example.translator.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translator.R;
import com.example.translator.controller.DbQueries;
import com.example.translator.model.Phrase;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Translate extends AppCompatActivity {
    // IBM code from this tutorial (tutorial6-7) - https://learn-eu-central-1-prod-fleet01-xythos.s3-eu-central-1.amazonaws.com/5d07709844dfc/8782362?response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%27Tutorial%252006-07.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200415T180925Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200415%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=4fb9f63d9c9a0f058ccf0b80cb6176594eebcc408e5db014666adef49bafa513
    ListView listView;
    String selectedLanguage;
    String selectedPhrase;
    int selectedListItemId=-1;
    boolean isPhraseSelectable = false;
    private LanguageTranslator translationService;
    TextView translatedText;
    DbQueries dbQueries = new DbQueries();

    //Text to speech
    private StreamPlayer player = new StreamPlayer();
    private TextToSpeech textService;
    ImageView speakerImg;
    boolean isIBMError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        //setup tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            mTitle.setText(R.string.translate);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
            getSupportActionBar().setDisplayShowTitleEnabled(false);//remove title
        }

        translatedText =(TextView)findViewById(R.id.translatedText);
        speakerImg =(ImageView) findViewById(R.id.speakerImg);
        textService = initTextToSpeechService();
        List<String> subscribedLanguages = dbQueries.getSubscribedLanguageNames(getApplicationContext());//get names for dropdown list
        subscribedLanguages.add(0, "Select Language");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_selected, subscribedLanguages);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);



        final List<Phrase> phrasesArray = dbQueries.getPhrases(getApplicationContext());
        ArrayList<String> phrasesTextArray = new ArrayList<>();
        for (Phrase p: phrasesArray){
            phrasesTextArray.add(p.getPhrase());
        }
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, phrasesTextArray);
        listView.setAdapter(arrayAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    selectedLanguage = parent.getItemAtPosition(position).toString();
                    isPhraseSelectable = true;
                    listView.setSelector(R.color.colorAccent);

                } else {
                    selectedLanguage = null;
                    isPhraseSelectable = false;
                    listView.setSelector(R.color.transparent);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    view.setBackgroundColor(Color.RED);
                    selectedListItemId = position;
                    selectedPhrase = phrasesArray.get(position).getPhrase();

            }
        });


    }

    public void translate(View view) {
        if(isNetworkConnected()){
        String code = getLanguageCode(selectedLanguage);
        if(selectedLanguage!=null&&selectedPhrase!=null && code!=null) {
            Authenticator authenticator
                    = new
                    IamAuthenticator(getString(R.string.language_translator_apikey));
            translationService = new LanguageTranslator("2018-05-01", authenticator);
            translationService.setServiceUrl(getString(R.string.language_translator_url));
            new TranslationTask().execute(code);
        }}
        else{
            Toast.makeText(getApplicationContext(),"No Internet",Toast.LENGTH_SHORT).show();

        }
    }



    private class TranslationTask extends AsyncTask<String, Void,
                String> {
        @Override
        protected String doInBackground(String... params) {
            TranslateOptions translateOptions = new
                    TranslateOptions.Builder()
                    .addText(selectedPhrase)
                    .source(Language.ENGLISH)
                    .target(params[0])
                    .build();
            String firstTranslation="";
            try {
                TranslationResult result = translationService.translate(translateOptions).execute().getResult();
                firstTranslation = result.getTranslations().get(0).getTranslation();
                isIBMError =false;
            }catch (Exception e){
                firstTranslation = e.getMessage();
                isIBMError =true;
            }
            return firstTranslation;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            translatedText.setText(s);
            if(isIBMError){
                translatedText.setTextColor(Color.RED);
                speakerImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_black_24dp_disable));

            }else{
                translatedText.setTextColor(getResources().getColor(R.color.textColor));
                speakerImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_black_24dp));

            }
        }
    }

    public String getLanguageCode(String language){
         String code = null;
        for (com.example.translator.model.Language l: dbQueries.getLanguages(getApplicationContext())){
            if (l.getLanguageName().equals(language)){
                code = l.getLanguageCode();
                break;
            }
        }
        return code;
    }

    public void pronounce(View view) {
        if((!translatedText.getText().toString().equals(""))&&!isIBMError){
            if(isNetworkConnected()){
        new SynthesisTask().execute(translatedText.getText().toString());}
            else {
                Toast.makeText(getApplicationContext(),"No Internet",Toast.LENGTH_SHORT).show();

            }
        }
    }
    private TextToSpeech initTextToSpeechService() {
        Authenticator authenticator = new
                IamAuthenticator(getString(R.string.text_speech_apikey));
        TextToSpeech service = new TextToSpeech(authenticator);
        service.setServiceUrl(getString(R.string.text_speech_url));
        return service;
    }
    private class SynthesisTask extends AsyncTask<String, Void,
            String> {
        @Override
        protected String doInBackground(String... params) {
            SynthesizeOptions synthesizeOptions = new
                    SynthesizeOptions.Builder()
                    .text(params[0])
                    .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                    .accept(HttpMediaType.AUDIO_WAV).build();
            player.playStream(textService.synthesize(synthesizeOptions).execute()
                    .getResult());
            return "Did synthesize";
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}

