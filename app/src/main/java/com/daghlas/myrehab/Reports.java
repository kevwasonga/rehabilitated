package com.daghlas.myrehab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daghlas.myrehab.Adapter.RAdapter;
import com.daghlas.myrehab.Interface.RInterface;
import com.daghlas.myrehab.Model.KModel;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.List;

public class Reports extends AppCompatActivity implements RInterface {

    RecyclerView recyclerView;
    TextView name1, name3;
    RAdapter adapter;
    List<KModel> kModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Rehab Report");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        nameIdEmail();
        name1 = findViewById(R.id.fname);
        name3 = findViewById(R.id.lname);

        //inflating the adapter to the recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        kModelList = new ArrayList<>();
        adapter = new RAdapter(this, this, kModelList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("enrolled");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    KModel kModel = dataSnapshot.getValue(KModel.class);
                    kModelList.add(kModel);

                    if (adapter.getItemCount() == 0) {
                        recyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                adapter.notifyDataSetChanged();
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
        Intent intent = new Intent(Reports.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(Reports.this, SetKid.class);
        startActivity(intent);
        finish();
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