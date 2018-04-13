package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class child_home extends child_menu {
    menu_variables m = new menu_variables();
     Intent intentOfUnit;
     Button family , homeLand, school;
     child_profile obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الرئيسية");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_child_home, null, false);

        myDrawerLayout.addView(contentView, 0);

        intentOfUnit=new Intent(this, unit_interface.class );
        school=findViewById(R.id.schoolButton);
        homeLand=findViewById(R.id.homeButton);
        family=findViewById(R.id.familyButton);

    }
    public void unitChosed(View view){
        String unitId = "unit2";
        if(school.callOnClick()){
            unitId="unit2";
        }else if(homeLand.callOnClick()){
            unitId="unit3";
        }else if(family.callOnClick()){
            unitId="unit1";
        }
        intentOfUnit.putExtra("id",unitId);
        startActivity(intentOfUnit);
    }
}


