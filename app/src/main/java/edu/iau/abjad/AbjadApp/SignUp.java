package edu.iau.abjad.AbjadApp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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
    Button SignUpBtn;
    EditText FN ,LN,Email,Pass,Cpass;
    TextView FNMsg,LNMsg,EmailMsg,PassMsg,CpassMsg;
    ImageView FNIcon,LNIcon,EmailIcon,PassIcon,CpassIcon;
    String email,password,educator_id;
    int counter;
    private FirebaseAuth mAuth;
    firebase_connection r;
    Pattern ArabicLetters;
    Pattern EnglishLetters;
    Educator educator;
 private   Intent educatorHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SignUpBtn = (Button) findViewById(R.id.SignUpButton);
        FN = (EditText) findViewById(R.id.FNFieldSU);
        LN = (EditText) findViewById(R.id.LNFieldSU);
        Email = (EditText) findViewById(R.id.EmailFieldSU);
        Pass = (EditText) findViewById(R.id.PassFieldSU);
        Cpass = (EditText) findViewById(R.id.CPassFieldSU);
        FNMsg = (TextView) findViewById(R.id.FNErrorMsgSU);
        LNMsg = (TextView) findViewById(R.id.LNErrorMsgSU);
        EmailMsg = (TextView) findViewById(R.id.EmailErrorMsgSU);
        PassMsg = (TextView) findViewById(R.id.PassErrorMsgSU);
        CpassMsg = (TextView) findViewById(R.id.CPassErrorMsgSU);
        FNIcon = (ImageView) findViewById(R.id.FNErrorIconSU);
        LNIcon = (ImageView) findViewById(R.id.LNErrorIconSU);
        EmailIcon = (ImageView) findViewById(R.id.EmailErrorIconSU);
        PassIcon = (ImageView) findViewById(R.id.PassErrorIconSU);
        CpassIcon = (ImageView) findViewById(R.id.CPassErrorIconSU);
        ArabicLetters = Pattern.compile("^[أ-ي ]+$");
        EnglishLetters = Pattern.compile("^[a-zA-Z ]+$");
        r = new firebase_connection();
        educatorHome= new Intent(this,educator_home.class);



        mAuth = FirebaseAuth.getInstance();
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FNMsg.setVisibility(View.INVISIBLE);
                LNMsg.setVisibility(View.INVISIBLE);
                EmailMsg.setVisibility(View.INVISIBLE);
                PassMsg.setVisibility(View.INVISIBLE);
                CpassMsg.setVisibility(View.INVISIBLE);
                FNIcon.setVisibility(View.INVISIBLE);
                LNIcon.setVisibility(View.INVISIBLE);
                EmailIcon.setVisibility(View.INVISIBLE);
                PassIcon.setVisibility(View.INVISIBLE);
                CpassIcon.setVisibility(View.INVISIBLE);
                counter = 0;
                if (FN.getText().toString().isEmpty()) {
                    FNMsg.setText("قم بتعبئة الحقل باسم المربي");
                    FNMsg.setVisibility(View.VISIBLE);
                    FNIcon.setVisibility(View.VISIBLE);
                } else if (!FN.getText().toString().contains(" ") || FN.getText().toString().contains(" ")) {
                    if (!ArabicLetters.matcher(FN.getText().toString()).matches()) {
                        FNMsg.setText("قم بكتابة اسم المربي باللغة العربية ");
                        FNMsg.setVisibility(View.VISIBLE);
                        FNIcon.setVisibility(View.VISIBLE);
                    } else {
                        counter++;



                    }

                }

                if (LN.getText().toString().isEmpty()) {
                    LNMsg.setText("قم بتعبئة الحقل باللقب");
                    LNMsg.setVisibility(View.VISIBLE);
                    LNIcon.setVisibility(View.VISIBLE);
                } else if (!LN.getText().toString().contains(" ") || LN.getText().toString().contains(" ")) {
                    if (!ArabicLetters.matcher(LN.getText().toString()).matches()) {
                        LNMsg.setText("قم بكتابة اسم المربي باللغة العربية ");
                        LNMsg.setVisibility(View.VISIBLE);
                        LNIcon.setVisibility(View.VISIBLE);
                    } else {
                        counter++;



                    }

                }


                email = Email.getText().toString();
                password = Pass.getText().toString();

                if (email.isEmpty()) {
                    EmailIcon.setVisibility(View.VISIBLE);
                    EmailMsg.setText("قم بتعبئة الحقل بالبريد الإلكتروني");
                    EmailMsg.setVisibility(View.VISIBLE);

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    EmailIcon.setVisibility(View.VISIBLE);
                    EmailMsg.setText("البريد الإلكتروني ليس على النمط someone@somewhere.com ");
                    EmailMsg.setVisibility(View.VISIBLE);


                } else {
                    counter++;
                   ;


                }
                if (password.isEmpty()) {
                    PassIcon.setVisibility(View.VISIBLE);
                    PassMsg.setText("قم بتعبئة الحقل بكلمة المرور");
                    PassMsg.setVisibility(View.VISIBLE);


                } else if (password.length() < 6) {
                    PassIcon.setVisibility(View.VISIBLE);
                    PassMsg.setText("كلمة المرور يجب ان تكون اطول من 6 خانات ");
                    PassMsg.setVisibility(View.VISIBLE);

                } else {
                    counter++;
                   ;


                }

                if (Cpass.getText().toString().isEmpty()) {
                    CpassMsg.setText("قم بتعبئة الحقل بكلمة المرور");
                    CpassMsg.setVisibility(View.VISIBLE);
                    CpassIcon.setVisibility(View.VISIBLE);
                } else if (!Cpass.getText().toString().matches(password)) {
                    CpassMsg.setText("المدخل في الحقل لا يطابق كلمة المرور المدخلة ");
                    CpassMsg.setVisibility(View.VISIBLE);
                    CpassIcon.setVisibility(View.VISIBLE);
                } else {
                    counter++;
                   ;


                }
                if(counter==5){
                    addEducatorInfo();
                }
            }




        });

    }
    public void addEducatorInfo(){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "تمت إضافة حساب المربي بنجاح", Toast.LENGTH_LONG).show();
                            educator_id = mAuth.getCurrentUser().getUid();
                            educator = new Educator(Email.getText().toString(),FN.getText().toString(),LN.getText().toString());
                            finish();
                            startActivity(educatorHome);



                            r.ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    r.ref.child("Educators").child(educator_id).setValue(educator);
                                }@Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        else{
                            FirebaseAuthException e  = (FirebaseAuthException) task.getException();
                            //Toast.makeText(adding_child.this, "لم تتم إضافة الطفل، الرجاء المحاولة لاحقا", Toast.LENGTH_LONG).show();//
                            //user added previously
                             if(e.getMessage().contains("email address")){
                                 EmailMsg.setText("البريد الإلكتروني المدخل مستخدم من قبل مستخدم آخر");
                                 EmailMsg.setVisibility(View.VISIBLE);
                                 EmailIcon.setVisibility(View.VISIBLE);
                             }
                             else Toast.makeText(SignUp.this,e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });




    }



    }


