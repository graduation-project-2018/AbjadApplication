package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class ResetPassword extends AppCompatActivity {
    Button submit;
    ImageView err ;
    EditText email;
    firebase_connection r = new firebase_connection();
    String emailAddress;
    TextView ResetPassword;
    ImageView back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_reset_password);


        submit = findViewById(R.id.submit_btn_reset);
        err = (ImageView) findViewById(R.id.error_symbol);
        email = (EditText) findViewById(R.id.email_reset);
        ResetPassword = findViewById(R.id.ResetPassword);
        back_btn = findViewById(R.id.back_btn_in_reset);



        err.setVisibility(View.INVISIBLE);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon, 0);
                submit.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_2x, 0);
                submit.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_15x, 0);
                submit.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_1x, 0);
                submit.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                break;
            default:
                ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_1x, 0);
                submit.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        }//end switch


       // Listener of back button to return the user to Sign in page
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take the user to the previous Activity
                finish();
                Intent intent = new Intent(getApplicationContext(), signin_new.class);
                startActivity(intent);
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseAuth auth = FirebaseAuth.getInstance();
                emailAddress = email.getText().toString();
                System.out.println("email"+ emailAddress);

                if(emailAddress.isEmpty()){
                    email.setError("الرجاء إدخال البريد الإلكتروني الذي قمت بالتسجيل به مسبقا");
                    email.requestFocus();
                    err.setVisibility(View.VISIBLE);
                    return;
                }

                err.setVisibility(View.INVISIBLE);
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "تم إرسال رابط تعيين كلمة المرور على بريدك الإلكتروني", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), signin_new.class);
                                    startActivity(intent);
                                }
                                else{
                                    email.setError("الرجاء إدخال البريد الإلكتروني الذي قمت بالتسجيل به مسبقا");
                                    email.requestFocus();
                                    err.setVisibility(View.VISIBLE);
                                }
                            }
                        });

            }// onClick
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
