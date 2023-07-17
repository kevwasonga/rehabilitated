package com.daghlas.myrehab;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class Enrollment extends AppCompatActivity {

    TextView biometric, faceID, name1, name3, date;
    EditText full_name, residence, guardian, health;
    AutoCompleteTextView rehab, gender;
    ArrayAdapter<String> categoryItemsGender;
    ArrayAdapter<String> categoryItemsRehab;
    String[] categoriesGender = {"Male", "Female", "Other"};
    String[] categoriesRehab = {"REHAB 1", "REHAB 2", "REHAB 3"};
    Button save;
    CheckBox fingerprint, face;
    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;


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

        date = findViewById(R.id.date);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(new Date());
        date.setText(currentDate);//read current date
        date.setVisibility(View.INVISIBLE);

        full_name = findViewById(R.id.full_name);
        residence = findViewById(R.id.residence);
        guardian = findViewById(R.id.guardian);
        health = findViewById(R.id.health);
        health = findViewById(R.id.health);
        biometric = findViewById(R.id.scanPrint);
        fingerprint = findViewById(R.id.checkboxFinger);

        //Read available rehabs from database and populate to drop down list
        rehab = findViewById(R.id.rehabCenter);
        categoryItemsRehab = new ArrayAdapter<>(this, R.layout.category_list);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("rehabs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String rehabs = dataSnapshot.child("rehabName").getValue(String.class);
                    categoryItemsRehab.add(rehabs);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rehab.setAdapter(categoryItemsRehab);

        gender = findViewById(R.id.gender);
        categoryItemsGender = new ArrayAdapter<>(this, R.layout.category_list, categoriesGender);
        gender.setAdapter(categoryItemsGender);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Enrollment.this);
                builder.setMessage("Do you want to confirm enrollment?")
                        .setCancelable(false)
                        .setPositiveButton("No", (dialog, id) -> {
                        })
                        .setNegativeButton("Yes", (dialog, id) -> {
                            if (!validateName() | !validateRehab() | !validateGender() | !validateGuardian() | !validateHealth() | !validateResidence() | !validateBiometric()) {
                                Toast.makeText(Enrollment.this, "all fields are required", Toast.LENGTH_SHORT).show();
                            } else {
                                saveRealTime();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //checkboxes
        fingerprint.setEnabled(false);
        //biometrics auth
        setBiometric();
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

    public void saveRealTime() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String name = full_name.getText().toString().trim();
        String reside = residence.getText().toString().trim();
        String guard = guardian.getText().toString().trim();
        String heal = health.getText().toString().trim();
        String dateToday = date.getText().toString().trim();

        String rehabilitation = rehab.getText().toString().trim();
        String genders = gender.getText().toString().trim();
        String fingerprint, faceID = null;

        List<String> data = new ArrayList<>();
        data.add(name);
        data.add(reside);
        data.add(guard);
        data.add(heal);
        data.add(dateToday);
        data.add(rehabilitation);
        data.add(genders);

        DatabaseReference databaseRef = database.getReference("enrolled");

        Map<String, Object> entries = new HashMap<>();
        entries.put("fullName", name);
        entries.put("residence", reside);
        entries.put("guardian", guard);
        entries.put("health", heal);
        entries.put("dateEnrolled", dateToday);
        entries.put("rehab", rehabilitation);
        entries.put("gender", genders);
        entries.put("fingerprint", "COMPLETE");
        entries.put("faceID", "pending");

        databaseRef.child(name).updateChildren(entries)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onUploadSuccess: Successfully enrolled " + name);
                        Toast.makeText(Enrollment.this, "Successfully enrolled " + name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Enrollment.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("onUploadFailure: ", e.getMessage());
                        Toast.makeText(Enrollment.this, "Could not enroll " + name, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //VALIDATION CHECKS
    public boolean validateName() {
        String name = full_name.getText().toString().trim();
        if (name.isEmpty()) {
            full_name.setError("Enter full name");
            return false;
        } else {
            full_name.setError(null);
            return true;
        }
    }
    public boolean validateResidence() {
        String name = residence.getText().toString().trim();
        if (name.isEmpty()) {
            residence.setError("Enter residence");
            return false;
        } else {
            residence.setError(null);
            return true;
        }
    }
    public boolean validateGuardian() {
        String name = guardian.getText().toString().trim();
        if (name.isEmpty()) {
            guardian.setError("Enter guardian's name");
            return false;
        } else {
            guardian.setError(null);
            return true;
        }
    }
    public boolean validateRehab() {
        String name = rehab.getText().toString().trim();
        if (name.isEmpty()) {
            rehab.setError(null);
            return false;
        } else {
            rehab.setError(null);
            return true;
        }
    }
    public boolean validateGender() {
        String name = gender.getText().toString().trim();
        if (name.isEmpty()) {
            gender.setError(null);
            return false;
        } else {
            gender.setError(null);
            return true;
        }
    }
    public boolean validateHealth() {
        String name = health.getText().toString().trim();
        if (name.isEmpty()) {
            health.setError("Enter health details");
            return false;
        } else {
            health.setError(null);
            return true;
        }
    }
    public boolean validateBiometric() {
        if (!fingerprint.isChecked()) {
            fingerprint.setError("");
            return false;
        } else {
            fingerprint.setError(null);
            return true;
        }
    }

    //BIOMETRIC VALIDATION
    public void setBiometric() {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(Enrollment.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                fingerprint.setChecked(true);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Confirm child enrollment")
                .setSubtitle("Scan fingerprint to authenticate child enrollment")
                .setNegativeButtonText("Use account password")
                .build();

        //fingerprint validation
        biometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }
}