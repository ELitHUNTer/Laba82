package com.example.laba8.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.laba8.LocaleManager;
import com.example.laba8.LoginActivity;
import com.example.laba8.MainActivity;
import com.example.laba8.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Network.NetworkManager;

public class SettingsFragment extends Fragment {

    private TextView currentLogin;
    private Spinner languageSelector;
    private Map<String, String> languages = new HashMap<String, String>() {{
        put("Русский", "ru");
        put("English", "en");
        put("Suomalainen", "fi");
        put("Ελληνική", "el");
        put("Español", "es");
        put("ru", "Русский");
        put("en", "English");
        put("fi", "Suomalainen");
        put("el", "Ελληνική");
        put("es", "Español");
    }};
    private String[] s = {"Русский", "English", "Suomalainen", "Ελληνική", "Español"};
    private Button quitButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public SettingsFragment(Context context){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //context = MainActivity.context;
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        currentLogin = v.findViewById(R.id.currentLogin_settingsFragment);
        currentLogin.setText(NetworkManager.getInstance().getLogin());
        quitButton = v.findViewById(R.id.quitButton_settingsFragment);
        languageSelector = v.findViewById(R.id.languageSelector_SettingsFragment);
        //languageSelector.setSelection(3);
        Log.e("def", LocaleManager.getInstance().getLanguage());
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });
        //Log.e("123", String.valueOf(indexOf(s, languages.get(LocaleManager.getInstance().getLanguage()))));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSelector.setAdapter(adapter);
        languageSelector.setSelection(indexOf(s, languages.get(LocaleManager.getInstance().getLanguage())));

        //SharedPreferences languagepref = context.getSharedPreferences("language", context.MODE_PRIVATE);
        //Log.e("def", LocaleManager.getInstance().getLanguage());

        languageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.e("language", ((TextView)view).getText().toString());
                if (!languages.get(((TextView) view).getText().toString()).equals(LocaleManager.getInstance().getLanguage())) {
                    LocaleManager.getInstance().setLocalization(languages.get(((TextView) view).getText().toString()));
                    getActivity().finish();
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return v;
    }

    private int indexOf(String[] arr, String value){
        for (int i = 0; i < arr.length; i++){
            if (arr[i].equals(value))
                return i;
        }
        return -1;
    }
}