package com.daghlas.myrehab;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Rehabs extends AppCompatActivity {

    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehabs);

        save = findViewById(R.id.save);

        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Add Rehab Center");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Rehabs.this);
                builder.setMessage("Confirm addition of the new rehab?")
                        .setCancelable(false)
                        .setPositiveButton("No", (dialog, id) -> {
                        })
                        .setNegativeButton("Yes", (dialog, id) -> {
                            Intent intent = new Intent(Rehabs.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(Rehabs.this, "Added successfully", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Rehabs.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}