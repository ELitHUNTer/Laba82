package com.example.laba8;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.TextView;

import java.util.Locale;

public class LocaleManager {

    private static LocaleManager localeManager = null;
    private Context context;
    private SharedPreferences sharedPreferences;

    public LocaleManager(Context context){
        if (localeManager == null){
            this.context = context;
            sharedPreferences = context.getSharedPreferences("language", context.MODE_PRIVATE);
            localeManager = this;
        }
    }

    public static LocaleManager getInstance(){
        return localeManager;
    }

    public void setLocalization(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());

        saveLocalization(language);
    }

    private void saveLocalization(String language){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("languageToLoad", language);
        editor.commit();
    }

    public String getLanguage(){
        return sharedPreferences.getString("languageToLoad", "en");
    }
}
