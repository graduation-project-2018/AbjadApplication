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
import android.util.Log;
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
    EditText FNChild, LNChild,Email;
    ImageView ChildImage;

    firebase_connection r;
    childInformation child;
    DatabaseReference read;
    Button saveChanges;
    Pattern ArabicLetters;
    String oldFname;
    String oldLname;
   Boolean foundErrors;
    String photo_URL, oldEmail;
    FirebaseUser user;
    Boolean z;

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
        Email = (EditText) findViewById(R.id.emailTxt);
        FNChild = (EditText) findViewById(R.id.fnameTxt);
        LNChild = (EditText) findViewById(R.id.lnameTxt);
        ChildImage = (ImageView) findViewById(R.id.childImage);

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
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setTitle_XLarge();
                m.setButton_text_XLarge(saveChanges);
                // change the right icon of Edit text based on screen size
                FNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray, 0);
                LNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray, 0);
                Email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon, 0);
                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setButton_text_Large(saveChanges);
                m.setTitle_Large();
                FNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_2x, 0);
                LNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_2x, 0);
                Email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_2x, 0);
                Log.i("scsize","Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setButton_text_Normal(saveChanges);
                m.setTitle_Normal();
                FNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_15x, 0);
                LNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_15x, 0);
                Email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_15x, 0);

                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setButton_text_Small(saveChanges);
                m.setTitle_Small();
                FNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                LNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                Email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_1x, 0);
                Log.i("scsize","Small" );
                break;
            default:
                m.setButton_text_Default(saveChanges);
                m.setTitle_Default();
                FNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                LNChild.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.child_gray_1x, 0);
                Email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_1x, 0);

                Log.i("scsize","Default screen" );
        }//end switch

    }//end of onCreate
    private void checkInputs(){


        foundErrors= false;
        checkFirstName();
        checkLastName();
        checkEmail();

    }//end of checkInputs function

    public void getCurrentChildInfo(){

        Query query = r.ref.child("Children").child(Signin.id_child);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    oldLname = dataSnapshot.child("last_name").getValue().toString();
                    oldFname  = dataSnapshot.child("first_name").getValue().toString();
                    photo_URL = dataSnapshot.child("photo_URL").getValue().toString();
                    oldEmail = dataSnapshot.child("email").getValue().toString();

                    FNChild .setText(oldFname);
                    LNChild.setText(oldLname);
                    Picasso.get().load(photo_URL).into(ChildImage);
                    Email.setText(oldEmail);
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


    }//end of getCurrentChildInfo function
    public void checkEmail(){
        if (Email.getText().toString().isEmpty()) {

            Email.setError("قم بتعبئة الحقل بالبريد الإلكتروني");

            foundErrors =true;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString().trim()).matches()) {

            Email.setError("البريد الإلكتروني ليس على النمط someone@example.com ");

            foundErrors =true;

        }


    }//end of checkEmail function
    public void editChild(){

        final String newFname = FNChild.getText().toString();
        final String newLname = LNChild.getText().toString();
        final String newEmail = Email.getText().toString().trim();

                r.ref.child("Children").child(Signin.id_child).child("first_name").setValue(newFname);
                r.ref.child("Children").child(Signin.id_child).child("last_name").setValue(newLname);
                if (!newEmail.equals(oldEmail)){
                    boolean x =  updateEmail(newEmail);
                    if(x==true){

                        r.ref.child("Children").child(Signin.id_child).child("email").setValue(newEmail);

                    }//end of nested if
                }
                Toast.makeText(child_profile.this, " تم حفظ التغييرات بنجاح", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(child_profile.this,child_home.class);
                startActivity(intent);

    }//end of editChild function


    public boolean updateEmail(String newEmail){

        user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(child_profile.this,"حدث خطأ خلال تغيير البريد الإلكتروني الرجاء المحاولة لاحقا", Toast.LENGTH_LONG).show();
                            z = false;
                        }
                        else
                            z =true;
                    }
                });

        return z;



    }//end of updateEmail function


    public void checkFirstName(){


        if (FNChild.getText().toString().isEmpty()) {
            FNChild.setError("قم بتعبئة الحقل بالاسم الأول للطفل");
            foundErrors=true;
        }
        else if (!ArabicLetters.matcher(FNChild.getText().toString()).matches()) {
            FNChild.setError("قم بكتابة الإسم الأول باللغة العربية فقط");

            foundErrors=true;
        }

    }//end of checkFirstName function


    public void checkLastName(){


        if (LNChild.getText().toString().isEmpty()) {
            LNChild.setError("قم بتعبئة الحقل بلقب الطفل");

            foundErrors=true;
        }
        else if (!ArabicLetters.matcher(LNChild.getText().toString()).matches()) {
            LNChild.setError("قم بكتابة اللقب باللغة العربية فقط ");

            foundErrors=true;
        }

    }//end of checkLastName function


}//end of the class

