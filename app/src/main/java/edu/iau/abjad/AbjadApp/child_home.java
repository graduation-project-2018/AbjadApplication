package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
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
    TextView family_textView,school_textView,homeLand_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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
        family_textView=findViewById(R.id.family);
        school_textView=findViewById(R.id.school);
        homeLand_textView=findViewById(R.id.city);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                family_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                school_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                homeLand_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                m.setTitle_XLarge();
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                family_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                school_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                homeLand_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                m.setTitle_Large();
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                family_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                school_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                homeLand_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                m.setTitle_Normal();
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                family_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                school_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                homeLand_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                m.setTitle_Small();
                break;
            default:
                family_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                school_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                homeLand_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                m.setTitle_Default();
        }

        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String unitId="unit2";
                intentOfUnit.putExtra("id",unitId);
                intentOfUnit.putExtra("Unitname","مدرستي");
                intentOfUnit.putExtra("preIntent","childHome");
                setResult(RESULT_OK,intentOfUnit);
                startActivity(intentOfUnit);
            }
        });
        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String unitId = "unit1";
                intentOfUnit.putExtra("id",unitId);
                intentOfUnit.putExtra("Unitname","أسرتي");
                intentOfUnit.putExtra("preIntent","childHome");
                setResult(RESULT_OK,intentOfUnit);
                startActivity(intentOfUnit);
            }
        });
        homeLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String unitId = "unit3";
                intentOfUnit.putExtra("id",unitId);
                intentOfUnit.putExtra("Unitname","مدينتي");
                intentOfUnit.putExtra("preIntent","childHome");
                setResult(RESULT_OK,intentOfUnit);
                startActivity(intentOfUnit);
            }
        });
    }

}


