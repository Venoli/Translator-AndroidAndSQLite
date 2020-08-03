package com.example.translator.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translator.R;
import com.example.translator.controller.DbQueries;
import com.example.translator.model.Phrase;

import java.util.List;

public class EditPhrases extends AppCompatActivity {
    RadioGroup rg;
    EditText editText;
    DbQueries dbQueries = new DbQueries();
    boolean editMode = false;
    List<Phrase> phrasesArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);

        //setup tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            mTitle.setText(R.string.edit_phrases);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
            getSupportActionBar().setDisplayShowTitleEnabled(false);//remove title
        }

        rg = (RadioGroup) findViewById(R.id.rg);
        editText = (EditText) findViewById(R.id.editText);

         phrasesArray = dbQueries.getPhrases(getApplicationContext());
        for(Phrase p:phrasesArray){
            String s=p.getPhrase();
            RadioButton rb = new RadioButton(getApplicationContext());
            // Set a Text for new RadioButton
            rb.setText(s);
            rb.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            rb.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            rb.setGravity(Gravity.START);
            rb.setPadding(20,10,20,10);
            rb.setTextColor(getResources().getColor(R.color.colorPrimary));
            rb.setTypeface(Typeface.DEFAULT_BOLD);
            //add the new RadioButton to the RadioGroup
            rg.addView(rb);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(editMode){
                    RadioButton selectedButton = (RadioButton) findViewById(checkedId);
                    editText.setText(selectedButton.getText());
                }
            }
        });
    }


    public void editPhrases(View view) {
        int selectedId = rg.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton selectedButton = (RadioButton) findViewById(selectedId);
        if(selectedId<0||selectedButton==null){
            Toast.makeText(getApplicationContext(),"Select a phrase",Toast.LENGTH_SHORT).show();
        }else {
            editText.setText(selectedButton.getText());
            editMode=true;
        }

}

    public void save(View view) {

        int selectedId = rg.getCheckedRadioButtonId();
        Log.d("selectedId",String.valueOf(selectedId));
        RadioButton selectedButton = (RadioButton) findViewById(selectedId);
        if(selectedId<0){
            Toast.makeText(getApplicationContext(),"First, press edit",Toast.LENGTH_SHORT).show();
        }else {
            if(!editText.getText().toString().equals("")&&selectedButton!=null) {
                dbQueries.updatePhrases(getApplicationContext(), selectedButton.getText().toString(), editText.getText().toString());
                phrasesArray = dbQueries.getPhrases(getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rg.removeAllViews();
                        for (Phrase p : phrasesArray) {
                            String s = p.getPhrase();
                            RadioButton rb = new RadioButton(getApplicationContext());
                            // Set a Text for new RadioButton
                            rb.setText(s);
                            rb.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                            rb.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            rb.setGravity(Gravity.START);
                            rb.setPadding(20, 10, 20, 10);
                            rb.setTextColor(getResources().getColor(R.color.colorPrimary));
                            rb.setTypeface(Typeface.DEFAULT_BOLD);
                            //add the new RadioButton to the RadioGroup
                            rg.addView(rb);
                        }
                    }
                });
//        selectedButton.setText(editText.getText().toString());
            }else {
                Toast.makeText(getApplicationContext(),"Can't be empty",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
