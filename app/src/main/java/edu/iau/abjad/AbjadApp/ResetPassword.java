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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ResetPassword extends AppCompatActivity {
    ImageButton submit;
    ImageView err ;
    EditText email;
    firebase_connection r = new firebase_connection();
    String emailAddress;
    String node;
    TextView ResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_reset_password);


        submit = (ImageButton) findViewById(R.id.submit_btn_reset);
        err = (ImageView) findViewById(R.id.error_symbol);
        email = (EditText) findViewById(R.id.email_reset);
        ResetPassword = findViewById(R.id.ResetPassword);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        err.setVisibility(View.INVISIBLE);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,60);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                break;
            default:
                ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);

        }//end switch
        node = "Educators";

        Intent intent = getIntent();
        final Bundle bd = intent.getExtras();
        if(bd != null){
          node = "Children";
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailAddress = email.getText().toString();

                Query query = r.ref.child(node).orderByChild("email").equalTo(emailAddress);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            err.setVisibility(View.INVISIBLE);
                            auth.sendPasswordResetEmail(emailAddress)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),
                                                        "تم إرسال رابط تعيين كلمة المرور على بريدك الإلكتروني", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else{

                            email.setError("الرجاء إدخال البريد الإلكتروني الذي قمت بالتسجيل به مسبقا");
                            email.requestFocus();
                            err.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });





    }
}
