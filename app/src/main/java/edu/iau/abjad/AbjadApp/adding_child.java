package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adding_child  extends menu_educator {

    menu_variables m = new menu_variables();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase db;
    firebase_connection r;
    TextView firstName;
    TextView lastName;
    RadioButton male;
    RadioButton female;
    TextView password;
    TextView confirmedPassword;
    TextView username;
    Button addBtn;
    String user_id;
    String userName;
    childInformation child;
    /*
    TextView fnameError;
    TextView lnameError;
    TextView genderError;
    TextView passwordError;
    TextView mismatchedPasswordsError;
    TextView usernameError;



    */
   DatabaseReference rf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("إضافة طفل");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_adding_child, null, false);

        mDrawerLayout.addView(contentView, 0);

       db = FirebaseDatabase.getInstance();
       rf = db.getReference();
        r = new firebase_connection();
      // child = new childInformation();
        firstName = (TextView)findViewById(R.id.editText18);
        lastName = (TextView)findViewById(R.id.editText8);
        male = (RadioButton)findViewById(R.id.radioButton2);
        female = (RadioButton)findViewById(R.id.radioButton);
        password = (TextView)findViewById(R.id.editText6);
        confirmedPassword = (TextView)findViewById(R.id.editText26);
        username = (TextView)findViewById(R.id.editText226);
        addBtn = (Button)findViewById(R.id.imageButton3);
        /*fnameError = (TextView)findrViewById(R.id.fnameEr);
        lnameError = (TextView)findrViewById(R.id.lnameEr);;
       genderError = (TextView)findrViewById(R.id.genderEr);;
         passwordError = (TextView)findrViewById(R.id.passEr);;
        mismatchedPasswordsError = (TextView)findrViewById(R.id.missPassEr);
         usernameError = (TextView)findrViewById(R.id.usernEr);

           fnameError.setVisibility(TextView.INVISIBLE);
            lnameError.setVisibility(TextView.INVISIBLE);
            genderError.setVisibility(TextView.INVISIBLE);
              passwordError.setVisibility(TextView.INVISIBLE);
                 mismatchedPasswordsError.setVisibility(TextView.INVISIBLE);
                 usernameError.setVisibility(TextView.INVISIBLE);

*/
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
             // Boolean x = checkInputs();  //check fields
                addChild();
            }


        });

    }//end of onCreate function



    private void addChild(){
//I NEED TO ADD THE CHILD ID TO THE EDUCATOR NODE IN DB
  //   DatabaseReference read = r.ref.child("Lessons").child(lessonID).child("Words")ك
      userName = username.getText().toString().trim();
       // userName = userName.concat("abjad.sa");
        String pass = password.getText().toString().trim();

        auth.createUserWithEmailAndPassword(userName,pass).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(adding_child.this, "تمت إضافة طفل جديد بنجاح", Toast.LENGTH_LONG).show();
                            user_id = auth.getCurrentUser().getUid();
                            addChildInfo();
                          /*   String user_id = auth.getCurrentUser().getUid();
                              DatabaseReference current_user_db = re.ref.child("Children");

                            String fname = firstName.getText().toString().trim();
                            String lname = lastName.getText().toString().trim();
                            //gender missing and fname
                            childInformation child = new childInformation("مها",lname,"female","_",userName);
                           current_user_db.child(user_id).setValue(child); */

                          //  Toast.makeText(adding_child.this, "child added", Toast.LENGTH_LONG).show();
                          /*  Intent intent = new Intent(this,child_photo.class);
                            Bundle extras = new Bundle();
                            extras.putString("Educator_ID",educator_id);//should be passed from previous activity
                            extras.putString("Child_ID",user_id);
                            extras.putSerializable("object",child);
                            intent.putExtras(extras);
                            startActivity(intent);*/
                        }//Successful child registeration

                        else{
                            FirebaseAuthException e  = (FirebaseAuthException) task.getException();
                            //Toast.makeText(adding_child.this, "لم تتم إضافة الطفل، الرجاء المحاولة لاحقا", Toast.LENGTH_LONG).show();
                            Toast.makeText(adding_child.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }//end of addChild function

    public void addChildInfo(){
      //  String fname = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();

    child = new childInformation("مها",lname,"female","_",userName);
   r.ref.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
          r. ref.child("Children").child(user_id).setValue(child);

          Query q = r.ref.child("Educator_has_child").child("educator22").orderByKey().limitToLast(1);
          q.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   String key = dataSnapshot.getKey();
                   Toast.makeText(adding_child.this, key, Toast.LENGTH_LONG).show();
                    String[] number = key.split(" _ ");
                    int lastChildNum = Integer.parseInt( number[1]);
                    lastChildNum ++;
                    String newChildKey = "childID_"+lastChildNum;
                   r.ref.child("Educator_has_child").child("educator22").child(newChildKey).setValue(user_id);
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {
                   //Handle possible errors.
               }
           });
       }

       @Override
       public void onCancelled(DatabaseError databaseError) {

       }
   });



    }//end of addChildInfo
    private Boolean checkInputs(){
        Boolean errorFound = false;

        if(errorFound == false)
            return true;
        else
            return false;


    }//end of checkInputs function





}