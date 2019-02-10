package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class menu_educator extends AppCompatActivity {
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mToggle;
    protected Toolbar mToolBar;
    ImageView home_icon;
    NavigationView navigationView ;
    ImageView menu_btn, back_btn;
    firebase_connection r = new firebase_connection();
    int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_menu_educator);

        mDrawerLayout=(DrawerLayout) findViewById(R.id.menu_edu_drawer_layout);
        mToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mToolBar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolBar);

        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                width = 820;
                height = 520;
                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                width = 820;
                height = 520;
                Log.i("scsize","Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                width = 1300;
                height = 700;
                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                Log.i("scsize","Small" );
                width = 1300;
                height = 700;
                break;
            default:
                width = 1000;
                height = 600;
                Log.i("scsize","Default screen" );
        }//end switch

        menu_btn = findViewById(R.id.menu_icon);
        back_btn = findViewById(R.id.back_button);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            // If navigation drawer is not open yet open it,  else close it.
            public void onClick(View view) {
                if(!mDrawerLayout.isDrawerOpen(GravityCompat.START))
                    mDrawerLayout.openDrawer(Gravity.START);
                else mDrawerLayout.closeDrawer(Gravity.END);
            }
        });

        navigationView= (NavigationView) findViewById(R.id.menu_educator);
        home_icon=(ImageView) findViewById(R.id.home_icon);

        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menu_educator.this, educator_home.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.move_to_child_section:{
                        Intent intent = new Intent(getApplicationContext(), child_after_signin.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.edit_profile:{
                        Intent intent = new Intent(getApplicationContext(), educator_profile.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.change_pass:{
                        Intent intent = new Intent(getApplicationContext(), change_password.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.add_child:{
                        Intent intent = new Intent(getApplicationContext(), new_add_child.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.delete_edu:{
                        popUpDelete();
                        return true;

                    }

                    case R.id.sign_out:{
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), signin_new.class);
                        startActivity(intent);
                        return true;

                    }
                    case R.id.contact_us:{
                        Intent intent = new Intent(getApplicationContext(), contact_us.class);
                        startActivity(intent);


                        return true;

                    }

                }
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void popUpDelete(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(menu_educator.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View mView = getLayoutInflater().inflate(R.layout.activity_delete_popup,null);
        Button cancel_btn = (Button) mView.findViewById(R.id.cancel_btn_delete);
        Button  confirm= (Button) mView.findViewById(R.id.submit_btn_delete);
        TextView label = (TextView)mView.findViewById(R.id.delete_label);

        label.setText("هل أنت متأكد من حذف حسابك ؟");

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();


        dialog.show();
        Window window =dialog.getWindow();
        window.setLayout(width,height);
        cancel_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            r.ref.child("Educators").child(signin_new.id_edu).removeValue().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("NotFound", e.getMessage());
                                }
                            });

                            r.ref.child("educator_home").child(signin_new.id_edu).removeValue().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("NotFound", e.getMessage());
                                }
                            });
                            r.ref.child("Children").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (final DataSnapshot s : dataSnapshot.getChildren()) {
                                        final String childKey = s.getKey();
                                        final DatabaseReference child_eduhome = r.ref.child("Children").child(childKey).child("educator_id");
                                        final ValueEventListener eventListenerhome = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                    String educator = s.child("Children").child(childKey).child("educator_id").getValue(String.class);
                                                    if (educator.equals(signin_new.id_edu)) {
                                                        r.ref.child("Children").child(childKey).removeValue().addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("NotFound",e.getMessage());
                                                            }
                                                        });

                                                        r.ref.child("child_takes_lesson").child(childKey).removeValue().addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("NotFound",e.getMessage());
                                                            }
                                                        });
                                                        r.ref.child("child_takes_test").child(childKey).removeValue().addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("NotFound",e.getMessage());
                                                            }
                                                        });
                                                    }
                                                }

                                                Intent usr = new Intent(menu_educator.this, signin_new.class);
                                                startActivity(usr);
                                                finish();
                                                Toast.makeText(menu_educator.this, "تم حذف المستخدم بنجاح", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        };
                                        child_eduhome.addValueEventListener(eventListenerhome);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Log.e("Error", "deletion");
                        }

                    }


                });

            }
        }); }}