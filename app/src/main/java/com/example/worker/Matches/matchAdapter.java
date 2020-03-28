package com.example.worker.Matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.worker.R;

import java.util.List;

public class matchAdapter extends RecyclerView.Adapter<matchesView> {

    private List<matchesObject> matchList;
    private Context context;

    public matchAdapter(List<matchesObject> matchList, Context context){
        this.matchList = matchList;
        this.context = context;
    }
    @Override
    public matchesView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches,  null, false);

        matchesView mvh = new matchesView(layoutView);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull matchesView holder, int position) {
        holder.matchText.setText(matchList.get(position).getUid());
        holder.matchPhone.setText(matchList.get(position).getPhone());
        holder.matchDes.setText(matchList.get(position).getDescription());
        if(!matchList.get(position).getProfilepicURL().equals("default")){
            Glide.with(context).load(matchList.get(position).getProfilepicURL()).into(holder.mImage);
        }

    }

    @Override
    public int getItemCount() {
        return this.matchList.size();
    }
}
