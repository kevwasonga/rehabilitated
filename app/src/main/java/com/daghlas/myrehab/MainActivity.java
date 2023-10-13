package com.daghlas.myrehab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class MainActivity extends AppCompatActivity {

    TextView greetings;
    TextView name1, name3, email, id, tag;
    LinearLayout enroll_kid, new_rehab, manage_kids, manage_rehabs, rehab_report;
    //Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        nameIdEmail();

        //greeting Tag
        greetings = findViewById(R.id.greetingTag);
        //Greeting tag - set according to time of day
        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.HOUR_OF_DAY);

        if (time < 12) {
            greetings.setText(R.string.good_morning);
        } else if (time < 16) {
            greetings.setText(R.string.good_afternoon);
        } else if (time < 21) {
            greetings.setText(R.string.good_evening);
        } else {
            greetings.setText(R.string.good_night);
        }

        enroll_kid = findViewById(R.id.layout1);
        new_rehab = findViewById(R.id.layout2);
        manage_kids = findViewById(R.id.layout3);
        manage_rehabs = findViewById(R.id.layout4);
        rehab_report = findViewById(R.id.layout5);

        name1 = findViewById(R.id.fname);
        name3 = findViewById(R.id.lname);
        email = findViewById(R.id.mail);
        id = findViewById(R.id.id);
        tag = findViewById(R.id.nameTag);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        enroll_kid.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Enrollment.class);
            startActivity(intent);
            finish();
        });
        new_rehab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Rehabs.class);
            startActivity(intent);
            finish();
        });
        manage_kids.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, KManagement.class);
            startActivity(intent);
            finish();
        });
        manage_rehabs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RManagement.class);
            startActivity(intent);
            finish();
        });
        rehab_report.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Reports.class);
            startActivity(intent);
            finish();
        });

        //action bar title & back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Kevin's REHAB App");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    //action bar back button implementation
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //ask user to confirm if they want to exit app when back button is pressed
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //builder.setTitle(R.string.app_name);
        builder.setMessage("Are you sure you want to exit the app?")
                .setCancelable(false)
                .setPositiveButton("No", (dialog, id) -> {
                })
                .setNegativeButton("Yes", (dialog, id) -> {
                    System.exit(0);
                    finish();
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    //inflating options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //what happens when menu item is selected

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.refresh) {
            Toast.makeText(this, "Refreshing..", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Log out of account?")
                    .setCancelable(false)
                    .setPositiveButton("No", (dialog, id) -> {
                    })
                    .setNegativeButton("Yes", (dialog, id) -> {
                        //CLEAR CREDENTIALS
                        SharedPreferences sharedPreferences = getSharedPreferences("sessionManager", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("email");
                        editor.remove("password");
                        editor.apply();
                        //END CLEAR
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(this, "Logged out.", Toast.LENGTH_SHORT).show();
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
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
            email.setText(value.getString("email"));
            id.setText(value.getString("nationalID"));
            tag.setText(value.getString("lastName"));
        });
    }
}