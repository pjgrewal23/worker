package com.example.worker.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.worker.Cards.cards;
import com.example.worker.R;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {

    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        cards  card_item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);

        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.images);

        name.setText(card_item.getName());
        switch (card_item.getProfileImageURL()){
            case "default":
                Glide.with(getContext()).load(R.mipmap.ic_launcher_round).into(image);
                break;
            default:
                Glide.with(getContext()).load(card_item.getProfileImageURL()).into(image);
        }
        Glide.with(getContext()).load(card_item.getProfileImageURL()).into(image);

        return convertView;
    }
}
