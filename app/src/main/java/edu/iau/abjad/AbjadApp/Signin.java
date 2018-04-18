package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import static android.view.View.VISIBLE;

public class Signin extends AppCompatActivity  implements View.OnClickListener{


    private FirebaseAuth Uath;
    private EditText ChildUsername, ChildPassword;//ResetPassword; for navgation to the activity

    private ImageView Wuser,Wpas;
    private ProgressBar PB;
    private Intent Itn;
    String username;
    static String id_child;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_child);

        //set values -noNeedForCasting-

        Uath= FirebaseAuth.getInstance();
        ChildUsername= (EditText) findViewById(R.id.Username);
        ChildPassword=(EditText) findViewById(R.id.password);
        // enter=findViewById(R.id.SendButton);
        PB= (ProgressBar) findViewById(R.id.CprogressBar);
        // back=findViewById(R.id.back);
        Wuser=(ImageView) findViewById(R.id.Wuser);
        Wpas=(ImageView) findViewById(R.id.Wpas);
        Itn =new Intent(this,child_home.class);
        //adding listeners to the buttons:
        findViewById(R.id.submit_btn_reset).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.ResetPassword).setOnClickListener(this);

        Wpas.setVisibility(View.INVISIBLE);
        Wuser.setVisibility(View.INVISIBLE);



    }//end of OnCreate



    private void signIn(){
        username = ChildUsername.getText().toString().trim();
        String password = ChildPassword.getText().toString().trim();
        int counter =0;


        //check username is not empty
        if (username.isEmpty()) {
            ChildUsername.setError("ادخل اسم المستخدم من فضلك ");
            ChildUsername.requestFocus();
            Wuser.setVisibility(VISIBLE);
            counter++;
        }


        //check password is not empty
        if (password.isEmpty()) {
            ChildPassword.setError("ادخل كلمة المرور من فضلك ");
            ChildPassword.requestFocus();
            Wpas.setVisibility(View.VISIBLE);
            counter++;
        }
        else{
            Wpas.setVisibility(View.INVISIBLE);
        }
        if(counter== 0){

            PB.setVisibility(View.VISIBLE);

            Uath.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    PB.setVisibility(View.GONE);
                    if(task.isSuccessful())
                    {

                        id_child= Uath.getCurrentUser().getUid();
                        finish();
                        startActivity(Itn);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private String username(String usr){
        if(! usr.matches("\\b[a-zA-Z][a-zA-Z0-9\\-._]{4,}\\b"))
        {
            ChildUsername.setError("تحقق من البريد الالكتروني من فضلك ");
            ChildUsername.requestFocus();
            Wuser.setVisibility(View.VISIBLE);
            return null;
        }
        else
        {
            return usr;
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
