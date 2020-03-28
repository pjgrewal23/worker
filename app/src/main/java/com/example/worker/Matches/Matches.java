package com.example.worker.Matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.worker.LoginActivity;
import com.example.worker.LoginRegistrationActivity;
import com.example.worker.MainActivity;
import com.example.worker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Matches extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recycler = findViewById(R.id.recycler);
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(Matches.this);
        recycler.setLayoutManager(layoutManager);

        adapter = new matchAdapter(getDataSetMatches(), Matches.this);
        recycler.setAdapter(adapter);

        getUserMatchId();

        adapter.notifyDataSetChanged();
    }

    private void getUserMatchId() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("connections").child("match");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot match: dataSnapshot.getChildren()){
                        fetchMatchInfo(match.getKey());
                    }
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchMatchInfo(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name ="";
                    String phone = "";
                    String description = "";
                    String profilepicURL = "";

                    if(dataSnapshot.child("name").getValue() != null){
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if(dataSnapshot.child("phone").getValue() != null){
                        phone = dataSnapshot.child("phone").getValue().toString();
                    }
                    if(dataSnapshot.child("description").getValue() != null){
                        description = dataSnapshot.child("description").getValue().toString();
                    }
                    if(dataSnapshot.child("profilepicURL").getValue() != null){
                        profilepicURL = dataSnapshot.child("profilepicURL").getValue().toString();
                    }

                    matchesObject obj = new matchesObject(name, phone, description, profilepicURL);
                    resultMatches.add(obj);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<matchesObject> resultMatches = new ArrayList<matchesObject>();
    private List<matchesObject> getDataSetMatches() {
        return resultMatches;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Matches.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
