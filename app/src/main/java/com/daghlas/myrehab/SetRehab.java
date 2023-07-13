package com.daghlas.myrehab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class SetRehab extends AppCompatActivity {

    TextView rehab_name, director, date_added, location, email,phone, donations;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rehab);

        rehab_name = findViewById(R.id.full_name);
        director = findViewById(R.id.allocated_rehab);
        date_added = findViewById(R.id.date_enrolled);
        location = findViewById(R.id.location);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        donations = findViewById(R.id.donations);

        done = findViewById(R.id.done);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            rehab_name.setText(bundle.getString("rehabName"));
            director.setText(bundle.getString("rehabDirector"));
            date_added.setText(bundle.getString("date"));
            location.setText(bundle.getString("rehabLocation"));
            email.setText(bundle.getString("rehabEmail"));
            phone.setText(bundle.getString("rehabPhone"));
            donations.setText(bundle.getString("rehabPhone"));
        }

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Profile Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        done.setOnClickListener(v -> {
            Intent intent = new Intent(SetRehab.this, RManagement.class);
            startActivity(intent);
            finish();
        });
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