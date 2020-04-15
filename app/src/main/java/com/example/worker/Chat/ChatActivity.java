package com.example.worker.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.worker.Matches.Matches;
import com.example.worker.Matches.matchAdapter;
import com.example.worker.Matches.matchesObject;
import com.example.worker.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recycler = findViewById(R.id.recycler);
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(ChatActivity.this);
        recycler.setLayoutManager(layoutManager);

        adapter = new matchAdapter(getDataSetMatches(), ChatActivity.this);
        recycler.setAdapter(adapter);
    }


    private ArrayList<matchesObject> resultMatches = new ArrayList<matchesObject>();
    private List<matchesObject> getDataSetMatches() {
        return resultMatches;
    }
}
