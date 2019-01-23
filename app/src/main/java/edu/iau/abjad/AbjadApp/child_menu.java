package edu.iau.abjad.AbjadApp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    ImageView menu_btn, back_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_child_menu);

        myDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout_child);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        myToggle= new ActionBarDrawerToggle(this,myDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        myToolBar = (Toolbar) findViewById(R.id.nav_action_bar);

        setSupportActionBar(myToolBar);
        myDrawerLayout.addDrawerListener(myToggle);
        myToggle.syncState();



        menu_btn = findViewById(R.id.menu_icon);
        back_btn = findViewById(R.id.back_button);



        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            // If navigation drawer is not open yet open it,  else close it.
            public void onClick(View view) {
                if(!myDrawerLayout.isDrawerOpen(GravityCompat.START))
                     myDrawerLayout.openDrawer(Gravity.START);
                else myDrawerLayout.closeDrawer(Gravity.END);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_profile:{
                        Intent intent = new Intent(getApplicationContext(), change_profile_photo.class);
                        startActivity(intent);
                        return true;

                    }


                    case R.id.edit_profile_child:{
                        popUp(4);
                        return true;
                    }

                  case R.id.delete_child:{
                        popUp(3);
                        return true;
                    }

                    case R.id.sign_out:{
                        finish();
                        Intent intent = new Intent(getApplicationContext(), select_user_type.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.lesson_um:{
                   /*     if(Lesson.words_counter==6){
                            Lesson.computeChildScore();
                        }*/

                        Intent intent = new Intent(child_menu.this, um.class);
                        intent.putExtra("Lessonltr","");
                        startActivity(intent);
                        return true;
                    }


                }
                return true;
            }
        });

        home_icon= findViewById(R.id.home_icon);

        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), child_home.class);
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



    public void popUp(final int i){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(child_menu.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View mView = getLayoutInflater().inflate(R.layout.activity_pop_up_repert_problem,null);
        Button cancel_btn = (Button) mView.findViewById(R.id.cancel_btn);
        email = (EditText) mView.findViewById(R.id.edu_email);
        final Button submit_btn = (Button) mView.findViewById(R.id.submit_btn);



        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edu_email = email.getText().toString();
                try {
                    read = r.ref.child("Children").child(child_after_signin.id_child).child("educator_id");
                    read.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                id = dataSnapshot.getValue().toString();

                                Query query = r.ref.child("Educators").orderByKey().equalTo(id);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot e : dataSnapshot.getChildren()) {
                                                String em = e.child("email").getValue().toString();

                                                if (em.equals(edu_email)) {
                                                   if (i == 3) {
                                                        popUpDelete();
                                                        return;

                                                    }
                                                    else if(i==4){
                                                        Intent intent = new Intent(getApplicationContext(), child_profile.class);
                                                        intent.putExtra("email", edu_email);
                                                        startActivity(intent);

                                                    }
                                                } else {
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

                            } else {
                                Intent usr = new Intent(getApplicationContext(), signin_new.class);
                                startActivity(usr);
                                finish();
                                Toast.makeText(child_menu.this, "تم حذف الطفل بنجاح", Toast.LENGTH_LONG).show();

                            }
                            
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });
                }catch (Exception e){
                    System.out.println("كاتش");
                }
            }
        });

        dialog.show();
        Window window =dialog.getWindow();
        window.setLayout(820,520);
           cancel_btn.setOnClickListener( new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   dialog.dismiss();
               }
           });
    }// end popup

    public void popUpDelete(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(child_menu.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View mView = getLayoutInflater().inflate(R.layout.activity_delete_popup,null);
        Button cancel_btn = (Button) mView.findViewById(R.id.cancel_btn_delete);
        Button  confirm= (Button) mView.findViewById(R.id.submit_btn_delete);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        dialog.show();
        Window window =dialog.getWindow();
        window.setLayout(830,520);
        cancel_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                            r.ref.child("Children").child(child_after_signin.id_child).removeValue().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("NotFound",e.getMessage());
                                }
                            });

                            r.ref.child("child_takes_lesson").child(child_after_signin.id_child).removeValue().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("NotFound",e.getMessage());
                                }
                            });
                            r.ref.child("child_takes_test").child(child_after_signin.id_child).removeValue().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("NotFound",e.getMessage());
                                }
                            });
                            r.ref.child("educator_home").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot s:dataSnapshot.getChildren()){
                                        final String eduKey=s.getKey();
                                        DatabaseReference child_eduhome=r.ref.child("educator_home").child(eduKey).child("children");
                                        ValueEventListener eventListenerhome=new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                                                    String child_id=dataSnapshot2.getKey();
                                                    if(child_id.equals(child_after_signin.id_child)){
                                                        r.ref.child("educator_home").child(eduKey).child("children").child(child_after_signin.id_child).removeValue();
                                                        signin_new.current_child_number--;
                                                        r.ref.child("educator_home").child(eduKey).child("childrenNumber").setValue(signin_new.current_child_number.toString());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        };child_eduhome.addValueEventListener(eventListenerhome);

                                    }
                                    Intent usr=new Intent(getApplicationContext(),signin_new.class);
                                    startActivity(usr);
                                    finish();
                                    Toast.makeText(child_menu.this,"تم حذف الطفل بنجاح",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
            }
        }); // end confirm button listener
    }// end Popup Delete

    } // end Class




