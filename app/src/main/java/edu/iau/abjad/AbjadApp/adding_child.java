package edu.iau.abjad.AbjadApp;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class adding_child  extends menu_educator {

    menu_variables m = new menu_variables();

    FirebaseDatabase db;
    firebase_connection r;
    EditText firstName, lastName, password,confirmedPassword, email;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button addBtn;
    childInformation child;
    Intent intent;
    boolean backFlag=false;
    TextView genderError;
    DatabaseReference rf;
    Boolean foundErrors;





   int errorCounts;
    Pattern ArabicLetters = Pattern.compile("^[ءئ ؤ إآ ى لآ لأ  لإ أ-ي ]+$");
    Query q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("إضافة طفل");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_adding_child, null, false);

        mDrawerLayout.addView(contentView, 0);

        db = FirebaseDatabase.getInstance();
        rf = db.getReference();
        r = new firebase_connection();

        firstName = findViewById(R.id.fnTxt);
        lastName = findViewById(R.id.lnTxt);
        radioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        password = findViewById(R.id.passwordTxt);
        confirmedPassword = findViewById(R.id.conPasswordTxt);
        email = findViewById(R.id.emailTxt);
        addBtn = (Button)findViewById(R.id.addBtn);
        intent = new Intent(this, child_photo.class);
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
                m.auth_setRight_icon_XLarge(email,password);
                confirmedPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_gray, 0);
                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setButton_text_Large(addBtn);
                m.setTitle_Large();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_2x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_2x, 0);
                m.auth_setRight_icon_Large(email,password);
                confirmedPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_2x, 0);
                Log.i("scsize","Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setButton_text_Normal(addBtn);
                m.setTitle_Normal();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_15x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_15x, 0);
                m.auth_setRight_icon_Normal(email,password);
                confirmedPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_15x, 0);
                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setButton_text_Small(addBtn);
                m.setTitle_Small();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                m.auth_setRight_icon_Small(email,password);
                confirmedPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_1x, 0);
                Log.i("scsize","Small" );
                break;
            default:
                m.setButton_text_Default(addBtn);
                m.setTitle_Default();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                m.auth_setRight_icon_Default(email,password);
                confirmedPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_1x, 0);

                Log.i("scsize","Default screen" );
        }//end switch

    }//end of onCreate function


    @Override
    public void onBackPressed() {
        if(backFlag == false){
            popUp();
            backFlag = true;
        }//first time to press back , we should give a warning


    }



    public void popUp(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(adding_child.this);
        mBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View mView = getLayoutInflater().inflate(R.layout.activity_adding_child_pop_up,null);
        Button close_btn = (Button) mView.findViewById(R.id.close_btn);
        Button ok_btn = (Button) mView.findViewById(R.id.ok_btn);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.show();
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


    }

    private void checkInputs(){


        genderError.setVisibility(View.INVISIBLE);
        foundErrors =false;
        checkFirstName();
        checkLastName();
        checkGender();
        checkEmail();
        checkPassword();
        checkExistingAccount();

    }//end of checkInputs function

    public void checkExistingAccount(){

        q = r.ref.child("Children").orderByChild("email").equalTo(email.getText().toString().trim());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    email.setError("البريد الإلكتروني تم استخدامه من قبل مستخدم آخر");
                    email.requestFocus();
                    foundErrors =true;
                }
                q = r.ref.child("Educators").orderByChild("email").equalTo(email.getText().toString().trim());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            email.setError("البريد الإلكتروني تم استخدامه من قبل مستخدم آخر");
                            email.requestFocus();
                            foundErrors =true;
                        }

                        else if (foundErrors == false){
                            String fname = firstName.getText().toString();
                            String lname = lastName.getText().toString();
                            String passChild = password.getText().toString().trim();
                            String emailChild = email.getText().toString().trim();
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            radioButton = (RadioButton) findViewById(selectedId);
                            String selectedGender = radioButton.getText().toString();
                            child = new childInformation(fname, lname, selectedGender, "_", emailChild);
                            Bundle extras = new Bundle();
                            extras.putSerializable("object", child);
                            extras.putString("password", passChild);
                            intent.putExtras(extras);
                            startActivity(intent);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Handle possible errors.
                    }
                });}


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
    }//end of checkExistingAccount
    public void checkFirstName(){


        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("قم بتعبئة الحقل بالاسم الأول للطفل");
            firstName.requestFocus();
            foundErrors =true;
        }
        else if (!ArabicLetters.matcher(firstName.getText().toString()).matches()) {
            firstName.setError("قم بكتابة الإسم الأول باللغة العربية فقط");
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
        else if (!ArabicLetters.matcher(lastName.getText().toString()).matches()) {
            lastName.setError("قم بكتابة اللقب باللغة العربية فقط ");
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
    public void checkEmail(){
        if (email.getText().toString().trim().isEmpty()) {
            email.setError("قم بتعبئة الحقل بالبريد الإلكتروني للطفل");
            email.requestFocus();
            foundErrors =true;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
            email.setError("االبريد الإلكتروني ليس على النمط someone@somewhere.com");
            email.requestFocus();
            foundErrors =true;

        }


    }//end of check email function

    public void checkPassword(){



        if (password.getText().toString().trim().isEmpty()) {
            password.setError("قم بتعبئة الحقل بكلمة المرور");
            password.requestFocus();
            foundErrors =true;

        }
        else if (password.length() < 6){

            password.setError("كلمة المرور يجب ان تكون اطول من 6 خانات ");
            password.requestFocus();
            foundErrors =true;
        }
        if (confirmedPassword.getText().toString().trim().isEmpty()) {
            confirmedPassword.setError("قم بتأكيد كلمة المرور");
            confirmedPassword.requestFocus();

            foundErrors =true;

        }

        else if (!confirmedPassword.getText().toString().trim().equals( password.getText().toString().trim())){
            confirmedPassword.setError("كلمة المرور المدخلة غير متطابقة");
            confirmedPassword.requestFocus();
            foundErrors =true;

        }

    }
}//end of adding_child class
