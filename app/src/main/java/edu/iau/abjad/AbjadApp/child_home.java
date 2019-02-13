package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


public class child_home extends child_menu {
     menu_variables m = new menu_variables();
     Intent intentOfUnit;
     ImageView family , homeLand, school;
     TextView family_textView,school_textView,homeLand_textView;
     String  first_signIn;
     String unit1_photo_url,unit2_photo_url, unit3_photo_url;
     ProgressBar family_progress_bar, school_progress_bar,watani_progress_bar;



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

        intentOfUnit=new Intent(getApplicationContext(), unit_interface.class );
        school=findViewById(R.id.schoolButton);
        homeLand=findViewById(R.id.homeButton);
        family=findViewById(R.id.familyButton);
        family_textView=findViewById(R.id.family);
        school_textView=findViewById(R.id.school);
        homeLand_textView=findViewById(R.id.city);
        family_progress_bar=findViewById(R.id.family_progress_bar);
        school_progress_bar=findViewById(R.id.school_progress_bar);
        watani_progress_bar=findViewById(R.id.watani_progress_bar);

        family_progress_bar.setVisibility(View.VISIBLE);
        school_progress_bar.setVisibility(View.VISIBLE);
        watani_progress_bar.setVisibility(View.VISIBLE);
        first_signIn ="";
        unit1_photo_url ="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/child_home_images%2Ffamily_unit_icon.png?alt=media&token=884e0c17-5982-4108-b6d1-5d8bdeb0df42";
        unit2_photo_url= "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/child_home_images%2Fschool_unit_icon.png?alt=media&token=7f0efe56-7362-41d5-aa0a-18e6d623d1d4";
        unit3_photo_url = "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/child_home_images%2Fwatani_unit_icon.png?alt=media&token=f9b6f11e-c8cf-4cd1-9903-6ee24ce4f722";

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),child_after_signin.class));
            }
        });

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

        // Display units images
        Picasso.get().load(unit1_photo_url).fit().into(family,new Callback() {
            @Override
            public void onSuccess(){
                family_progress_bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }

        });
        Picasso.get().load(unit2_photo_url).fit().into(school,new Callback() {
            @Override
            public void onSuccess(){
                school_progress_bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }

        });
        Picasso.get().load(unit3_photo_url).fit().into(homeLand,new Callback() {
            @Override
            public void onSuccess(){
                watani_progress_bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }

        });

        try{
    // get the value of first_signIn to know if we should play user manual or not.
        DatabaseReference getChildFirstSignInInfo = r.ref.child("Children").child(child_after_signin.id_child).child("first_signIn");
        getChildFirstSignInInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                first_signIn = dataSnapshot.getValue().toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
            }catch (Exception e){

            }


        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String unitId="unit2";
                intentOfUnit.putExtra("id",unitId);
                intentOfUnit.putExtra("Unitname","مدرستي");
                intentOfUnit.putExtra("preIntent","childHome");
                intentOfUnit.putExtra("first_signIn", first_signIn);
                setResult(RESULT_OK,intentOfUnit);
                finish();
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
                intentOfUnit.putExtra("first_signIn", first_signIn);
                setResult(RESULT_OK,intentOfUnit);
                finish();
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
                intentOfUnit.putExtra("first_signIn", first_signIn);
                setResult(RESULT_OK,intentOfUnit);
                finish();
                startActivity(intentOfUnit);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(child_home.this,child_after_signin.class));
    }

}


