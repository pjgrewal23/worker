package com.example.worker.Matches;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.worker.R;

public class matchesView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView matchText, matchPhone, matchDes;
    public ImageView mImage;

    public matchesView(View itemView){
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
