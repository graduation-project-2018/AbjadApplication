package edu.iau.abjad.AbjadApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class userTypeSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_selection);
        Intent intentOfChild=new Intent(this, Signin.class );
        Intent intentOfEdu=new Intent(this, SigninEducator.class );

        ImageView ChildIcon = (ImageView) findViewById(R.id.child_btn);
        ImageView EduIcon = (ImageView) findViewById(R.id.educator_Btn);

        if(ChildIcon.isClickable()){
            startActivity(intentOfChild);
        }
        else if(EduIcon.isClickable()){
            startActivity(intentOfEdu);

        }


    }
}
