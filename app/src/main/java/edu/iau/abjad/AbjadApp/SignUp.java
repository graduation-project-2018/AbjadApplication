package edu.iau.abjad.AbjadApp;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    menu_variables m = new menu_variables();
    Button SignUpBtn;
    EditText FN ,LN,Email,Pass,Cpass;
    boolean condition=true;
    private FirebaseAuth mAuth;
    firebase_connection r;
    Pattern ArabicLetters;
    Educator educator;
    ImageView back_btn;
    private Intent educatorHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_sign_up);
        SignUpBtn = (Button) findViewById(R.id.SignUpButton);
        FN = (EditText) findViewById(R.id.FNFieldSU);
        LN = (EditText) findViewById(R.id.LNFieldSU);
        Email = (EditText) findViewById(R.id.EmailFieldSU);
        Pass = (EditText) findViewById(R.id.PassFieldSU);
        Cpass = (EditText) findViewById(R.id.CPassFieldSU);
        ArabicLetters = Pattern.compile("^[ءئ ؤ إآ ى لآ لأ  لإ أ-ي ]+$");
        r = new firebase_connection();
        educatorHome= new Intent(this,educator_home.class);
        back_btn = findViewById(R.id.back_signUp);

        // Listener of back button to return the user to Sign in page
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take the user to the previous Activity
                finish();
                Intent intent = new Intent(SignUp.this, SigninEducator.class);
                startActivity(intent);
            }
        });

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setButton_text_XLarge(SignUpBtn);
                // change the right icon of Edit text based on screen size
                FN.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_4x, 0);
                LN.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_4x, 0);
                m.auth_setRight_icon_XLarge(Email,Pass);
                Cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_gray, 0);
                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setButton_text_Large(SignUpBtn);
                FN.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_2x, 0);
                LN.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_2x, 0);
                m.auth_setRight_icon_Large(Email,Pass);
                Cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_2x, 0);
                Log.i("scsize","Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setButton_text_Normal(SignUpBtn);
                FN.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_15x, 0);
                LN.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_15x, 0);
                m.auth_setRight_icon_Normal(Email,Pass);
                Cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_15x, 0);
                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setButton_text_Small(SignUpBtn);
                FN.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_1x, 0);
                LN.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_1x, 0);
                m.auth_setRight_icon_Small(Email,Pass);
                Cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_1x, 0);
                Log.i("scsize","Small" );
                break;
            default:
                m.setButton_text_Default(SignUpBtn);
                FN.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_1x, 0);
                LN.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.user_icon_1x, 0);
                m.auth_setRight_icon_Default(Email,Pass);
                Cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_1x, 0);
                Log.i("scsize","Default screen" );
        }//end switch


        mAuth = FirebaseAuth.getInstance();
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                condition = true;
                if (FN.getText().toString().isEmpty()) {
                    FN.setError("قم بتعبئة الحقل باسم المربي");
                    FN.requestFocus();
                    condition = false;
                } else if (!FN.getText().toString().contains(" ") || FN.getText().toString().contains(" ")) {
                    if (!ArabicLetters.matcher(FN.getText().toString()).matches()) {
                        FN.setError("قم بكتابة اسم المربي باللغة العربية ");
                        FN.requestFocus();
                        condition=false;
                    }
                }

                if (LN.getText().toString().isEmpty()) {
                    LN.setError("قم بتعبئة الحقل بلقب المربي");
                    LN.requestFocus();
                    condition= false;
                } else if (!LN.getText().toString().contains(" ") || LN.getText().toString().contains(" ")) {
                    if (!ArabicLetters.matcher(LN.getText().toString()).matches()) {
                        LN.setError("قم بكتابة لقب المربي باللغة العربية ");
                        LN.requestFocus();
                        condition=false;
                    }

                }

                if (Email.getText().toString().trim().isEmpty()) {
                    Email.setError("قم بتعبئة الحقل بالبريد الإلكتروني");
                    Email.requestFocus();
                    condition=false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString().trim()).matches()) {
                    Email.setError("البريد الإلكتروني ليس على النمط someone@somewhere.com ");
                    Email.requestFocus();
                    condition = false;
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
                    Cpass.setError("المدخل في الحقل لا يطابق كلمة المرور المدخلة ");
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
        mAuth.createUserWithEmailAndPassword(Email.getText().toString().trim(),Pass.getText().toString().trim()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "تمت إضافة حساب المربي بنجاح", Toast.LENGTH_LONG).show();
                            //educatorID= mAuth.getCurrentUser().getUid();
                            SigninEducator.id_edu = mAuth.getCurrentUser().getUid();
                            educator = new Educator(Email.getText().toString().trim(),FN.getText().toString(),LN.getText().toString());
                            r.ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    r.ref.child("Educators").child(SigninEducator.id_edu).setValue(educator);
                                }@Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            finish();
                            startActivity(educatorHome);
                        }

                        else{
                            FirebaseAuthException e  = (FirebaseAuthException) task.getException();
                            //Toast.makeText(adding_child.this, "لم تتم إضافة الطفل، الرجاء المحاولة لاحقا", Toast.LENGTH_LONG).show();//
                            //user added previously
                             if(e.getMessage().contains("email address")){
                                 Email.setError("البريد الإلكتروني المدخل تم استخدامه من قبل مستخدم آخر");
                                 Email.requestFocus();
                             }
                             else Toast.makeText(SignUp.this,e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    }


