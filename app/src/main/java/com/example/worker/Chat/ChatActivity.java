package com.example.worker.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.worker.Matches.Matches;
import com.example.worker.Matches.matchAdapter;
import com.example.worker.Matches.matchesObject;
import com.example.worker.R;
import com.google.firebase.auth.FirebaseAuth;
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
    private RecyclerView.Adapter adapter;
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

        dbUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("connections").child("matches").child("chatId");
        dbChat = FirebaseDatabase.getInstance().getReference().child("chat");

        getChatId();


        recycler = findViewById(R.id.recycler);
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(ChatActivity.this);
        recycler.setLayoutManager(layoutManager);

        adapter = new matchAdapter(getDataSetChat(), ChatActivity.this);
        recycler.setAdapter(adapter);

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
            newMessage.put("createdByUser", currentUser);
            newMessage.put("text", mText);

            newMessageDb.setValue(newMessage);
        }

        sendT.setText(null);
    }

    private void getChatId(){
        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    dbChat = dbChat.child(chatId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private ArrayList<matchesObject> resultChat = new ArrayList<matchesObject>();
    private List<matchesObject> getDataSetChat() {
        return resultChat;
    }
}
