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
    String id_edu;
    EditText email;
    Button saveBtn;
    Intent intent;
    Boolean foundErrors;
    String oldEmail;
    String newEmail;
    FirebaseAuth Uath;
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
        email = findViewById(R.id.emailTxt);
        saveBtn = (Button)findViewById(R.id.saveChangesBtn);

        try{
            id_edu= FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){

        }


        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setTitle_XLarge();
                // change the right icon of Edit text based on screen size
                m.auth_setRight_icon_XLarge(email,null);
                m.setButton_text_XLarge(saveBtn);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setTitle_Large();
                m.auth_setRight_icon_Large(email,null);
                m.setButton_text_Large(saveBtn);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setTitle_Normal();
                m.auth_setRight_icon_Normal(email,null);
                m.setButton_text_Normal(saveBtn);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setTitle_Small();
                m.auth_setRight_icon_Small(email,null);
                m.setButton_text_Small(saveBtn);
                break;
            default:
                m.setTitle_Default();
                m.auth_setRight_icon_Default(email,null);
                m.setButton_text_Default(saveBtn);
        }//end switch

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),educator_home.class));
                finish();
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
        checkEmail();

    }//end of checkInputs function


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
        Query query = r.ref.child("Educators").child(id_edu);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                                    oldEmail = dataSnapshot.child("email").getValue().toString();
                                    email.setText(oldEmail);

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
                if (!newEmail.equals(oldEmail))
                {
                 updateEmail();
                }

    }//end of editEducator function
    public void updateEmail(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        newEmail = email.getText().toString().trim();
        System.out.println("new Email: "+ newEmail);
                    user.updateEmail(newEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        r.ref.child("Educators").child(id_edu).child("email").setValue(newEmail);
                                        Toast.makeText(educator_profile.this, " تم حفظ التغييرات بنجاح", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), educator_home.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(educator_profile.this,"حدث خطأ خلال تغيير البريد الإلكتروني الرجاء المحاولة لاحقا", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });





    }//end of updateEmail function

    @Override
    public void onBackPressed() {
        startActivity(new Intent(educator_profile.this,educator_home.class));
        finish();
    }
}//end of the class
