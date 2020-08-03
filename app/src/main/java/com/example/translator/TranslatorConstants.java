package com.example.translator;

import android.provider.BaseColumns;

public final class TranslatorConstants {
    private TranslatorConstants() {}

    /* Inner class that defines the table contents */
    public static class Phrases implements BaseColumns {
        public static final String TABLE_NAME = "phrases";
        public static final String COLUMN_NAME_PHRASE = "phrase";
    }


    public static class Languages implements BaseColumns {
        public static final String TABLE_NAME = "languages";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LANGUAGE_CODE = "language_code";
        public static final String COLUMN_NAME_ISSUBSCRIBED = "is_subscribed";
    }

    public static class PhrasesInLanguages implements BaseColumns {
        public static final String TABLE_NAME = "phrases_in_languages";
        public static final String COLUMN_NAME_LANGUAGE_ID = "language_id";
        public static final String COLUMN_NAME_PHRASE_ID = "phrase_id";
        public static final String COLUMN_NAME_TRANSLATED_PHRASE = "translated_phrase";
    }

}
