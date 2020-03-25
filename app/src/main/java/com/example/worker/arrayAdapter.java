package com.example.worker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
        ImageSwitcher images = convertView.findViewById(R.id.images);

        name.setText(card_item.getName());
        images.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view= new ImageView(getContext());
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return view;
            }
        });
        images.setImageResource(R.mipmap.ic_launcher);

        return convertView;
    }
}
