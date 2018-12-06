package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Signin extends AppCompatActivity  implements View.OnClickListener{

    menu_variables m = new menu_variables();
    private FirebaseAuth Uath;
    private EditText ChildEmail, ChildPassword;
    private ProgressBar PB;
    private Intent Itn;
    String email;
    static String id_child;
    ImageView back_btn;
    TextView send_label, signIn_label, reset_pass_label;
    firebase_connection r = new firebase_connection();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_signin_child);

        //set values -noNeedForCasting-

        Uath= FirebaseAuth.getInstance();
        ChildEmail= (EditText) findViewById(R.id.child_email);
        ChildPassword=(EditText) findViewById(R.id.password);
        // enter=findViewById(R.id.SendButton);
        PB= (ProgressBar) findViewById(R.id.CprogressBar);
        back_btn =findViewById(R.id.back);
        send_label = findViewById(R.id.send_label_on_btn);
        reset_pass_label= findViewById(R.id.ResetPassword);

        Itn =new Intent(Signin.this,child_home.class);
        //adding listeners to the buttons:
        findViewById(R.id.submit_btn_reset).setOnClickListener(this);
        back_btn.setOnClickListener(this);
        findViewById(R.id.ResetPassword).setOnClickListener(this);
        signIn_label= findViewById(R.id.signIn_label);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                m.auth_setRight_icon_XLarge(ChildEmail,ChildPassword);
                send_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                m.auth_setRight_icon_Large(ChildEmail,ChildPassword);
                send_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                m.auth_setRight_icon_Normal(ChildEmail,ChildPassword);
                back_btn.setImageResource(R.drawable.back_btn_2x);
                send_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                m.auth_setRight_icon_Small(ChildEmail,ChildPassword);
                send_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                break;
            default:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                m.auth_setRight_icon_Default(ChildEmail,ChildPassword);
                send_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);

        }//end switch


    }//end of OnCreate

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
                    PB.setVisibility(View.GONE);
                    if(task.isSuccessful())
                    {

                        id_child= Uath.getCurrentUser().getUid();
                        Query query = r.ref.child("Children").orderByKey().equalTo(id_child);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    startActivity(Itn);
                                }
                                else{
                                    ChildEmail.setError("الرجاء كتابة البريد الإلكتروني الخاص بالطفل");
                                    ChildEmail.requestFocus();


                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                    else
                    {
                        if(task.getException().getMessage().startsWith("The email address is badly formatted")){
                            ChildEmail.setError("الرجاء كتابة البريد الإلكتروني كالتالي someone@example.com ");
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
                Intent intent = new Intent(Signin.this, ResetPassword.class);
                intent.putExtra("child", "yes");
                startActivity(intent);
                break;

            case R.id.back:
                finish();
                startActivity(new Intent(this,userTypeSelection.class));
                break;

            case R.id.submit_btn_reset:
                signIn();
                break;
        }
    }


}
