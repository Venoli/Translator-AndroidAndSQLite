package com.example.translator.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translator.R;
import com.example.translator.model.Language;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<Language> languagesList;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, List<Language> languagesList) {
        this.context = context;
        this.languagesList = languagesList;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return languagesList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.languages_listview, null);
        TextView textView = (TextView)view.findViewById(R.id.textView);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        textView.setText(languagesList.get(i).getLanguageName());
        if(languagesList.get(i).getIsSubscribed()==1){
            checkBox.setChecked(true);
        }else if(languagesList.get(i).getIsSubscribed()==0){
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    languagesList.get(i).setIsSubscribed(1);
                } else {
                    languagesList.get(i).setIsSubscribed(0);
                }
            }
        });
//        checkBox.setChecked( languagesList.get(i).getIsSubscribed());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isSelected()){
                }else {
                }
            }
        });
        return view;
    }

    public List<Language> getLanguagesList() {
        return languagesList;
    }
}
