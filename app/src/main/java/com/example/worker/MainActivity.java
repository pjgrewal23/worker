package com.example.worker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.worker.Cards.arrayAdapter;
import com.example.worker.Cards.cards;
import com.example.worker.Matches.Matches;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private cards card_data[];
    private com.example.worker.Cards.arrayAdapter arrayAdapter;

    private FirebaseAuth firebaseAuth;

    private String currentUid;
    private String userType;
    private String searchType;
    //

    private DatabaseReference userDb;


    ListView listview;
    List<cards> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        userList = new ArrayList<cards>();
        checkUserType();

        currentUid = firebaseAuth.getCurrentUser().getUid();
        arrayAdapter = new arrayAdapter(this, R.layout.item, userList );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                userList.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                cards obj = (cards)dataObject;
                String uid = obj.getUid();
                userDb.child(uid).child("connections").child("rejected").child(currentUid).setValue(true);
                Toast.makeText(MainActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards)dataObject;
                String uid = obj.getUid();
                userDb.child(uid).child("connections").child("approved").child(currentUid).setValue(true);
                isConnectionFormed(uid);
                Toast.makeText(MainActivity.this, "Approved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }

        });
        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //Toast.makeText(MainActivity.this,"CLICKED!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void isConnectionFormed(String uid) {
        DatabaseReference connectionDb = userDb.child(currentUid).child("connections").child("approved").child(uid);
        connectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this, "New Connection", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                    userDb.child(dataSnapshot.getKey()).child("connections").child("match").child(currentUid).child("chatId").setValue(key);
                    userDb.child(currentUid).child("connections").child("match").child(dataSnapshot.getKey()).child("chatId").setValue(key);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void checkUserType(){
        
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        
        DatabaseReference db = userDb.child(user.getUid());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(user.getUid())){
                    if (dataSnapshot.exists()){
                        if(dataSnapshot.child("userType").getValue() != null){
                            userType = dataSnapshot.child("userType").getValue().toString();
                            switch (userType){
                                case "Worker":
                                    searchType = "Customer";
                                    break;
                                case "Customer":
                                    searchType = "Worker";
                                    break;
                            }
                            getSearchTypeUsers();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }
    
    public void getSearchTypeUsers(){
        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { 
                if(dataSnapshot.exists() && !dataSnapshot.child("connections").child("rejected").hasChild(currentUid) && !dataSnapshot.child("connections").child("approved").hasChild(currentUid) && dataSnapshot.child("userType").getValue().toString().equals(searchType)){
                    String pictureURL = "default";

                    if(!dataSnapshot.child("profilepicURL").getValue().equals(pictureURL)){
                        pictureURL = dataSnapshot.child("profilepicURL").getValue().toString();
                    }
                        cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), pictureURL);
                        userList.add(item);
                        arrayAdapter.notifyDataSetChanged();

                    //userList.add(new cards(dataSnapshot.getKey(), dd.child("name").getValue().toString()));



                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void logOutUser(View view) {
        firebaseAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginRegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
        finish();

    }

    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, Matches.class);
        startActivity(intent);
        finish();
    }
}
