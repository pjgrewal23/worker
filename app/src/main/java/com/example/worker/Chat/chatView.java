package com.example.worker.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.worker.R;

public class chatView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView matchText, matchPhone, matchDes;
    public ImageView mImage;

    public chatView(View itemView){
        super(itemView);

        itemView.setOnClickListener(this);

        matchText = itemView.findViewById(R.id.matchId);
        matchPhone = itemView.findViewById(R.id.matchPhone);
        matchDes = itemView.findViewById(R.id.matchDes);

        mImage = itemView.findViewById(R.id.matchImage);
    }

    @Override
    public void onClick(View v) {
    }
}
