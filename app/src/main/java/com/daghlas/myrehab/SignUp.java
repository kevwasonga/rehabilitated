package com.daghlas.myrehab;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    Button signUp;
    TextView login_here, cancel;
    EditText first_name, last_name, id, email, password, confirm;
    ProgressBar progressBar;
    //Firebase
    FirebaseAuth mAuth;
    String userID;
    FirebaseUser mUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        signUp = findViewById(R.id.signup_btn);
        login_here = findViewById(R.id.loginS);
        cancel = findViewById(R.id.cancelS);
        progressBar = findViewById(R.id.progress);
        first_name = findViewById(R.id.firstName);
        last_name = findViewById(R.id.lastName);
        id = findViewById(R.id.id);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);

        //go to main activity when log in button is clicked
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateFirstName() | !validateLatName() | !validateNationalID() | !validateMail() | !validatePwd() | !validateConfirmPwd()){
                    progressBar.setVisibility(View.GONE);
                    signUp.setVisibility(View.VISIBLE);
                }else {
                    signUpUser();
                }
            }
        });

        //go to main activity when log in button is clicked
        login_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        //go to main activity when log in button is clicked
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public boolean validateFirstName(){
        String name = first_name.getText().toString().trim();
        if(name.isEmpty()){
            first_name.setError("Field cannot be empty");
            return false;
        } else {
            first_name.setError(null);
            return true;
        }
    }
    public boolean validateLatName(){
        String name = last_name.getText().toString().trim();
        if(name.isEmpty()){
            last_name.setError("Field cannot be empty");
            return false;
        } else {
            last_name.setError(null);
            return true;
        }
    }
    public boolean validateNationalID(){
        String idS = id.getText().toString().trim();
        if(idS.isEmpty()){
            id.setError("Field cannot be empty");
            return false;
        } else if (idS.length() < 8) {
            id.setError("Enter valid ID");
            return false;
        } else {
            id.setError(null);
            return true;
        }
    }
    private boolean validateMail() {
        String val = email.getText().toString();
        String pattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(pattern)) {
            email.setError("Enter valid email");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
    private boolean validatePwd() {
        String val = password.getText().toString();
        String pattern = "[a-zA-Z\\d!?@#$%^&*()_+/|~-]{8,24}";
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(pattern)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPwd() {
        String val = confirm.getText().toString();
        String val1 = password.getText().toString();

        if (val.isEmpty()) {
            confirm.setError("Field cannot be empty");
            return false;
        } else if (!val.equals(val1)) {
            confirm.setError("Password does not match!");
            return false;
        } else {
            confirm.setError(null);
            return true;
        }
    }



    //go back to log in activity when back button is pressed
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        finish();
    }

    public void signUpUser(){

        progressBar.setVisibility(View.VISIBLE);
        signUp.setVisibility(View.INVISIBLE);
        //Signing in new user
        String emailS = email.getText().toString().trim();
        String passwordS = password.getText().toString().trim();
        //send user details to firestore
        String firstNameS = first_name.getText().toString().trim();
        String lastNameS = last_name.getText().toString().trim();
        String idS = id.getText().toString().trim();

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mAuth.createUserWithEmailAndPassword(emailS, passwordS)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firebaseFirestore.collection("USERS").document(userID);

                            Map<String, Object> user = new HashMap<>();

                            user.put("firstName", firstNameS);
                            user.put("lastName", lastNameS);
                            user.put("nationalID", idS);
                            user.put("email", emailS);
                            user.put("password", passwordS);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.R)
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"onSuccess: Successfully registered user: " +userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("FireStoreDataError:", e.getMessage());
                                }
                            });

                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(SignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.w("Sign up error:", task.getException());
                            Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}