package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class adding_child  extends menu_educator {
    menu_variables m = new menu_variables();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("إضافة طفل");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_adding_child, null, false);

        mDrawerLayout.addView(contentView, 0);
        Button addBtn = (Button)findViewById(R.id.imageButton3);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //check fields
          addChild();
            }


        });

    }//end ofk oknCreate function

    public void addChild(){
        

    }//end of addChild function






}