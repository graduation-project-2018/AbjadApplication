package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.regex.Pattern;

public class adding_child  extends menu_educator {

    menu_variables m = new menu_variables();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase db;
    firebase_connection r;
    TextView firstName;
    TextView lastName;
    RadioGroup radioGroup;
    RadioButton radioButton;

    TextView password;
    TextView confirmedPassword;
    TextView username;
    Button addBtn;
    String user_id;
    String userName;
    childInformation child;
    Intent intent;

    TextView fnameError;
    TextView lnameError;
    TextView genderError;
    TextView passwordError;
    TextView mismatchedPasswordsError;
    TextView emailError;
    ImageView fnameErrorIcon;
    ImageView lnameErrorIcon;
    ImageView genderErrorIcon;
    ImageView passwordErrorIcon;
    ImageView mismatchedPasswordsErrorIcon;
    ImageView emailErrorIcon;
    String LastChildKey;
    String fullUsername;
    String pass;
   DatabaseReference rf;

   int errorCounts;
    Pattern ArabicLetters = Pattern.compile("^[أ-ي ]+$");
    Pattern EnglishLetters = Pattern.compile("^[a-zA-Z ]+$");
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

        firstName = (TextView)findViewById(R.id.fnTxt);
        lastName = (TextView)findViewById(R.id.lnTxt);

        radioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        password = (TextView)findViewById(R.id.passwordTxt);
        confirmedPassword = (TextView)findViewById(R.id.conPasswordTxt);
        username = (TextView)findViewById(R.id.emailTxt);
        addBtn = (Button)findViewById(R.id.addBtn);
        intent = new Intent(this, child_photo.class);
        fnameError = (TextView)findViewById(R.id.fnameEr);
        lnameError = (TextView)findViewById(R.id.lnameEr);
       genderError = (TextView)findViewById(R.id.genderEr);
         passwordError = (TextView)findViewById(R.id.passEr);
        mismatchedPasswordsError = (TextView)findViewById(R.id.missPassEr);
         emailError = (TextView)findViewById(R.id.unameEr);
        fnameErrorIcon= (ImageView)findViewById(R.id.fnErIcon);
        lnameErrorIcon = (ImageView)findViewById(R.id.lnErIcon);
        genderErrorIcon = (ImageView)findViewById(R.id.genderErIcon);
        passwordErrorIcon = (ImageView)findViewById(R.id.passwordErIcon);
        mismatchedPasswordsErrorIcon = (ImageView)findViewById(R.id.conPasswordErIcon);
        emailErrorIcon = (ImageView)findViewById(R.id.emailErIcon);


        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

             checkInputs();
              if (errorCounts == 0){
                  checkExistingAccount();
              }

            }


        });

    }//end of onCreate function




    private void checkInputs(){

        fnameError.setVisibility(View.INVISIBLE);
        lnameError.setVisibility(View.INVISIBLE);
        genderError.setVisibility(View.INVISIBLE);
        passwordError.setVisibility(View.INVISIBLE);
        mismatchedPasswordsError.setVisibility(View.INVISIBLE);
       emailError.setVisibility(View.INVISIBLE);
        fnameErrorIcon.setVisibility(View.INVISIBLE);
        lnameErrorIcon.setVisibility(View.INVISIBLE);
        genderErrorIcon.setVisibility(View.INVISIBLE);
        passwordErrorIcon.setVisibility(View.INVISIBLE);
        mismatchedPasswordsErrorIcon.setVisibility(View.INVISIBLE);
        emailErrorIcon.setVisibility(View.INVISIBLE);
      errorCounts = 0;
      checkFirstName();
        checkLastName();
        checkGender();
        checkEmail();
        checkPassword();
       // checkExistingAccount();

    }//end of checkInputs function

    public void checkExistingAccount(){

        Query q = r.ref.child("Children").orderByChild("username").equalTo(userName);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    emailError.setText("اسم المستخدم موجود سابقا");
                    emailError.setVisibility(View.VISIBLE);
                    emailErrorIcon.setVisibility(View.VISIBLE);
                    errorCounts++;
                }
                else {
                    Toast.makeText(adding_child.this, "NOT EXIST", Toast.LENGTH_LONG).show();
                    if(errorCounts == 0){
                        String fname = firstName.getText().toString();
                        String lname = lastName.getText().toString();

                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(selectedId);
                        String selectedGender = radioButton.getText().toString();
                        child = new childInformation(fname,lname,selectedGender,"_",userName);
                     /*   Bundle extras = new Bundle();
                        extras.putSerializable("object",child);
                        extras.putString("password",pass);
                        intent.putExtras(extras);
                        startActivity(intent);*/

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });




    }//end of checkExistingAccount function

    public void checkFirstName(){


        if (firstName.getText().toString().isEmpty()) {
            fnameError.setText("قم بتعبئة الحقل بالاسم الأول للطفل");
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
            lnameError.setText("قم بتعبئة الحقل بلقب الطفل");
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
    public void checkGender(){

        if(radioGroup.getCheckedRadioButtonId() == -1)
        {
            genderError.setText("قم باختيار جنس الطفل");
            genderError.setVisibility(View.VISIBLE);
            genderErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;
        }


    }//end of checkGender function

    public void checkEmail(){
        if (username.getText().toString().isEmpty()) {
            emailError.setText("قم بتعبئة الحقل باسم المستخدم للطفل");
            emailError.setVisibility(View.VISIBLE);
            emailErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;
        }
        else if (!EnglishLetters.matcher(username.getText().toString()).matches()){
            emailError.setText("اسم المستخدم يجب أن يحتوي على أحرف انجليزية فقط");
            emailError.setVisibility(View.VISIBLE);
            emailErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;

        }


    }//end of checkUsername function

    public void checkPassword(){

Boolean emptyPass =false;
Boolean emptyConPass = false;

        if (password.getText().toString().isEmpty()) {
            passwordError.setText("قم بتعبئة الحقل بكلمة المرور");
            passwordError.setVisibility(View.VISIBLE);
            passwordErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;
            emptyPass =true;
        }
        else if (password.length() < 6){

            passwordError.setText("كلمة المرور يجب ان تكون اطول من 6 خانات ");
            passwordError.setVisibility(View.VISIBLE);
            passwordErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;
        }
        if (confirmedPassword.getText().toString().isEmpty()) {
            mismatchedPasswordsError.setText("قم بتأكيد كلمة المرور");
            mismatchedPasswordsError.setVisibility(View.VISIBLE);
            mismatchedPasswordsErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;
            emptyConPass = true;
        }

        if ( emptyConPass == false && emptyPass ==false){
        if (!confirmedPassword.getText().toString().equals( password.getText().toString())){
            mismatchedPasswordsError.setText("كلمات المرور المدخلة غير متطابقة");
            mismatchedPasswordsError.setVisibility(View.VISIBLE);
            mismatchedPasswordsErrorIcon.setVisibility(View.VISIBLE);
            passwordErrorIcon.setVisibility(View.VISIBLE);
            errorCounts++;
//////

        } }

    }



}
/* private void addChild(){


         auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(adding_child.this, "تمت إضافة طفل جديد بنجاح", Toast.LENGTH_LONG).show();
                            user_id = auth.getCurrentUser().getUid();
                            addChildInfo();


                        }//Successful child registeration

                        else{


                        }
                    }
                });


    }//end of addChild function


    public void addChildInfo(){
  //Make that you set the photo_URL in the child object

       r.ref.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
           r.ref.child("Children").child(user_id).setValue(child);
           //educator ID need to  be changed
           r.ref.addValueEventListener(new ValueEventListener(){
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   r.ref.child("Educator_has_child").child(SigninEducator.id_edu).child(user_id).setValue(true);

                 }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }


           });





       }
       @Override
       public void onCancelled(DatabaseError databaseError) {

       }
   });



    }//end of addChildInfo
*/