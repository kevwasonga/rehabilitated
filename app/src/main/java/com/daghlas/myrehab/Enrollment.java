package com.daghlas.myrehab;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Enrollment extends AppCompatActivity {

    TextView biometric, faceID;
    Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Enrollment");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Enrollment.this);
                builder.setMessage("Do you want to confirm enrollment?")
                        .setCancelable(false)
                        .setPositiveButton("No", (dialog, id) -> {
                        })
                        .setNegativeButton("Yes", (dialog, id) -> {
                            Intent intent = new Intent(Enrollment.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(Enrollment.this, "Enrollment successful", Toast.LENGTH_SHORT).show();
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Enrollment.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    //biometric method

}