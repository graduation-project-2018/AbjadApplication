package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView firstName;
    TextView lastName;
    TextView email;
    Button saveBtn;
    Intent intent;//ask about it
    TextView fnameError;
    TextView lnameError;
    TextView emailError;
    ImageView fnameErrorIcon;
    ImageView lnameErrorIcon;
    ImageView emailErrorIcon;
    int errorCounts;
    String oldFname;
    String oldLname;
    String oldEmail;
    Pattern ArabicLetters = Pattern.compile("^[أ-ي ]+$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الملف الشخصي");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_educator_profile, null, false);

        mDrawerLayout.addView(contentView, 0);


        db = FirebaseDatabase.getInstance();
        r = new firebase_connection();
        firstName = (TextView)findViewById(R.id.fnTxt);
        lastName = (TextView)findViewById(R.id.lnTxt);
        email = (TextView)findViewById(R.id.emailTxt);
        saveBtn = (Button)findViewById(R.id.saveChangesBtn);
        //intent ?????????
         fnameError = (TextView)findViewById(R.id.fnEr);
         lnameError = (TextView)findViewById(R.id.lnameEr) ;
         emailError = (TextView)findViewById(R.id.emailEr);
         fnameErrorIcon = (ImageView) findViewById(R.id.fnErIcon);
         lnameErrorIcon = (ImageView) findViewById(R.id.lnErIcon);
         emailErrorIcon = (ImageView) findViewById(R.id.emailErIcon);
         getCurrentEducatorInfo();
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                checkInputs();
                if (errorCounts == 0){

                    editEducator();
                }

            }


        });//end of saveBtn listener

    }//end of onCreate function

    private void checkInputs(){

        fnameError.setVisibility(View.INVISIBLE);
        lnameError.setVisibility(View.INVISIBLE);
        emailError.setVisibility(View.INVISIBLE);
        fnameErrorIcon.setVisibility(View.INVISIBLE);
        lnameErrorIcon.setVisibility(View.INVISIBLE);
        emailErrorIcon.setVisibility(View.INVISIBLE);
        errorCounts = 0;
        checkFirstName();
        checkLastName();
        checkEmail();

    }//end of checkInputs function

    public void checkFirstName(){


        if (firstName.getText().toString().isEmpty()) {
            fnameError.setText("قم بتعبئة الحقل بالإسم الأول للمربي");
            fnameError.setVisibility(View.VISIBLE);
            fnameErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;
        }
        else if (!ArabicLetters.matcher(firstName.getText().toString()).matches()) {
            fnameError.setText("قم بكتابة الإسم الأول باللغة العربية فقط");
            fnameError.setVisibility(View.VISIBLE);
            fnameErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;
        }

    }//end of checkFirstName function


    public void checkLastName(){


        if (lastName.getText().toString().isEmpty()) {
            lnameError.setText("قم بتعبئة الحقل بلقب المربي");
            lnameError.setVisibility(View.VISIBLE);
            lnameErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;
        }
        else if (!ArabicLetters.matcher(lastName.getText().toString()).matches()) {
            lnameError.setText("قم بكتابة اللقب باللغة العربية فقط ");
            lnameError.setVisibility(View.VISIBLE);
            lnameErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;
        }

    }//end of checkLastName function

    public void checkEmail(){
        if (email.getText().toString().isEmpty()) {
            emailErrorIcon.setVisibility(View.VISIBLE);
           emailError.setText("قم بتعبئة الحقل بالبريد الإلكتروني");
           emailError.setVisibility(View.VISIBLE);
            errorCounts++;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailErrorIcon.setVisibility(View.VISIBLE);
            emailError.setText("البريد الإلكتروني ليس على النمط someone@example.com ");
            emailError.setVisibility(View.VISIBLE);
            errorCounts++;

        }


    }//end of checkEmail function

    public void getCurrentEducatorInfo(){
        //educator ID need to be changed
       DatabaseReference read = r.ref.child("Educators").child("educator22");
        read.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot educatorInfo : dataSnapshot.getChildren()) {

                  oldFname = educatorInfo.child("first_name").getValue().toString();
                  oldLname = educatorInfo.child("last_name").getValue().toString();
                   oldEmail = educatorInfo.child("email").getValue().toString();

                } //end of for loop
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//end of read listener
        email.setText(oldEmail);
        firstName.setText(oldFname);
        lastName.setText(oldLname);

    }//end of getCurrentEducatorInfo function

    public void editEducator(){
        //make the object
        final String newEmail = email.getText().toString();
        final String newFname = firstName.getText().toString();
        final String newLname = lastName.getText().toString();

        r.ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //educator ID need to  be changed
                r.ref.child("Educators").child("educator22").child("email").setValue(newEmail);
                r.ref.child("Educators").child("educator22").child("first_name").setValue(newFname);
                r.ref.child("Educators").child("educator22").child("last_name").setValue(newLname);
                Toast.makeText(educator_profile.this, "تم حفظ التغييرات", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }//end of editEducator function
}//end of the class
