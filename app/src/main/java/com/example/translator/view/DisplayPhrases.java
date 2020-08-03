package com.example.translator.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.translator.R;
import com.example.translator.TranslatorConstants.Phrases;
import com.example.translator.controller.DbQueries;
import com.example.translator.controller.TranslatorDbHelper;
import com.example.translator.model.Phrase;

import java.util.ArrayList;
import java.util.List;

public class DisplayPhrases extends AppCompatActivity {
    // Array of strings...
    ListView phrasesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);

        //setup tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            mTitle.setText(R.string.display_phrases);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
            getSupportActionBar().setDisplayShowTitleEnabled(false);//remove title
        }

        DbQueries dbQueries = new DbQueries();
        List<Phrase> phrasesArray = dbQueries.getPhrases(getApplicationContext());
        ArrayList<String> phrasesTextArray = new ArrayList<>();
        for (Phrase p: phrasesArray){
            phrasesTextArray.add(p.getPhrase());
        }


        phrasesListView = (ListView) findViewById(R.id.phrasesListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, phrasesTextArray);
        phrasesListView.setAdapter(arrayAdapter);

    }



}
