package edu.iau.abjad.AbjadApp;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    menu_variables m = new menu_variables();
    Button SignUpBtn;
    EditText email,confirm_email,Pass,Cpass;
    boolean condition=true;
    private FirebaseAuth mAuth;
    String id_edu;
    firebase_connection r;
    Pattern ArabicLetters,ps;
    Educator educator;
    ImageView back_btn;
    private Intent educatorHome;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_sign_up);
        SignUpBtn = (Button) findViewById(R.id.SignUpButton);
        email = (EditText) findViewById(R.id.email_field);
        confirm_email = (EditText) findViewById(R.id.confirm_email);
        Pass = (EditText) findViewById(R.id.PassFieldSU);
        Cpass = (EditText) findViewById(R.id.CPassFieldSU);
        progressBar = findViewById(R.id.signup_pg);

        ArabicLetters = Pattern.compile("^[ءئ ؤ إآ ى لآ لأ  لإ أ-ي ]+$");
        r = new firebase_connection();
        educatorHome= new Intent(getApplicationContext(),educator_home.class);
        back_btn = findViewById(R.id.back_signUp);
        ps = Pattern.compile("^[a-zA-Z ]+$");
        // Listener of back button to return the user to Sign in page
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take the user to the previous Activity
                Intent intent = new Intent(getApplicationContext(), signin_new.class);
                startActivity(intent);
                finish();
            }
        });

        progressBar.setVisibility(View.INVISIBLE);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setButton_text_XLarge(SignUpBtn);
                // change the right icon of Edit text based on screen size
                confirm_email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon, 0);
                m.auth_setRight_icon_XLarge(email,Pass);
                Cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_gray, 0);
               // Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setButton_text_Large(SignUpBtn);
                confirm_email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_2x, 0);
                m.auth_setRight_icon_Large(email,Pass);
                Cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_2x, 0);
              //  Log.i("scsize","Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setButton_text_Normal(SignUpBtn);
                confirm_email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_15x, 0);
                m.auth_setRight_icon_Normal(email,Pass);
                Cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_15x, 0);
             //   Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setButton_text_Small(SignUpBtn);
                confirm_email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_1x, 0);
                m.auth_setRight_icon_Small(email,Pass);
                Cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_1x, 0);
            //    Log.i("scsize","Small" );
                break;
            default:
                m.setButton_text_Default(SignUpBtn);
                confirm_email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_1x, 0);
                m.auth_setRight_icon_Default(email,Pass);
                Cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_1x, 0);
            //    Log.i("scsize","Default screen" );
        }//end switch


        mAuth = FirebaseAuth.getInstance();
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                condition = true;

                if (email.getText().toString().trim().isEmpty()) {
                    email.setError("قم بتعبئة الحقل بالبريد الإلكتروني");
                    email.requestFocus();
                    condition=false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    email.setError("البريد الإلكتروني ليس على النمط someone@somewhere.com ");
                    email.requestFocus();
                    condition = false;
                }
                else if (!confirm_email.getText().toString().matches(email.getText().toString().trim())){
                    confirm_email.setError("البريد الإلكتروني غير متطابق");
                    confirm_email.requestFocus();
                    condition=false;
                }

                if (Pass.getText().toString().trim().isEmpty()) {
                    Pass.setError("قم بتعبئة الحقل بكلمة المرور");
                    Pass.requestFocus();
                    condition =false;
                } else if (Pass.getText().toString().trim().length() < 6) {
                    Pass.setError("كلمة المرور يجب ان تكون اطول من 6 خانات ");
                    Pass.requestFocus();
                    condition =false;
                }
                if (Cpass.getText().toString().isEmpty()) {
                    Cpass.setError("قم بتعبئة الحقل بكلمة المرور");
                    Cpass.requestFocus();
                    condition =false;
                } else if (!Cpass.getText().toString().matches(Pass.getText().toString().trim())) {
                    Cpass.setError("كلمة المرور غير متطابقة");
                    Cpass.requestFocus();
                    condition =false;
                }
                if(condition==true){
                    addEducatorInfo();

                }
            }

        });

    }
    public void addEducatorInfo(){
        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),Pass.getText().toString().trim()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "تمت إضافة حساب المربي بنجاح", Toast.LENGTH_LONG).show();
                            id_edu = mAuth.getCurrentUser().getUid();
                            educator = new Educator(email.getText().toString().trim());
                            r.ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    r.ref.child("Educators").child(id_edu).setValue(educator);
                                }@Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                            startActivity(educatorHome);
                            finish();
                        }

                        else{
                            try{
                                FirebaseAuthException e  = (FirebaseAuthException) task.getException();
                                if(e.getMessage().contains("email address")){
                                    email.setError("البريد الإلكتروني المدخل تم استخدامه من قبل مستخدم آخر");
                                    email.requestFocus();
                                }
                                else Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                            }catch(Exception e){
                            }Toast.makeText(getApplicationContext(),"الرجاء توفير إتصال بالانترنت", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),signin_new.class));
        finish();

    }

    }


