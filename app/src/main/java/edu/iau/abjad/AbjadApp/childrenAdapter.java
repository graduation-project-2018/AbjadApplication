package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class childrenAdapter extends BaseAdapter {

Context c;
ArrayList <children> children;

    public childrenAdapter(Context c, ArrayList<edu.iau.abjad.AbjadApp.children> children) {
        this.c = c;
        this.children = children;
    }

    @Override
    public int getCount() {
        return children.size();
    }

    @Override
    public Object getItem(int position) {
        return children.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.activity_child_crad_model,parent,false);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.child_img);
        TextView name = (TextView) convertView.findViewById(R.id.child_name);


        final children s= (children) this.getItem(position);

        Picasso.get().load(s.photo_URL).into(img);
        name.setText(s.getFirst_name());

        //ONITECLICK
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //GET CHILD ID AND MOVE TO CHILD PROGRESS
            }
        });

        return convertView;
    }
}//end of childrenAdapter  function
