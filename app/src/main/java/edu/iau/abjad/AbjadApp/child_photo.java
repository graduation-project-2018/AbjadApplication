package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class child_photo  extends menu_educator {
    menu_variables b = new menu_variables();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b.title = (TextView) findViewById(R.id.interface_title);
        b.title.setText("إضافة طفل");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_child_photo, null, false);

        mDrawerLayout.addView(contentView, 0);



    }
}

