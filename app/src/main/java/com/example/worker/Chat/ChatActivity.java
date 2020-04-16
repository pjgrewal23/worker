package com.example.worker.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.worker.Matches.Matches;
import com.example.worker.Matches.matchAdapter;
import com.example.worker.Matches.matchesObject;
import com.example.worker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private EditText sendT;
    private Button sendB;

    private String currentUser, matchId, chatId;

    DatabaseReference dbUser, dbChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        matchId = getIntent().getExtras().getString("matchText");
        sendT = findViewById(R.id.msg);
        sendB = findViewById(R.id.sendBtn);


        Log.i("id", matchId);
        dbUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("connections").child("match").child(matchId).child("chatId");
        dbChat = FirebaseDatabase.getInstance().getReference().child("chat");

        Log.i("DB", dbUser.getKey());

        getChatId();


        recycler = findViewById(R.id.recyclerChat);
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(ChatActivity.this);
        recycler.setLayoutManager(layoutManager);

        chatAdapter = new chatAdapter(getDataSetChat(), ChatActivity.this);
        recycler.setAdapter(chatAdapter);

        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }


    private void sendMessage(){
        String mText = sendT.getText().toString();

        if(!mText.isEmpty()){
            DatabaseReference newMessageDb = dbChat.push();

            Map newMessage = new HashMap();
            Log.i("sendMessage", "errrrrrrrrr");

            newMessage.put("createdByUser", currentUser);
            newMessage.put("text", mText);

            newMessageDb.setValue(newMessage);
        }

        sendT.setText(null);
    }

    private void getChatId(){

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    dbChat = dbChat.child(chatId);
                    getChatMessages();
                }else{
                    Log.i("getChatID", "error");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getChatMessages() {
        dbChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String message = null, createdBy = null;

                    if(dataSnapshot.child("text").getValue() != null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }

                    if(dataSnapshot.child("createdByUser").getValue() != null){
                        createdBy = dataSnapshot.child("createdByUser").getValue().toString();
                    }

                    if(message != null && createdBy != null){
                        Boolean currentUserBoolean =false;
                        if(createdBy.equals(currentUser)){
                            currentUserBoolean = true;
                        }
                        chatObject newMsg = new chatObject(message, currentUserBoolean);
                        resultChat.add(newMsg);
                        chatAdapter.notifyDataSetChanged();
                    }
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

    private ArrayList<chatObject> resultChat = new ArrayList<chatObject>();
    private List<chatObject> getDataSetChat() {
        return resultChat;
    }
}
