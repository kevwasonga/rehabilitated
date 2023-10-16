package com.daghlas.myrehab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daghlas.myrehab.Adapter.RManagementAdapter;
import com.daghlas.myrehab.Interface.RManagementInterface;
import com.daghlas.myrehab.Model.RModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class RManagement extends AppCompatActivity implements RManagementInterface {

    RecyclerView recyclerView;
    TextView name1, name3, text, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmanagement);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Manage Rehab Centers");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        nameIdEmail();
        name1 = findViewById(R.id.fname);
        name3 = findViewById(R.id.lname);
        text = findViewById(R.id.text);
        text2 = findViewById(R.id.text2);

        text2.setOnClickListener(v -> {
            Intent intent = new Intent(RManagement.this, Rehabs.class);
            startActivity(intent);
            finish();
        });

        //inflating the adapter to the recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        List<RModel> rModelList = new ArrayList<>();
        RManagementAdapter adapter = new RManagementAdapter(this, this, rModelList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("rehabs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RModel rModel = dataSnapshot.getValue(RModel.class);
                    rModelList.add(rModel);

                    if (adapter.getItemCount() == 0) {
                        recyclerView.setVisibility(View.INVISIBLE);
                        text.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        text.setVisibility(View.INVISIBLE);
                        text2.setVisibility(View.INVISIBLE);
                    }
                }adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        Intent intent = new Intent(RManagement.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            Toast.makeText(this, "button to search rehabs on this list..", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

 */

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(RManagement.this, SetRehab.class);
        startActivity(intent);
        finish();
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
}