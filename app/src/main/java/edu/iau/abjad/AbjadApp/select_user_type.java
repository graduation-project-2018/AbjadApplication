package edu.iau.abjad.AbjadApp;

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

import com.google.firebase.auth.FirebaseAuth;

public class select_user_type extends AppCompatActivity {
    TextView eduLabel, childLabel;
    ImageView back_btn;
    String[] edu_auth_numbers = {"1200", "973", "1855", "1234", "4321","123","987","458","1111","2222"};
    String[] edu_auth_written_numbers= {"ألف ومئتان","تسعمائة وثلاثة وسبعون","ألف وثمانمئة وخمسة وخمسون ",
            "ألف ومئتان وثلاثة وأربعون","أربعة آلاف وثلاثمائة وواحد وعشرون","مئة وثلاثة وعشرون","تسعمائة وثمانية وسبعون",
            "أربعمائة وخمسة وثمانون ","ألف ومئة واحدى عشر","ألفان ومئتان واثنان وعشرون"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_select_user_type);
        final Intent intentOfChild=new Intent(getApplicationContext(), child_after_signin.class );
        final Intent intentOfEdu=new Intent(getApplicationContext(), educator_home.class );
        ImageView ChildIcon = findViewById(R.id.child_btn);
        ImageView EduIcon = findViewById(R.id.educator_Btn);
        eduLabel = findViewById(R.id.educatortext);
        childLabel = findViewById(R.id.childtext);
        back_btn = findViewById(R.id.back_select_user_type);



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(select_user_type.this,signin_new.class));
            }
        });

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

    @Override
    public void onBackPressed() {
        finish();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(select_user_type.this,signin_new.class));
    }
}
