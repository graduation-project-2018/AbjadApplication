package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class child_profile extends child_menu {


    menu_variables m = new menu_variables();
    EditText FNChild, LNChild;
    ImageView ChildImage;
    firebase_connection r;
    child_info_new child;
    DatabaseReference read;
    FirebaseAuth Uath;
    String edu_id;
    Button saveChanges;
    Pattern ArabicLetters;
    String oldFname;
    String oldLname;
    Boolean foundErrors;
    String photo_URL;
    FirebaseUser user;
    Boolean z;
    Pattern ps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الملف الشخصي للطفل");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_child_profile, null, false);

        myDrawerLayout.addView(contentView, 0);
        FNChild = (EditText) findViewById(R.id.fnameTxt);
        LNChild = (EditText) findViewById(R.id.lnameTxt);
        ChildImage = (ImageView) findViewById(R.id.childImage);
        ps = Pattern.compile("^[a-zA-Z ]+$");
        //UsernameMsg = (TextView) findViewById(R.id.UsernameErrorMsgCP);
        saveChanges = (Button) findViewById(R.id.saveChangesBtn);
        ArabicLetters = Pattern.compile("^[ءئ ؤ إآ ى لآ لأ  لإ أ-ي ]+$");
        r = new firebase_connection();
        getCurrentChildInfo();
        saveChanges.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
        checkInputs();
        if (foundErrors==false){
            editChild();

        }
    }
});//end of onClick function

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //get educator id;
        try{
            edu_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){

        }


        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setTitle_XLarge();
                m.setButton_text_XLarge(saveChanges);
                // change the right icon of Edit text based on screen size
                FNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray, 0);
                LNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray, 0);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setButton_text_Large(saveChanges);
                m.setTitle_Large();
                FNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_2x, 0);
                LNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_2x, 0);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setButton_text_Normal(saveChanges);
                m.setTitle_Normal();
                FNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_15x, 0);
                LNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_15x, 0);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setButton_text_Small(saveChanges);
                m.setTitle_Small();
                FNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                LNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                break;
            default:
                m.setButton_text_Default(saveChanges);
                m.setTitle_Default();
                FNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                LNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
        }//end switch

    }//end of onCreate
    private void checkInputs(){


        foundErrors= false;
        checkFirstName();
        checkLastName();

    }//end of checkInputs function

    public void getCurrentChildInfo(){

        try{
            Query query = r.ref.child("Children").child(child_after_signin.id_child);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        oldLname = dataSnapshot.child("last_name").getValue().toString();
                        oldFname  = dataSnapshot.child("first_name").getValue().toString();
                        photo_URL = dataSnapshot.child("photo_URL").getValue().toString();
                        FNChild .setText(oldFname);
                        LNChild.setText(oldLname);
                        Picasso.get().load(photo_URL).into(ChildImage);
                    }
                    else{
                        Toast.makeText(child_profile.this, "المستخدم غير موجود", Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(child_profile.this, "" + databaseError, Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){

        }



    }//end of getCurrentChildInfo function




    public void editChild(){

        final String newFname = FNChild.getText().toString();
        final String newLname = LNChild.getText().toString();

                r.ref.child("Children").child(child_after_signin.id_child).child("first_name").setValue(newFname);
                r.ref.child("Children").child(child_after_signin.id_child).child("last_name").setValue(newLname);
                r.ref.child("educator_home").child(edu_id).child("children").child(child_after_signin.id_child).child("first_name").setValue(newFname);
                Toast.makeText(child_profile.this, " تم حفظ التغييرات بنجاح", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),child_home.class);
                startActivity(intent);
                finish();

    }//end of editChild function

    public void checkFirstName(){


        if (FNChild.getText().toString().isEmpty()) {
            FNChild.setError("قم بتعبئة الحقل بالاسم الأول للطفل");
            foundErrors=true;
        }
        else if (!ArabicLetters.matcher(FNChild.getText().toString()).matches()&& !ps.matcher(FNChild.getText().toString()).matches()) {
            FNChild.setError("اسم الطفل يجب أن لا يحتوي على رموز أخرى غير الحروف");

            foundErrors=true;
        }

    }//end of checkFirstName function


    public void checkLastName(){


        if (LNChild.getText().toString().isEmpty()) {
            LNChild.setError("قم بتعبئة الحقل بلقب الطفل");

            foundErrors=true;
        }
        else if (!ArabicLetters.matcher(FNChild.getText().toString()).matches()&& !ps.matcher(FNChild.getText().toString()).matches()) {
            LNChild.setError("لقب الطفل يجب أن لا يحتوي على رموز أخرى غير الحروف ");

            foundErrors=true;
        }

    }//end of checkLastName function

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),child_home.class));
        finish();

    }


}//end of the class

