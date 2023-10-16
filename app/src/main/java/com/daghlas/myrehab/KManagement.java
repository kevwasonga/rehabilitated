package com.daghlas.myrehab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daghlas.myrehab.Adapter.KManagementAdapter;
import com.daghlas.myrehab.Interface.KManagementInterface;
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

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class KManagement extends AppCompatActivity implements KManagementInterface {

    RecyclerView recyclerView;
    TextView name1, name3, text, text2;
    KManagementAdapter adapter;
    List<KModel> kModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kmanagement);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manage Enrolled Kids");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        nameIdEmail();
        name1 = findViewById(R.id.fname);
        name3 = findViewById(R.id.lname);
        text = findViewById(R.id.text);
        text2 = findViewById(R.id.text2);

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KManagement.this, Enrollment.class);
                startActivity(intent);
                finish();
            }
        });

        //inflating the adapter to the recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        kModelList = new ArrayList<>();
        adapter = new KManagementAdapter(this, this, kModelList);
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
                        text.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        text.setVisibility(View.INVISIBLE);
                        text2.setVisibility(View.INVISIBLE);
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
        Intent intent = new Intent(KManagement.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(KManagement.this, SetKid.class);
        startActivity(intent);
        finish();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

 */

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