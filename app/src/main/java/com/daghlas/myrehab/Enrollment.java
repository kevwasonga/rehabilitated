package com.daghlas.myrehab;

import androidx.annotation.Nullable;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Enrollment extends AppCompatActivity {

    TextView biometric, faceID, name1, name3;
    Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Enrollment");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        nameIdEmail();

        save = findViewById(R.id.save);
        name1 = findViewById(R.id.fname);
        name3 = findViewById(R.id.lname);

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

    public void nameIdEmail() {
        //database
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("USERS").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                name1.setText(value.getString("firstName"));
                name3.setText(value.getString("lastName"));
            }
        });
    }
}