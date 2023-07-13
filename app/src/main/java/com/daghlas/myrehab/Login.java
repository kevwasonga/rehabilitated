package com.daghlas.myrehab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class Login extends AppCompatActivity {

    Button login;
    ProgressBar progressBar;
    TextView reset_password, register_account;
    EditText email, password;
    //Firebase
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress);
        reset_password = findViewById(R.id.forgotpass);
        register_account = findViewById(R.id.register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        autoLogin();

        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        //go to main activity when log in button is clicked
        login.setOnClickListener(v -> {
            if (!validateEmail() | !validatePwd()) {
                progressBar.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
            } else {
                logIN();
            }
        });

        //sign up activity
        register_account.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
            finish();
        });

        //reset password activity
        reset_password.setOnClickListener(v -> Toast.makeText(Login.this, "Contact admin", Toast.LENGTH_SHORT).show());
    }

    public boolean validateEmail() {
        String val = email.getText().toString().trim();
        if (val.isEmpty()) {
            email.setError("Enter email");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    public boolean validatePwd() {
        String val = password.getText().toString().trim();
        if (val.isEmpty()) {
            password.setError("Enter password");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void logIN() {
        progressBar.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        //logging in
        String emailS = email.getText().toString().trim();
        String passwordS = password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(emailS, passwordS)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //SAVE CREDENTIALS
                        SharedPreferences sharedPreferences = getSharedPreferences("sessionManager", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", emailS);
                        editor.putString("password", passwordS);
                        editor.apply();
                        //END SAVE
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w("Login Error:", task.getException());
                        Toast.makeText(Login.this, "wrong email/password", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void autoLogin() {
        //retrieve values from stored preference
        SharedPreferences sharedPreferencesLogin = getSharedPreferences("sessionManager", Context.MODE_PRIVATE);
        //read values from stored preference
        String emailPref = sharedPreferencesLogin.getString("email", "");
        String passPref = sharedPreferencesLogin.getString("password", "");

        if (!emailPref.isEmpty() && !passPref.isEmpty()) {
            // User is already logged in, redirect on Main activity
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}