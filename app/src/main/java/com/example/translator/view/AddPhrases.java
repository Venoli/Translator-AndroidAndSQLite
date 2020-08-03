package com.example.translator.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translator.R;
import com.example.translator.TranslatorConstants.Phrases;
import com.example.translator.controller.TranslatorDbHelper;


public class AddPhrases extends AppCompatActivity {

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);
        //setup tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            mTitle.setText(R.string.add_phrases);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
            getSupportActionBar().setDisplayShowTitleEnabled(false);//remove title
        }
        editText = (EditText) findViewById(R.id.editText);



    }

    public void save(View view) {
        if(!editText.getText().toString().equals("")) {
            TranslatorDbHelper dbHelper = new TranslatorDbHelper(getApplicationContext());
// Gets the data repository in write mode
            SQLiteDatabase db = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Phrases.COLUMN_NAME_PHRASE, editText.getText().toString());

// Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(Phrases.TABLE_NAME, null, values);
            editText.setText("");
        }
    }
}
