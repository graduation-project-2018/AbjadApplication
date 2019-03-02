package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class select_user_type extends AppCompatActivity {
    TextView eduLabel, childLabel;
    Random rand = new Random();
    String[]  edu_auth_arabic_numbers = {"١٢٠٠","٣٠٠٠","١٢٠","٥٥","٢١٠","٤٤","٩٩","٨٧","٥١٠٠","١١٠٠"};
    String[] edu_auth_numbers = {"1200", "3000", "120", "55", "210","44","99","87","5100","1100"};
    String[] edu_auth_written_numbers=
            {"ألف ومئتان",
                    "ثلاثة آلاف",
                    "مئة وعشرون",
                    "خمسة وخمسون",
                    "مئتان وعشرة",
                    "أربعة وأربعون",
                    "تسعة وتسعون",
                    "سبعة وثمانون",
                    "خمسة آلاف ومئة",
                    "ألف ومئة"};

    int chosen_element;
    Intent intentOfEdu;
   int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_select_user_type);
        final Intent intentOfChild=new Intent(getApplicationContext(), child_after_signin.class );
        intentOfEdu=new Intent(getApplicationContext(), educator_home.class );
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
                width = 820;
                height = 520;
               // Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                eduLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                childLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
              //  Log.i("scsize","Large" );
                width = 820;
                height = 520;

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                eduLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                childLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                width = 1200;
                height = 700;
            //    Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                eduLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                childLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                width = 1200;
                height = 700;
            //    Log.i("scsize","Small" );
                break;
            default:
                eduLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                childLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                width = 1000;
                height = 600;
            //    Log.i("scsize","Default screen" );
        }//end switch


        ChildIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentOfChild);
                finish();
            }
        });
        EduIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp_edu();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void popUp_edu(){
        chosen_element = rand.nextInt(10); // array length is 10
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(select_user_type.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_edu_auth_popup,null);
        Button close_btn = mView.findViewById(R.id.cancel_btn);
        TextView num_label = mView.findViewById(R.id.num_label);
        final EditText num_field = mView.findViewById(R.id.enter_num);
        Button enter_btn = mView.findViewById(R.id.submit_btn);
        // Display the choosen element in the Popup
        num_label.setText(edu_auth_written_numbers[chosen_element]);
        final ArrayList<String> arrayList_num = new ArrayList<String>(Arrays.asList(edu_auth_numbers));
        final ArrayList<String> arrayList_num_arabic = new ArrayList<String>(Arrays.asList(edu_auth_arabic_numbers));

        //check if the user enter the right number
        enter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = num_field.getText().toString().trim();
                //get the index of chosen number that displayed to the user
                int returned_index = arrayList_num.indexOf(num);
                if(returned_index == chosen_element){
                    startActivity(intentOfEdu);
                    finish();
                }
                // user enter the number wrong or in Arabic
                else {
                    int returned_index2 = arrayList_num_arabic.indexOf(num);
                    if (returned_index2 == chosen_element) {
                        startActivity(intentOfEdu);
                        finish();
                    } else {
                        num_field.setError("الرقم غير صحيح");
                        num_field.requestFocus();
                    }
                }

            }
        });

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        Window window =dialog.getWindow();
        window.setLayout(width,height);
        close_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }// end Popup edu



}
