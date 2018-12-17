package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.VISIBLE;

public class SigninEducator extends AppCompatActivity  implements View.OnClickListener{
    menu_variables m = new menu_variables();
    private FirebaseAuth Uath;
    private EditText Email, EdPassword;
    private ProgressBar progressBar;
    private Intent Itn;
    static String id_edu;
    TextView signIn_label, send_label, new_account_label, reset_pass_label;
    firebase_connection  r = new firebase_connection();
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_signin_educator);

        Uath= FirebaseAuth.getInstance();
        Email= (EditText) findViewById(R.id.Email_e);
        EdPassword=(EditText) findViewById(R.id.password);
        progressBar=(ProgressBar) findViewById(R.id.EprogressBar);
        signIn_label = findViewById(R.id.signIn_label_edu);
        send_label = findViewById(R.id.send_label_on_btn_edu);
        new_account_label= findViewById(R.id.rgs);
        reset_pass_label= findViewById(R.id.ResetPassword);

        back = findViewById(R.id.back_edu);

        //adding listeners to the buttons:
        findViewById(R.id.submit_btn_reset).setOnClickListener(this);
        findViewById(R.id.ResetPassword).setOnClickListener(this);
        findViewById(R.id.rgs).setOnClickListener(this);
        Itn =new Intent(this,educator_home.class);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0B365C, android.graphics.PorterDuff.Mode.MULTIPLY);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                m.auth_setRight_icon_XLarge(Email,EdPassword);
                m.setButton_text_XLarge(send_label);
                new_account_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                m.auth_setRight_icon_Large(Email,EdPassword);
                m.setButton_text_Large(send_label);
                new_account_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                m.auth_setRight_icon_Normal(Email,EdPassword);
                m.setButton_text_Normal(send_label);
                new_account_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                m.auth_setRight_icon_Small(Email,EdPassword);
                m.setButton_text_Small(send_label);
                new_account_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                break;
            default:
                signIn_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                m.auth_setRight_icon_Default(Email,EdPassword);
                m.setButton_text_Default(send_label);
                new_account_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                reset_pass_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);

        }//end switch



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), userTypeSelection.class);
                startActivity(intent);
            }
        });
    }




    private void signIn(){
        String email = Email.getText().toString().trim();
        String password = EdPassword.getText().toString().trim();
        boolean flag = true;

        //input validation:

        if (email.isEmpty()) {
            Email.setError("أدخل البريد الإلكتروني من فضلك");
            Email.requestFocus();

            flag = false;

        }

        if (password.isEmpty()) {
            EdPassword.setError("أدخل كلمة المرور من فضلك");
            EdPassword.requestFocus();

            flag = false;

        }


        if(flag){
            progressBar.setVisibility(View.VISIBLE);
            //core of sign in
            //validate email/password then rerutn the sign in state
            // if worked then finshies this view and shows the Edu home
            Uath.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                id_edu = Uath.getCurrentUser().getUid();
                                Query query = r.ref.child("Educators").orderByKey().equalTo(id_edu);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){

                                            finish();
                                            startActivity(Itn);
                                        }
                                        else{
                                            Email.setError("الرجاء كتابة البريد الإلكتروني الخاص بالمربي");
                                            Email.requestFocus();


                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                            else {

                                if(task.getException().getMessage().startsWith("The email address is badly formatted")){
                                    Email.setError("الرجاء كتابة البريد الإلكتروني كالتالي someone@example.com ");
                                   Email.requestFocus();


                                }
                                else if(task.getException().getMessage().startsWith("There is no user record")){
                                    Email.setError("لا يوجد مستخدم بهذا الحساب ، الرجاء التحقق من البريد الإلكتروني");
                                   Email.requestFocus();


                                }
                                else if (task.getException().getMessage().startsWith("The password is invalid")){
                                   EdPassword.setError("كلمة المرور خاطئة ، الرجاء التحقق منها");
                                   EdPassword.requestFocus();

                                }
                                //Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rgs:
                finish();
                startActivity(new Intent(this,SignUp.class));
                break;

            case R.id.ResetPassword:
                finish();
                startActivity(new Intent(this,ResetPassword.class));
                break;

            case R.id.submit_btn_reset:
                signIn();
                break;
        }
    }

}

