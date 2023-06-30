package com.daghlas.myrehab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

public class SetRehab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rehab);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Profile Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SetRehab.this, RManagement.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}