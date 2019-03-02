package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class new_add_child extends menu_educator {

    menu_variables m = new menu_variables();
    FirebaseDatabase db;
    firebase_connection r;
    EditText firstName, lastName;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button addBtn;
    child_info_new child;
    Intent intent;
    boolean backFlag=false;
    TextView genderError;
    DatabaseReference rf;
    Boolean foundErrors;
    Intent i;

    int errorCounts;
    Pattern ArabicLetters = Pattern.compile("^[ءئ ؤ إآ ى لآ لأ  لإ أ-ي ]+$");
    Query q;
    Pattern ps;
    int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("إضافة طفل");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_new_add_child, null, false);

        mDrawerLayout.addView(contentView, 0);

        db = FirebaseDatabase.getInstance();
        rf = db.getReference();
        r = new firebase_connection();
        firstName = findViewById(R.id.fnTxt);
        lastName = findViewById(R.id.lnTxt);
        ps = Pattern.compile("^[a-zA-Z ]+$");
        radioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        addBtn = (Button)findViewById(R.id.addBtn);
        intent = new Intent(getApplicationContext(), new_child_photo.class);
        genderError = (TextView)findViewById(R.id.genderEr);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                checkInputs();
            } });

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setTitle_XLarge();
                m.setButton_text_XLarge(addBtn);
                // change the right icon of Edit text based on screen size
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray, 0);
                width = 820;
                height = 520;
             //   Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setButton_text_Large(addBtn);
                m.setTitle_Large();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_2x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_2x, 0);
                width = 820;
                height = 520;
            //    Log.i("scsize","Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setButton_text_Normal(addBtn);
                m.setTitle_Normal();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_15x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_15x, 0);
                width = 1300;
                height = 700;
              //  Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setButton_text_Small(addBtn);
                m.setTitle_Small();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                width = 1300;
                height = 700;
             //   Log.i("scsize","Small" );
                break;
            default:
                m.setButton_text_Default(addBtn);
                m.setTitle_Default();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                width = 1000;
                height = 600;
             //   Log.i("scsize","Default screen" );
        }//end switch

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }// end OnCreate()

    @Override
    public void onBackPressed() {
        if(backFlag == false){
            popUp();
            backFlag = true;
        }//first time to press back , we should give a warning
    }

    private void checkInputs(){
        genderError.setVisibility(View.INVISIBLE);
        foundErrors =false;
        checkFirstName();
        checkLastName();
        checkGender();
        moveToChildPhoto();

    }//end of checkInputs function

    public void moveToChildPhoto(){
        if(!foundErrors){
            String fname = firstName.getText().toString();
            String lname = lastName.getText().toString();
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            String selectedGender = radioButton.getText().toString();
            child = new child_info_new(fname, lname, selectedGender, "_", "000");
            Bundle extras = new Bundle();
            extras.putSerializable("object", child);
            intent.putExtras(extras);
            startActivity(intent);
            finish();
        }
    }

    public void popUp(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(new_add_child.this);
        mBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View mView = getLayoutInflater().inflate(R.layout.activity_adding_child_pop_up,null);
        Button ok_btn =  mView.findViewById(R.id.ok_btn);
        i = new Intent(getApplicationContext(), educator_home.class);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.show();
        Window window =dialog.getWindow();
        window.setLayout(width,height);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(i);
                finish();
            }
        });





    }


    public void checkFirstName(){
        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("قم بتعبئة الحقل بالاسم الأول للطفل");
            firstName.requestFocus();
            foundErrors =true;
        }
        else if (!ArabicLetters.matcher(firstName.getText().toString()).matches()&& !ps.matcher(firstName.getText().toString()).matches()) {
            firstName.setError("اسم الطفل يجب أن لا يحتوي على رموز أخرى غير الحروف");
            firstName.requestFocus();
            foundErrors =true;
        }

    }//end of checkFirstName function

    public void checkLastName(){
        if (lastName.getText().toString().isEmpty()) {
            lastName.setError("قم بتعبئة الحقل بلقب الطفل");
            lastName.requestFocus();
            foundErrors =true;
        }
        else if (!ArabicLetters.matcher(firstName.getText().toString()).matches()&& !ps.matcher(firstName.getText().toString()).matches()) {
            lastName.setError("لقب الطفل يجب أن لا يحتوي على رموز أخرى غير الحروف ");
            lastName.requestFocus();
            foundErrors =true;
        }

    }//end of checkLastName function


    public void checkGender(){
        if(radioGroup.getCheckedRadioButtonId() == -1)
        {
            genderError.setVisibility(View.VISIBLE);
            foundErrors =true;
        }
    }//end of checkGender function

    @Override
    public void onPause() {
        super.onPause();
        finish();

    }

}
