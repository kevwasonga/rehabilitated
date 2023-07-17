package com.daghlas.myrehab;

import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class Rehabs extends AppCompatActivity {

    Button save;
    TextView name1, name3, date;

    EditText rehab_name, location, rehab_director, rehab_email, rehab_phone;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehabs);

        nameIdEmail();

        save = findViewById(R.id.save);
        name1 = findViewById(R.id.fname);
        name3 = findViewById(R.id.lname);

        rehab_name = findViewById(R.id.full_name);
        location = findViewById(R.id.location);
        rehab_director = findViewById(R.id.director);
        rehab_email = findViewById(R.id.contact_mail);
        rehab_phone = findViewById(R.id.contact);

        date = findViewById(R.id.date);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(new Date());
        date.setText(currentDate);//read current date
        date.setVisibility(View.INVISIBLE);

        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Add Rehab Center");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        save.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Rehabs.this);
            builder.setMessage("Confirm addition of the new rehab?")
                    .setCancelable(false)
                    .setPositiveButton("No", (dialog, id) -> {
                    })
                    .setNegativeButton("Yes", (dialog, id) -> {
                        if (!validateName() | !validateLocation() | !validateDirector() | !validateEmail()| !validatePhone()) {
                            Toast.makeText(Rehabs.this, "all fields are required", Toast.LENGTH_SHORT).show();
                        } else {
                            saveRealTime();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
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

    public void nameIdEmail() {
        //database
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("USERS").document(userID);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            name1.setText(value.getString("firstName"));
            name3.setText(value.getString("lastName"));
        });
    }

    public boolean validateName() {
        String name = rehab_name.getText().toString().trim();
        if (name.isEmpty()) {
            rehab_name.setError("Enter rehab's name");
            return false;
        } else {
            rehab_name.setError(null);
            return true;
        }
    }
    public boolean validateLocation() {
        String name = location.getText().toString().trim();
        if (name.isEmpty()) {
            location.setError("Enter rehab's location");
            return false;
        } else {
            location.setError(null);
            return true;
        }
    }
    public boolean validateDirector() {
        String name = rehab_director.getText().toString().trim();
        if (name.isEmpty()) {
            rehab_director.setError("Enter director's name");
            return false;
        } else {
            rehab_director.setError(null);
            return true;
        }
    }
    public boolean validateEmail() {
        String name = rehab_email.getText().toString().trim();
        String pattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+.+[a-z]+";
        if (name.isEmpty()) {
            rehab_email.setError("Enter rehab's email address");
            return false;
        } else if(!name.matches(pattern)) {
            rehab_email.setError("Enter valid email");
            return false;
        } else {
            rehab_email.setError(null);
            return true;
        }
    }
    private boolean validatePhone() {
        String val = rehab_phone.getText().toString();
        String pattern = "^(07|01)[0-9]{8}$";
        if (val.isEmpty()) {
            rehab_phone.setError("Enter rehab's contact number");
            return false;
        } else if (!val.matches(pattern)) {
            rehab_phone.setError("Enter valid Phone number");
            return false;
        } else {
            rehab_phone.setError(null);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void saveRealTime() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String name = rehab_name.getText().toString().trim();
        String reside = location.getText().toString().trim();
        String ceo = rehab_director.getText().toString().trim();
        String mail = rehab_email.getText().toString().trim();
        String phone = rehab_phone.getText().toString().trim();
        String dateToday = date.getText().toString().trim();

        List<String> data = new ArrayList<>();
        data.add(name);
        data.add(reside);
        data.add(ceo);
        data.add(mail);
        data.add(phone);
        data.add(dateToday);

        DatabaseReference databaseRef = database.getReference("rehabs");

        Map<String, Object> entries = new HashMap<>();
        entries.put("rehabName", name);
        entries.put("rehabLocation", reside);
        entries.put("rehabDirector", ceo);
        entries.put("rehabEmail", mail);
        entries.put("rehabPhone", phone);
        entries.put("dateAdded", dateToday);

        databaseRef.child(name).updateChildren(entries)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "onUploadSuccess: Successfully enrolled " + name);
                    Toast.makeText(Rehabs.this, "Successfully added " + name, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Rehabs.this, MainActivity.class);

                    intent.putExtra("rehabName", name);
                    intent.putExtra("rehabLocation", reside);
                    intent.putExtra("rehabDirector", ceo);
                    intent.putExtra("rehabEmail", mail);
                    intent.putExtra("rehabPhone", phone);
                    intent.putExtra("dateAdded", dateToday);

                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w("onUploadFailure: ", e.getMessage());
                    Toast.makeText(Rehabs.this, "Could not add " + name, Toast.LENGTH_SHORT).show();
                });
    }
}