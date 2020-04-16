package com.example.worker.Chat;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.worker.R;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatView> {

    private List<chatObject> chatList;
    private Context context;

    public chatAdapter(List<chatObject> matchList, Context context){
        this.chatList = matchList;
        this.context = context;
    }
    @Override
    public chatView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,  null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        chatView mvh = new chatView(layoutView);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull chatView holder, int position) {
        holder.mText.setText((chatList.get(position).getMessage()));
        if(chatList.get(position).getCurrentUserBool()){
            Log.i("her", "e");
            holder.mText.setGravity(Gravity.END);
            holder.mText.setTextColor(Color.WHITE);
            holder.containerL.setBackgroundColor(Color.BLUE);
        }
        else{
            Log.i("her", "errrrrrrrrr");
            holder.mText.setGravity(Gravity.START);
            holder.mText.setTextColor(Color.BLACK);
            holder.containerL.setBackgroundColor(Color.WHITE);

        }
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
