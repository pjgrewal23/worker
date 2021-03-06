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
import com.example.worker.SettingsCustomer;
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

    private   int select;
    private  String TaskId;
    private String otherId;
    private String yN;
    private String custPhotoURL;
    private String custDes;
    private String task1;
    private String task2;
    private String task3;
    private String t1;
    private String t2;
    private String t3;


    private String forCust;
    private DatabaseReference userDb;
    private DatabaseReference TaskDb;


    ListView listview;
    List<cards> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        userDb = FirebaseDatabase.getInstance().getReference().child("Users");

        TaskDb = FirebaseDatabase.getInstance().getReference().child("task");
        userList = new ArrayList<cards>();
        currentUid = firebaseAuth.getCurrentUser().getUid();
        arrayAdapter = new arrayAdapter(this, R.layout.item, userList );
        checkUserType();
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
                yN = "n";
                otherId = uid;
                if(searchType.equals("Customer")){

                    //customerDbUpdate(uid,currentUid);
                    userDb.child(uid).child("connections").child("rejected").child(obj.getKey()).setValue(true);
                }
                else{

                    TaskDb.child(obj.getKey()).child("connections").child("rejected").child(otherId).setValue(true);
                }
                Toast.makeText(MainActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards)dataObject;
                String uid = obj.getUid();

                Log.i("Right", "dsf");
                yN = "y";
                otherId = uid;
                Log.i("Right", uid);
                if(searchType.equals("Customer")){
                    TaskId = obj.getKey();
                    //customerDbUpdate(uid,currentUid);
                    TaskDb.child(TaskId).child("connections").child("approved").child(currentUid).setValue(true);
                    //userDb.child(uid).child("connections").child("approved").child(obj.getKey()).setValue(true);
                    isConnectionFormed(uid);
                }
                else{
                    Log.i("task", TaskId);
                    //customerDbUpdate();
                    Log.i("sdfasdf", "dsf");
                    //Log.i("key", TaskId);

                    //TaskDb.child(TaskId).child("connections").child("approved").child(otherId).setValue(true);
                    userDb.child(uid).child("connections").child("approved").child(TaskId).setValue(true);
                    isConnectionFormed(TaskId);
                }

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
        DatabaseReference connectionDb;
        Log.i("passed", uid);
        if(userType.equals("Worker")){
            Log.i("Worker", uid);
            Log.i("other", otherId);
            connectionDb = userDb.child(currentUid).child("connections").child("approved").child(TaskId);
        }
        else{
            Log.i("Customer", uid);
            Log.i("other", otherId);
            connectionDb = TaskDb.child(TaskId).child("connections").child("approved").child(otherId);
        }
        connectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this, "New Connection", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                    Log.i("curent",currentUid);
                    Log.i("other", otherId);
                    userDb.child(otherId).child("connections").child("match").child(currentUid).child("chatId").setValue(key);
                    userDb.child(currentUid).child("connections").child("match").child(otherId).child("chatId").setValue(key);

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
                            if(userType.equals("Customer")){
                                findTask();
                            }
                            else{
                                getSearchTypeUsers();
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private void findTask() {
        DatabaseReference numDb = userDb.child(currentUid).child("selectedNum");
        numDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    select = Integer.parseInt(dataSnapshot.getValue().toString());
                    getTaskIdSearch(select);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void getTaskIdSearch(int select) {
        DatabaseReference numDb;
        if(select == 1){
            numDb = userDb.child(currentUid).child("t1Id");
        }
        else if(select ==2){
            numDb = userDb.child(currentUid).child("t2Id");
        }
        else{
            numDb = userDb.child(currentUid).child("t3Id");
        }

        numDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    TaskId = dataSnapshot.getValue().toString();
                    getSearchTypeUsers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getSearchTypeUsers(){
        Log.i("get MidleCard", "");
        if(userType.equals("Customer")) {
            Log.i("get  Cust Card", "");
            userDb.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(TaskId != null) {
                        if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("rejected").hasChild(TaskId) && !dataSnapshot.child("connections").child("approved").hasChild(TaskId) && dataSnapshot.child("userType").getValue().toString().equals(searchType)) {
                            String pictureURL = "default";
                            if (!dataSnapshot.child("profilepicURL").getValue().equals(pictureURL)) {
                                pictureURL = dataSnapshot.child("profilepicURL").getValue().toString();
                            }
                            cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), pictureURL, "");
                            userList.add(item);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
        else {
            Log.i("get work Card", "");
            userDb.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists() && dataSnapshot.child("userType").getValue().toString().equals(searchType)) {
                        String pictureURL = "default";
                        if (!dataSnapshot.child("profilepicURL").getValue().equals(pictureURL)) {
                            pictureURL = dataSnapshot.child("profilepicURL").getValue().toString();
                        }
                        custPhotoURL = pictureURL;

                        if(dataSnapshot.child("t1").getValue() != null){
                            t1 = dataSnapshot.child("t1").getValue().toString();
                            Log.i("non", "empty1");
                        }
                        else{
                            t1 = "";
                        }
                        if(dataSnapshot.child("t2").getValue() != null){
                            t2 = dataSnapshot.child("t2").getValue().toString();

                        }
                        else{
                            t2 = "";
                        }
                        if(dataSnapshot.child("t3").getValue() != null){
                            t3 = dataSnapshot.child("t3").getValue().toString();
                        }
                        else{
                            t3 = "";
                        }
                         forCust = dataSnapshot.getKey();
                        if(t1 != null && !t1.isEmpty()){
                            Log.i("tup1", t1);
                            final String st1 = t1;
                            task1 =  dataSnapshot.child("t1Id").getValue().toString();
                            DatabaseReference t = TaskDb.child(task1);
                            Log.i("t1", t1);

                            t.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists() && !dataSnapshot.child("connections").child("approved").hasChild(currentUid) && !dataSnapshot.child("connections").child("rejected").hasChild(currentUid)){

                                        cards item = new cards(forCust, dataSnapshot.child("name").getValue().toString(), custPhotoURL,task1);
                                        userList.add(item);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        if(t2 != null && !t2.isEmpty()){

                            task2 =  dataSnapshot.child("t2Id").getValue().toString();
                            DatabaseReference t = TaskDb.child(task2);
                            final String st2 = t2;
//
                            t.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists() && !dataSnapshot.child("connections").child("approved").hasChild(currentUid) && !dataSnapshot.child("connections").child("rejected").hasChild(currentUid)){

                                        cards item = new cards(forCust, st2, custPhotoURL,task2);
                                        userList.add(item);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        if(t3 != null && !t3.isEmpty()){

                            task3 =  dataSnapshot.child("t3Id").getValue().toString();
                            DatabaseReference t = TaskDb.child(task3);
                            final String st3 = t3;
//
                            t.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists() && !dataSnapshot.child("connections").child("approved").hasChild(currentUid) && !dataSnapshot.child("connections").child("rejected").hasChild(currentUid)){

                                        cards item = new cards(forCust, st3, custPhotoURL,task3);
                                        userList.add(item);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

        }
    }
    public void logOutUser(View view) {
        firebaseAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginRegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSettings(View view) {

        if(userType == null) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
        } else {
            if (!userType.equals("Worker")) {
                Intent intent = new Intent(MainActivity.this, SettingsCustomer.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        }
        finish();


    }

    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, Matches.class);
        startActivity(intent);
        finish();
    }
}
