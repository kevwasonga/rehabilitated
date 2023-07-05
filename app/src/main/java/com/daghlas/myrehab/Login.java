package com.daghlas.myrehab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
        mAuth= FirebaseAuth.getInstance();

        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        //go to main activity when log in button is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail() | !validatePwd()){
                    progressBar.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                }else{
                    logIN();
                }
            }
        });

        //sign up activity
        register_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        //reset password activity
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Contact admin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validateEmail(){
        String val = email.getText().toString().trim();
        if(val.isEmpty()){
            email.setError("Enter email");
            return false;
        }else {
            email.setError(null);
            return true;
        }
    }
    public boolean validatePwd(){
        String val = password.getText().toString().trim();
        if(val.isEmpty()){
            password.setError("Enter password");
            return false;
        }else {
            password.setError(null);
            return true;
        }
    }

    public void logIN(){
        progressBar.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        //logging in
        String emailS = email.getText().toString().trim();
        String passwordS = password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(emailS, passwordS)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.w("Login Error:", task.getException());
                            Toast.makeText(Login.this, "wrong email/password", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}