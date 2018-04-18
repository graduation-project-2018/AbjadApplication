package edu.iau.abjad.AbjadApp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class child_menu extends AppCompatActivity {
    protected DrawerLayout myDrawerLayout;
    protected ActionBarDrawerToggle myToggle;
    protected Toolbar myToolBar;
    ImageView home_icon;
    NavigationView navigationView ;
    firebase_connection r = new firebase_connection();
    String edu_email;
    EditText email;
    DatabaseReference read;
    String id;
    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_menu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        myDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout_child);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        myToggle= new ActionBarDrawerToggle(this,myDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        myToolBar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(myToolBar);
        myDrawerLayout.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_profile:{
                        if(Lesson.words_counter==6){
                            Lesson.computeChildScore();
                        }
                        Intent intent = new Intent(child_menu.this, change_profile_photo.class);
                        startActivity(intent);
                        return true;

                    }
                    case R.id.change_pass_child:{
                        if(Lesson.words_counter==6){
                            Lesson.computeChildScore();
                        }
                        popUp();
                        return true;
                    }
                    case R.id.report_problem:{
                        flag = true;
                        if(Lesson.words_counter==6){
                            Lesson.computeChildScore();
                        }
                        popUp();



                        return true;
                    }
                    case R.id.sign_out:{
                        if(Lesson.words_counter==6){
                            Lesson.computeChildScore();
                        }
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(child_menu.this, userTypeSelection.class);
                        startActivity(intent);

                        return true;

                    }

                }
                return true;
            }
        });

        home_icon=(ImageView) findViewById(R.id.home_icon);

        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Lesson.words_counter==6){
                    Lesson.computeChildScore();
                }
                Intent intent = new Intent(child_menu.this, child_home.class);
                startActivity(intent);
            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myToggle.onOptionsItemSelected(item)){
            return true;
        }
                return super.onOptionsItemSelected(item);
        }



    public void popUp(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(child_menu.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View mView = getLayoutInflater().inflate(R.layout.activity_pop_up_repert_problem,null);
        Button close_btn = (Button) mView.findViewById(R.id.close_btn);
        Button cancel_btn = (Button) mView.findViewById(R.id.cancel_btn);
        email = (EditText) mView.findViewById(R.id.edu_email);
        final Button submit_btn = (Button) mView.findViewById(R.id.submit_btn);



        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();


        System.out.println("لقاه");

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edu_email = email.getText().toString();

                read = r.ref.child("Children").child(Signin.id_child).child("educator_id");
                read.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                           id = dataSnapshot.getValue().toString();
                            System.out.println("الرقم"+ id);

                        Query query = r.ref.child("Educators").orderByKey().equalTo(id);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    System.out.println("لقاه");
                                    for(DataSnapshot e: dataSnapshot.getChildren()){
                                        String em = e.child("email").getValue().toString();

                                        if(em.equals(edu_email)){
                                            if(flag == true){
                                                Intent intent = new Intent(child_menu.this, report_problem.class);
                                                startActivity(intent);
                                                flag = false;
                                            }
                                            else {
                                                Intent intent = new Intent(child_menu.this, child_change_password.class);
                                                startActivity(intent);
                                            }

                                        }
                                        else{
                                            email.setError("الرجاء إدخال البريد الإلكتروني الذي قمت بالتسجيل به مسبقا");
                                            email.requestFocus();
                                        }
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });





        dialog.show();
           close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
           cancel_btn.setOnClickListener( new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   dialog.dismiss();
               }
           });

    }
    }




