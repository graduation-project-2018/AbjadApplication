package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.VISIBLE;

public class SigninEducator extends AppCompatActivity  implements View.OnClickListener{

    private FirebaseAuth Uath; //pravite?
    private EditText Email, EdPassword;
    private ProgressBar progressBar;
    private ImageView Wuser,Wpas;
    private ImageButton enter;
    private Intent Itn;
    static String id_edu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_educator);

        Uath= FirebaseAuth.getInstance();
        Email= (EditText) findViewById(R.id.Email_e);
        EdPassword=(EditText) findViewById(R.id.password);
        progressBar=(ProgressBar) findViewById(R.id.EprogressBar);
        Wuser=(ImageView) findViewById(R.id.Wuser);
        Wpas=(ImageView) findViewById(R.id.Wpas);

        //adding listeners to the buttons:
        findViewById(R.id.submit_btn_reset).setOnClickListener(this);
        findViewById(R.id.ResetPassword).setOnClickListener(this);
        findViewById(R.id.rgs).setOnClickListener(this);
        Itn =new Intent(this,educator_home.class);

        Wpas.setVisibility(View.INVISIBLE);
        Wuser.setVisibility(View.INVISIBLE);
    }




    private void signIn(){
        String email = Email.getText().toString().trim();
        String password = EdPassword.getText().toString().trim();
        boolean flag = true;

        //input validation:

        if (email.isEmpty()) {
            Email.setError("أدخل البريد الإلكتروني من فضلك");
            Email.requestFocus();
            Wpas.setVisibility(View.VISIBLE);
            flag = false;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("الرجاء كتابة البريد الإلكتروني بشكل صحيح");
            Wpas.setVisibility(View.VISIBLE);
            Email.requestFocus();
            flag = false;
        }
        if (password.isEmpty()) {
            EdPassword.setError("أدخل كلمة المرور من فضلك");
            EdPassword.requestFocus();
            Wuser.setVisibility(View.VISIBLE);
            flag = false;

        }
        progressBar.setVisibility(View.VISIBLE);

        if(flag){
            //core of sign in
            //validate email/password then rerutn the sign in state
            // if worked then finshies this view and shows the Edu home
            Uath.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {

                                id_edu = Uath.getCurrentUser().getUid();
                                finish();
                                startActivity(Itn);
                            }
                            else {

                                if(task.getException().getMessage().startsWith("The email address is badly formatted")){
                                    Email.setError("الرجاء كتابة البريد الإلكتروني بشكل صحيح");
                                   Email.requestFocus();
                                    Wuser.setVisibility(VISIBLE);

                                }
                                else if(task.getException().getMessage().startsWith("There is no user record")){
                                    Email.setError("لا يوجد مستخدم بهذا الحساب ، الرجاء التحقق من البريد الإلكتروني");
                                   Email.requestFocus();
                                    Wuser.setVisibility(VISIBLE);

                                }
                                else if (task.getException().getMessage().startsWith("The password is invalid")){
                                   EdPassword.setError("كلمة المرور خاطئة ، الرجاء التحقق منها");
                                   EdPassword.requestFocus();
                                    Wpas.setVisibility(View.VISIBLE);
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

