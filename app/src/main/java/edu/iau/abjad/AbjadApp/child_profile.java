package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class child_profile extends menu_educator {


    menu_variables m = new menu_variables();
    EditText FNChild, LNChild;
    ImageView ChildImage;

    firebase_connection r;
    childInformation child;
    DatabaseReference read;
    Button saveChanges;
    Pattern ArabicLetters;
    String oldFname;
    String oldLname;
   Boolean foundErrors;
    String photo_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الملف الشخصي للطفل");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_child_profile, null, false);

        mDrawerLayout.addView(contentView, 0);

        FNChild = (EditText) findViewById(R.id.fnameTxt);
        LNChild = (EditText) findViewById(R.id.lnameTxt);
        ChildImage = (ImageView) findViewById(R.id.childImage);

        //UsernameMsg = (TextView) findViewById(R.id.UsernameErrorMsgCP);
        saveChanges = (Button) findViewById(R.id.saveChangesBtn);
        ArabicLetters = Pattern.compile("^[أ-ي ]+$");
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


    }//end of onCreate
    private void checkInputs(){


        foundErrors= false;
        checkFirstName();
        checkLastName();


    }//end of checkInputs function

    public void getCurrentChildInfo(){
///change ID
        Query query = r.ref.child("Children").child("i6ywh35HrgdyjDe9lh98BGcutpY2");
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


    }//end of getCurrentChildInfo function
    public void editChild(){

        final String newFname = FNChild.getText().toString();
        final String newLname = LNChild.getText().toString();

        r.ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                r.ref.child("Children").child(Signin.id_child).child("first_name").setValue(newFname);
                r.ref.child("Children").child(Signin.id_child).child("last_name").setValue(newLname);

                Toast.makeText(child_profile.this, " تم حفظ التغييرات بنجاح", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(child_profile.this, ChildProgress.class);
                startActivity(intent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }//end of editChild function

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

