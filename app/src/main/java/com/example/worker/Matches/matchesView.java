package com.example.worker.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.worker.Chat.ChatActivity;
import com.example.worker.R;

public class matchesView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView matchText, matchPhone, matchDes, user;
    public ImageView mImage;

    public matchesView(View itemView){
        super(itemView);

        itemView.setOnClickListener(this);

        matchText = itemView.findViewById(R.id.matchId);
        matchPhone = itemView.findViewById(R.id.matchPhone);
        matchDes = itemView.findViewById(R.id.matchDes);
        user = itemView.findViewById(R.id.userID);
        mImage = itemView.findViewById(R.id.matchImage);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchText", user.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
