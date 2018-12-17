package edu.iau.abjad.AbjadApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class userTypeSelection extends AppCompatActivity {

    TextView eduLabel, childLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_user_type_selection);

        final Intent intentOfChild=new Intent(getApplicationContext(), Signin.class );
        final Intent intentOfEdu=new Intent(getApplicationContext(), SigninEducator.class );

        ImageView ChildIcon = findViewById(R.id.child_btn);
        ImageView EduIcon = findViewById(R.id.educator_Btn);

        eduLabel = findViewById(R.id.educatortext);
        childLabel = findViewById(R.id.childtext);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                eduLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
                childLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                eduLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                childLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);

                Log.i("scsize","Large" );

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                eduLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                childLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);

                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                eduLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                childLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

                Log.i("scsize","Small" );
                break;
            default:
               eduLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                childLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                Log.i("scsize","Default screen" );


        }//end switch


        ChildIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(intentOfChild);
            }
        });
        EduIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(intentOfEdu);
            }
        });

    }
}
