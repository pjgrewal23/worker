package com.example.worker.Chat;

import android.content.Context;
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

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches,  null, false);

        chatView mvh = new chatView(layoutView);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull chatView holder, int position) {
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
