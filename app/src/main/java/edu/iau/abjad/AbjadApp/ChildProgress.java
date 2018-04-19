package edu.iau.abjad.AbjadApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static java.security.AccessController.getContext;

public class ChildProgress extends menu_educator {
    menu_variables m = new menu_variables();
    private ImageView viewChildProfile;
    private ImageView deleteChild;
    private ImageView changePass;
    private TextView nUnlokedLesson;
    private TextView nDoneLesson;
    private TextView nTimer;
    private TextView LessonNameTimer;
    private TextView highestScoreLesson;
    private TextView lessonNameScore;
    private TextView nDoneTest;
    private TextView highestScoreTest;
    private TextView testName;
    private firebase_connection lesson;
    private firebase_connection test;
    private firebase_connection child,deleteChildQ;
    private String childID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        final View contentView = inflater.inflate(R.layout.activity_child_progress, null, false);

        mDrawerLayout.addView(contentView, 0);
        //intilization
        viewChildProfile= findViewById(R.id.ChildProfile);
        deleteChild=findViewById(R.id.deleteChild);
        changePass=findViewById(R.id.changePass);
         nUnlokedLesson=findViewById(R.id.nUnlokedLesson);
         nDoneLesson=findViewById(R.id.nDoneLesson);
         nTimer=findViewById(R.id.nTimer);
         LessonNameTimer=findViewById(R.id.LessonNameTimer);
         highestScoreLesson=findViewById(R.id.highestScoreLesson);
         lessonNameScore=findViewById(R.id.hLessonNameScore);
         nDoneTest=findViewById(R.id.nDoneTest);
         highestScoreTest=findViewById(R.id.highestScoreTest);
         testName=findViewById(R.id.testName);
         lesson=new firebase_connection();
         test=new firebase_connection();
         child = new firebase_connection();
         childID="childID";//Signin.id_child;
        deleteChildQ=new firebase_connection();
         final Intent changePassword =new Intent(this, change_password.class );

         //set onClickListener for 3 buttons
         viewChildProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent childProfile =new Intent(ChildProgress.this, child_profile.class );
                childProfile.putExtra("childID",childID);
                setResult(RESULT_OK, childProfile);
                startActivity(childProfile);
            }
        });


        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(changePassword);

            }
        });

        //get data from firebase and set Text view


        test.ref.child("child_takes_test").child("childID").child("testID");
        child.ref.child("Children").child(childID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String fName=dataSnapshot.child("first_name").getValue(String.class);
                final String lName=dataSnapshot.child("last_name").getValue(String.class);
                m.title.setText(fName+" "+lName);
                deleteChild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(contentView.getContext());
                        builder.setCancelable(true);
                        builder.setTitle("تأكيد حذف المستخدم");
                        builder.setMessage("هل أنت متأكد من حذف "+fName+" "+lName);
                        builder.setPositiveButton("نعم",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
