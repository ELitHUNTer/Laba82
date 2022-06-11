package com.example.laba8.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laba8.LocaleManager;
import com.example.laba8.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;

import Network.NetworkManager;

public class ConsoleFragment extends Fragment {

    private TextInputLayout requestWrapper;
    private TextInputEditText request;
    private RecyclerView responseField;
    private Button submitButton;

    public ConsoleFragment() {
        // Required empty public constructor
    }

    public ConsoleFragment(Context context){

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
        View v = inflater.inflate(R.layout.fragment_console, container, false);
        requestWrapper = v.findViewById(R.id.requestWrapper_consoleFragment);
        request = v.findViewById(R.id.request_consoleFragment);
        responseField = v.findViewById(R.id.response_consoleFragment);
        submitButton = v.findViewById(R.id.submit_consoleFragment);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        responseField.setLayoutManager(llm);
        ListAdapter adapter = new ListAdapter();
        responseField.setAdapter(adapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = NetworkManager.getInstance().executeCommand(request.getText().toString());
                        ArrayList<String> list = new ArrayList<>();
                        String[] s = response.split("\n");
                        for (int i = 0; i < s.length; i++)
                            list.add(s[i]);
                        adapter.update(list);
                        Log.e("response", response);
                        Log.e("size", String.valueOf(response.split("\n").length));
                    }
                }).start();
            }
        });

        return v;
    }
}