package com.example.translator.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.translator.TranslatorConstants;
import com.example.translator.TranslatorConstants.Languages;
import com.example.translator.TranslatorConstants.Phrases;
import com.example.translator.TranslatorConstants.PhrasesInLanguages;
import com.example.translator.model.Language;

public class TranslatorDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES_PHRASES =
            "CREATE TABLE " + Phrases.TABLE_NAME + " (" +
                    Phrases._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Phrases.COLUMN_NAME_PHRASE + " TEXT UNIQUE)";

    private static final String SQL_CREATE_ENTRIES_lANGUAGES =
            "CREATE TABLE " + Languages.TABLE_NAME + " (" +
                    Languages._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Languages.COLUMN_NAME_NAME + " TEXT," +
                    Languages.COLUMN_NAME_LANGUAGE_CODE + " TEXT," +
                    Languages.COLUMN_NAME_ISSUBSCRIBED + " INTEGER)";

     private static final String SQL_CREATE_ENTRIES_PHRASES_In_lANGUAGES =
            "CREATE TABLE " + PhrasesInLanguages.TABLE_NAME + " (" +
                    PhrasesInLanguages._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PhrasesInLanguages.COLUMN_NAME_LANGUAGE_ID + " INTEGER," +
                    PhrasesInLanguages.COLUMN_NAME_PHRASE_ID + " INTEGER," +
                    PhrasesInLanguages.COLUMN_NAME_TRANSLATED_PHRASE + " TEXT)";



    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Translator.db";

    public TranslatorDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_PHRASES);
        db.execSQL(SQL_CREATE_ENTRIES_lANGUAGES);
        db.execSQL(SQL_CREATE_ENTRIES_PHRASES_In_lANGUAGES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES+ Phrases.TABLE_NAME);
        db.execSQL(SQL_DELETE_ENTRIES+ Languages.TABLE_NAME);
        db.execSQL(SQL_DELETE_ENTRIES+ PhrasesInLanguages.TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
