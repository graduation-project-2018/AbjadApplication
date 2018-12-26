package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class educator_profile extends menu_educator {

    menu_variables m = new menu_variables();
    FirebaseDatabase db;
    firebase_connection r;
    EditText firstName;
    EditText lastName;
    EditText email;
    Button saveBtn;
    Intent intent;
    Boolean foundErrors;
    String oldFname;
    String oldLname;
    String oldEmail;
    Pattern ArabicLetters = Pattern.compile("^[ءئ ؤ إآ ى لآ لأ  لإ أ-ي ]+$");
    String newEmail;
    Educator educator;
    FirebaseAuth Uath;
    FirebaseUser user;
    boolean z;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الملف الشخصي");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_educator_profile, null, false);

        mDrawerLayout.addView(contentView, 0);


        Uath= FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        r = new firebase_connection();
        firstName = findViewById(R.id.fnTxt);
        lastName = findViewById(R.id.lnTxt);
        email = findViewById(R.id.emailTxt);
        saveBtn = (Button)findViewById(R.id.saveChangesBtn);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setTitle_XLarge();
                // change the right icon of Edit text based on screen size
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_4x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_4x, 0);
                m.auth_setRight_icon_XLarge(email,null);
                m.setButton_text_XLarge(saveBtn);
                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setTitle_Large();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_2x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_2x, 0);
                m.auth_setRight_icon_Large(email,null);
                m.setButton_text_Large(saveBtn);
                Log.i("scsize","Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setTitle_Normal();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_15x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_15x, 0);
                m.auth_setRight_icon_Normal(email,null);
                m.setButton_text_Normal(saveBtn);
                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setTitle_Small();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_1x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_1x, 0);
                m.auth_setRight_icon_Small(email,null);
                m.setButton_text_Small(saveBtn);
                Log.i("scsize","Small" );
                break;
            default:
                m.setTitle_Default();
                firstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_1x, 0);
                lastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_1x, 0);
                m.auth_setRight_icon_Default(email,null);
                m.setButton_text_Default(saveBtn);
                Log.i("scsize","Default screen" );
        }//end switch

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(educator_profile.this,educator_home.class));
            }
        });

        getCurrentEducatorInfo();
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                checkInputs();
                if ( foundErrors ==false){
                    editEducator();

               }

            }


        });//end of saveBtn listener

    }//end of onCreate function

    private void checkInputs(){


        foundErrors =false;
        checkFirstName();
        checkLastName();
        checkEmail();

    }//end of checkInputs function

    public void checkFirstName(){


        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("قم بتعبئة الحقل بالإسم الأول للمربي");
            foundErrors =true;
        }
        else if (!ArabicLetters.matcher(firstName.getText().toString()).matches()) {
            firstName.setError("قم بكتابة الإسم الأول باللغة العربية فقط");

            foundErrors =true;
        }

    }//end of checkFirstName function


    public void checkLastName(){


        if (lastName.getText().toString().isEmpty()) {
            lastName.setError("قم بتعبئة الحقل بلقب المربي");

            foundErrors =true;
        }
        else if (!ArabicLetters.matcher(lastName.getText().toString()).matches()) {
            lastName.setError("قم بكتابة اللقب باللغة العربية فقط ");

            foundErrors =true;
        }

    }//end of checkLastName function

    public void checkEmail(){
        if (email.getText().toString().isEmpty()) {

           email.setError("قم بتعبئة الحقل بالبريد الإلكتروني");

            foundErrors =true;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {

            email.setError("البريد الإلكتروني ليس على النمط someone@example.com ");

            foundErrors =true;

        }


    }//end of checkEmail function

    public void getCurrentEducatorInfo(){
        //educator ID need to be changed
        Query query = r.ref.child("Educators").child(signin_new.id_edu);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                                    oldLname = dataSnapshot.child("last_name").getValue().toString();
                                    oldFname = dataSnapshot.child("first_name").getValue().toString();
                                    oldEmail = dataSnapshot.child("email").getValue().toString();
                                    email.setText(oldEmail);
                                    firstName.setText(oldFname);
                                    lastName.setText(oldLname);
                }
                else{
                    Toast.makeText(educator_profile.this, "NOT EXIST", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(educator_profile.this, "" + databaseError, Toast.LENGTH_LONG).show();
            }
        });


    }//end of getCurrentEducatorInfo function

    public void editEducator(){

        newEmail = email.getText().toString().trim();
        final String newFname = firstName.getText().toString();
        final String newLname = lastName.getText().toString();
        r.ref.child("Educators").child(signin_new.id_edu).child("first_name").setValue(newFname);
        r.ref.child("Educators").child(signin_new.id_edu).child("last_name").setValue(newLname);
                if (!newEmail.equals(oldEmail))
                {
                  boolean x =  updateEmail();

                  if(x==true){

                      r.ref.child("Educators").child(signin_new.id_edu).child("email").setValue(newEmail);

                  }//end of nested if
                }//end of outer if


        Toast.makeText(educator_profile.this, " تم حفظ التغييرات بنجاح", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(educator_profile.this, educator_home.class);
        startActivity(intent);


    }//end of editEducator function
    public boolean updateEmail(){

                    user = FirebaseAuth.getInstance().getCurrentUser();

                    user.updateEmail(newEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(educator_profile.this,"حدث خطأ خلال تغيير البريد الإلكتروني الرجاء المحاولة لاحقا", Toast.LENGTH_LONG).show();
                                       z = false;
                                    }
                                    else
                                        z =true;
                                }
                            });

       return z;



    }//end of updateEmail function

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(educator_profile.this,educator_home.class));
    }
}//end of the class
