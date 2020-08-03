//package com.example.translator.controller;
//
//import android.os.AsyncTask;
//
//import com.example.translator.R;
//import com.ibm.cloud.sdk.core.security.Authenticator;
//import com.ibm.cloud.sdk.core.security.IamAuthenticator;
//import com.ibm.watson.language_translator.v3.LanguageTranslator;
//import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
//import com.ibm.watson.language_translator.v3.model.TranslateOptions;
//import com.ibm.watson.language_translator.v3.model.TranslationResult;
//import com.ibm.watson.language_translator.v3.util.Language;
//
//public class IBMController {
//    IdentifiableLanguages identifiableLanguages;
//
//    public class IdentifyLanguagesTask extends AsyncTask<IdentifiableLanguages, Void,
//            String> {
//        @Override
//        protected String doInBackground(IdentifiableLanguages... voids) {
//            Authenticator authenticator = new IamAuthenticator(getString(R.string.language_translator_apikey));
//            LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
//
//            service.setServiceUrl(getString(R.string.language_translator_url));
//            return service;
//
//            TranslateOptions translateOptions = new
//                    TranslateOptions.Builder()
//                    .addText(params[0])
//                    .source(Language.ENGLISH)
//                    .target("es")
//                    .build();
//            TranslationResult result = translationService.translate(translateOptions).execute().getResult();
//            String firstTranslation = result.getTranslations().get(0).getTranslation();
//            return firstTranslation;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            tv.setText(s);
//        }
//    }
//}
