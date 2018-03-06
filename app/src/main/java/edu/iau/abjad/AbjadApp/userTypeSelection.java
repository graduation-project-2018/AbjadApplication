package edu.iau.abjad.AbjadApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class userTypeSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_selection);
        final Intent intentOfChild=new Intent(this, Signin.class );
        final Intent intentOfEdu=new Intent(this, SigninEducator.class );

        ImageView ChildIcon = (ImageView) findViewById(R.id.child_btn);
        ImageView EduIcon = (ImageView) findViewById(R.id.educator_Btn);
        ChildIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentOfChild);
            }
        });

        EduIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentOfEdu);
            }
        });

    }
}