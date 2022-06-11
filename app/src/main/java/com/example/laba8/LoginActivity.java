package com.example.laba8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import Network.Command;
import Network.NetworkManager;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout loginWrapper, passwordWrapper;
    private TextInputEditText login, password;
    private TextView errorField;
    private Button loginButton, createNewAccountButton;
    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NetworkManager.context = this;

        loginWrapper = findViewById(R.id.loginWrapper_login_activity);
        passwordWrapper = findViewById(R.id.passwordWrapper_login_activity);
        login = findViewById(R.id.login_login_activity);
        password = findViewById(R.id.password_login_activity);
        errorField = findViewById(R.id.error_login_activity);
        loginButton = findViewById(R.id.loginButton_login_activity);
        createNewAccountButton = findViewById(R.id.createAccount_login_activity);
        networkManager = NetworkManager.getInstance();

        //Intent i = new Intent(LoginActivity.this, MainActivity.class);
        //startActivity(i);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = networkManager.executeCommand("login " + login.getText().toString() + " " + password.getText().toString());
                        Log.e("result", result);
                        if (result.equals(Command.LOGIN_OK)){
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            errorField.setText(getResources().getString(R.string.loginError));
                        }
                    }
                }).start();
            }
        });

        createNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = networkManager.executeCommand("register " + login.getText().toString() + " " + password.getText().toString());
                        errorField.setText(result);
                        //Log.e("result", result);
                    }
                }).start();
            }
        });

    }
}