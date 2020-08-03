package com.example.translator.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import com.example.translator.TranslatorConstants;
import com.example.translator.TranslatorConstants.Languages;
import com.example.translator.TranslatorConstants.Phrases;
import com.example.translator.TranslatorConstants.PhrasesInLanguages;
import com.example.translator.model.Language;
import com.example.translator.model.Phrase;
import com.example.translator.model.PhraseInLanguage;

import java.util.ArrayList;
import java.util.List;

public class DbQueries {
    //sqlite queries from - https://developer.android.com/training/data-storage/sqlite
    public List<Phrase> getPhrases(Context context) {
        List<Phrase> phrasesArray = new ArrayList<>();
        TranslatorDbHelper dbHelper = new TranslatorDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                Phrases.COLUMN_NAME_PHRASE,
        };


// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Phrases.COLUMN_NAME_PHRASE + " ASC";

        Cursor cursor = db.query(
                Phrases.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Phrases._ID));
            String phraseText = cursor.getString(1);

            Phrase phrase = new Phrase(itemId, phraseText);
            phrasesArray.add(phrase);
        }
        cursor.close();
        return phrasesArray;
    }

    public void updatePhrases(Context context, String oldPhrase, String newPhrase) {
        TranslatorDbHelper dbHelper = new TranslatorDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(Phrases.COLUMN_NAME_PHRASE, newPhrase);

// Which row to update, based on the title
        String selection = Phrases.COLUMN_NAME_PHRASE + " LIKE ?";
        String[] selectionArgs = {oldPhrase};
        String query = "Select * From " + Phrases.TABLE_NAME + " where " + Phrases.COLUMN_NAME_PHRASE + " = '" + newPhrase + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() <= 0) {

//delete this phrase from PhraseInLanguages table
            String querytogetId = "Select * From " + Phrases.TABLE_NAME + " where " + Phrases.COLUMN_NAME_PHRASE + " = '" + oldPhrase + "'";
            Cursor cursortogetId = db.rawQuery(querytogetId, null);
            while (cursortogetId.moveToNext()) {
                long itemId = cursortogetId.getLong(
                        cursortogetId.getColumnIndexOrThrow(Phrases._ID));


                String selectionPhraseLanguage = PhrasesInLanguages.COLUMN_NAME_PHRASE_ID + " LIKE ?";
// Specify arguments in placeholder order.
                String[] selectionArgsPhraseLanguage = {""+itemId};
// Issue SQL statement.
                int deletedRows = db.delete(PhrasesInLanguages.TABLE_NAME, selectionPhraseLanguage, selectionArgsPhraseLanguage);

            }



            //update phrase
            int count = db.update(
                    Phrases.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        } else {
            Toast.makeText(context, "Phrase already exist", Toast.LENGTH_SHORT).show();

        }
        cursor.close();

    }

    public void addLanguage(Context context, Language language) {
        TranslatorDbHelper dbHelper = new TranslatorDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Languages.COLUMN_NAME_NAME, language.getLanguageName());
        values.put(Languages.COLUMN_NAME_LANGUAGE_CODE, language.getLanguageCode());
        values.put(Languages.COLUMN_NAME_ISSUBSCRIBED, language.getIsSubscribed());


        db.insert(Languages.TABLE_NAME, null, values);
    }

    public boolean isTableEmpty(Context context) {
        boolean isTableEmpty;
        TranslatorDbHelper dbHelper = new TranslatorDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM " + Languages.TABLE_NAME;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);
        if (rowCount > 0) {
            isTableEmpty = false;
            Log.d("tttttttttttttttttttt", "not empty");
        } else {
            isTableEmpty = true;
            Log.d("tttttttttttttttttttt", "empty");
        }
        cursor.close();

        return isTableEmpty;
    }

    public List<Language> getLanguages(Context context) {
        List<Language> phrasesArray = new ArrayList<>();
        TranslatorDbHelper dbHelper = new TranslatorDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sortOrder =
                Languages.COLUMN_NAME_NAME + " ASC";

        Cursor cursor = db.query(
                Languages.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Languages._ID));
            String name = cursor.getString(1);
            String code = cursor.getString(2);
            int isSubscribed = cursor.getInt(3);


            phrasesArray.add(new Language(name, code, isSubscribed));
        }
        cursor.close();
        return phrasesArray;
    }

    public void updateLanguageSubscription(Context context, String code, int isSubscribed) {
        TranslatorDbHelper dbHelper = new TranslatorDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(Languages.COLUMN_NAME_ISSUBSCRIBED, isSubscribed);

// Which row to update, based on the title
        String selection = Languages.COLUMN_NAME_LANGUAGE_CODE + " LIKE ?";
        String[] selectionArgs = {code};

        int count = db.update(
                Languages.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


    public List<String> getSubscribedLanguageNames(Context context) {
        List<String> subscribedLanguages = new ArrayList<>();
        for (Language l : getLanguages(context)) {
            if (l.getIsSubscribed() == 1) {
                subscribedLanguages.add(l.getLanguageName());
            }
        }

        return subscribedLanguages;
    }

    public PhraseInLanguage getTranslatedPhraseSavedInDb(PhraseInLanguage phrasesInLanguages, Context context) {
        TranslatorDbHelper dbHelper = new TranslatorDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "Select * From " + PhrasesInLanguages.TABLE_NAME + " where " + PhrasesInLanguages.COLUMN_NAME_LANGUAGE_ID + " = '" + phrasesInLanguages.getLanguageId() + "' AND " + PhrasesInLanguages.COLUMN_NAME_PHRASE_ID + " = '" + phrasesInLanguages.getPhraseId() + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String translatedPhrase = cursor.getString(3);
                phrasesInLanguages.setTranslatedPhrase(translatedPhrase);

            }
        }
        cursor.close();
        return phrasesInLanguages;
    }

    public Language getLanguageByName(String languageName, Context context) {
        Language language = null;
        TranslatorDbHelper dbHelper = new TranslatorDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "Select * From " + Languages.TABLE_NAME + " where " + Languages.COLUMN_NAME_NAME + " = '" + languageName + "'";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Languages._ID));
            String name = cursor.getString(1);
            String code = cursor.getString(2);
            int isSubscribed = cursor.getInt(3);


            language = new Language(itemId, name, code, isSubscribed);

        }

        return language;
    }

    public long getPhraseId(String phrase, Context context) {
        long phraseId = -1;
        TranslatorDbHelper dbHelper = new TranslatorDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "Select * From " + Phrases.TABLE_NAME + " where " + Phrases.COLUMN_NAME_PHRASE + " = '" + phrase + "'";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            phraseId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Phrases._ID));
        }
        return phraseId;
    }

    public void addPhraseInLanguage(Context context, PhraseInLanguage phraseInLanguage) {
        TranslatorDbHelper dbHelper = new TranslatorDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PhrasesInLanguages.COLUMN_NAME_LANGUAGE_ID, phraseInLanguage.getLanguageId());
        values.put(PhrasesInLanguages.COLUMN_NAME_PHRASE_ID, phraseInLanguage.getPhraseId());
        values.put(PhrasesInLanguages.COLUMN_NAME_TRANSLATED_PHRASE, phraseInLanguage.getTranslatedPhrase());


        db.insert(PhrasesInLanguages.TABLE_NAME, null, values);
    }
}
