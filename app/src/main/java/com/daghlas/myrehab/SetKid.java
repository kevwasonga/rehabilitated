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

public class SetKid extends AppCompatActivity {

    Button done;
    TextView full_name, rehab, enrolled, residence, guardian, gender, health, fingerprint, faceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_kid);

        done = findViewById(R.id.done);

        full_name = findViewById(R.id.full_name);
        rehab = findViewById(R.id.allocated_rehab);
        enrolled = findViewById(R.id.date_enrolled);
        residence = findViewById(R.id.residence);
        guardian = findViewById(R.id.guardian);
        gender = findViewById(R.id.gender);
        health = findViewById(R.id.health);
        fingerprint = findViewById(R.id.fingerprint);
        //faceID = findViewById(R.id.face_id);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            full_name.setText(bundle.getString("fullName"));
            rehab.setText(bundle.getString("rehab"));
            enrolled.setText(bundle.getString("date"));
            residence.setText(bundle.getString("residence"));
            guardian.setText(bundle.getString("guardian"));
            gender.setText(bundle.getString("gender"));
            health.setText(bundle.getString("health"));
            fingerprint.setText(bundle.getString("fingerprint"));
            //faceID.setText(bundle.getString("faceID"));
        }

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profile Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        done.setOnClickListener(v -> {
            Intent intent = new Intent(SetKid.this, KManagement.class);
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
        Intent intent = new Intent(SetKid.this, KManagement.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}