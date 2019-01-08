package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signin_new extends AppCompatActivity implements View.OnClickListener{

    menu_variables m = new menu_variables();
    private FirebaseAuth Uath;
    private EditText ChildEmail, ChildPassword;
    private ProgressBar PB;
    private Intent Itn;
    String email;
    static String id_edu;
    TextView send_label, signIn_label, reset_pass_label,  new_account_label, skip_label;
    firebase_connection r = new firebase_connection();
    static boolean skip_flag;
    DatabaseReference db2;
    String x;
    static Integer current_child_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.signin_new);

        //set values -noNeedForCasting-
        Uath= FirebaseAuth.getInstance();
        ChildEmail=  findViewById(R.id.child_email);
        ChildPassword=(EditText) findViewById(R.id.password);
        PB= findViewById(R.id.CprogressBar);
        send_label = findViewById(R.id.send_label_on_btn);
        reset_pass_label= findViewById(R.id.ResetPassword);
        new_account_label = findViewById(R.id.create_new_account);
        skip_label = findViewById(R.id.skip_btn);
        Itn =new Intent(this,select_user_type.class);
        skip_flag = false;

        //adding listeners to the buttons:
        findViewById(R.id.submit_btn_reset).setOnClickListener(this);
        findViewById(R.id.ResetPassword).setOnClickListener(this);
        findViewById(R.id.create_new_account).setOnClickListener(this);
        signIn_label= findViewById(R.id.signIn_label);
        PB.getIndeterminateDrawable().setColorFilter(	0xFF0B365C, android.graphics.PorterDuff.Mode.MULTIPLY);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                m.auth_setRight_icon_XLarge(ChildEmail,ChildPassword);
                m.setButton_text_XLarge(send_label);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                new_account_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                m.auth_setRight_icon_Large(ChildEmail,ChildPassword);
                m.setButton_text_Large(send_label);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                new_account_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                m.auth_setRight_icon_Normal(ChildEmail,ChildPassword);
                m.setButton_text_Normal(send_label);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                new_account_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                m.auth_setRight_icon_Small(ChildEmail,ChildPassword);
                m.setButton_text_Small(send_label);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                new_account_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                break;
            default:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                m.auth_setRight_icon_Default(ChildEmail,ChildPassword);
                m.setButton_text_Default(send_label);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                new_account_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        }//end switch

        skip_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip_flag = true;
                finish();
                startActivity(new Intent(signin_new.this,child_home.class));
            }
        });

    }// onCreate

    private void signIn(){
        email =  ChildEmail.getText().toString().trim();
        String password = ChildPassword.getText().toString().trim();
        boolean flag = true;
        //check username is not empty
        if (email.isEmpty()) {
            ChildEmail.setError("ادخل البريد الإلكتروني من فضلك ");
            ChildEmail.requestFocus();

            flag = false;
        }
        //check password is not empty
        if (password.isEmpty()) {
            ChildPassword.setError("ادخل كلمة المرور من فضلك ");
            ChildPassword.requestFocus();

            flag = false;
        }

        if(flag){
            PB.setVisibility(View.VISIBLE);
            Uath.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        id_edu= Uath.getCurrentUser().getUid();
                       checkNumOfChildren();
                        finish();
                        startActivity(Itn);
                    }
                    else
                    {
                        if(task.getException().getMessage().startsWith("The email address is badly formatted")){
                            ChildEmail.setError("الرجاء كتابة البريد الإلكتروني بالصيغة الصحيحة someone@example.com ");
                            ChildEmail.requestFocus();
                        }
                        else if(task.getException().getMessage().startsWith("There is no user record")){
                            ChildEmail.setError("لا يوجد مستخدم بهذا الحساب ، الرجاء التحقق من البريد الإلكتروني");
                            ChildEmail.requestFocus();
                        }
                        else if (task.getException().getMessage().startsWith("The password is invalid")){
                            ChildPassword.setError("كلمة المرور خاطئة ، الرجاء التحقق منها");
                            ChildPassword.requestFocus();
                        }
                        //Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ResetPassword:
                finish();
                Intent intent = new Intent(signin_new.this, ResetPassword.class);
                startActivity(intent);
                break;
            case R.id.create_new_account:
                finish();
                startActivity(new Intent(signin_new.this,SignUp.class));
                break;

            case R.id.submit_btn_reset:
                signIn();
                break;


        }
    }
    public void checkNumOfChildren(){
        db2 = FirebaseDatabase.getInstance().getReference().child("educator_home").child(signin_new.id_edu).child("childrenNumber");
        db2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    x = dataSnapshot.getValue().toString();
                    current_child_number = Integer.parseInt(x);

                } else {
                    x = "0";
                    current_child_number = Integer.parseInt(x);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }//end of function
}
