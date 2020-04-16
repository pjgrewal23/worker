package com.example.worker.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.worker.R;


public class chatView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mText;
    public LinearLayout containerL;

    public chatView(View itemView){
        super(itemView);

        itemView.setOnClickListener(this);

        mText = itemView.findViewById(R.id.msger);
        containerL = itemView.findViewById(R.id.containerTEXT);
    }

    @Override
    public void onClick(View v) {
    }
}
