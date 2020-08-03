package com.example.translator.view;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.zip.Inflater;

public class TranslateAllAdapter extends BaseAdapter {
    Context context;
    HashMap<String,String> phraseAndTranslatedPhrase;
    LayoutInflater inflter;
    List<Phrase> phrasesArray;
    boolean isIBMEroor;

    public TranslateAllAdapter(Context applicationContext, HashMap<String,String> phraseAndTranslatedPhrase, List<Phrase> phrasesArray,boolean isIBMEroor) {
        this.context = applicationContext;
        this.phraseAndTranslatedPhrase = phraseAndTranslatedPhrase;
        this.phrasesArray = phrasesArray;
        this.isIBMEroor =isIBMEroor;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return phraseAndTranslatedPhrase.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.translate_all_listview, null);
        TextView phrase = (TextView) view.findViewById(R.id.phrase);
        TextView translatedPhrase = (TextView) view.findViewById(R.id.translatedPhrase);
        phrase.setText(phrasesArray.get(i).getPhrase());
        if (phraseAndTranslatedPhrase.get(phrasesArray.get(i).getPhrase())==null){
            if(isNetworkConnected()) {
                translatedPhrase.setText("Translating..."); //1st time translating
            }else {
                translatedPhrase.setText("No Internet"); //1st time translating - no internet
            }
            translatedPhrase.setTextColor(Color.RED);
        }else{


            if (isIBMEroor){
                translatedPhrase.setTextColor(Color.RED);
                if(isNetworkConnected()){
                    translatedPhrase.setText(phraseAndTranslatedPhrase.get(phrasesArray.get(i).getPhrase())); //IBM error
                }else{
                    translatedPhrase.setText("No Internet"); // no internet
                }
            }else {
            translatedPhrase.setTextColor(context.getResources().getColor(R.color.colorPrimary)); // correct translated phrase
                translatedPhrase.setText(phraseAndTranslatedPhrase.get(phrasesArray.get(i).getPhrase()));
            }
        }



        return view;
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}

