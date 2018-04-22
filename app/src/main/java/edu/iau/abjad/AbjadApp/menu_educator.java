package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class menu_educator extends AppCompatActivity {
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mToggle;
    protected Toolbar mToolBar;
    ImageView home_icon;
    NavigationView navigationView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_educator);

        mDrawerLayout=(DrawerLayout) findViewById(R.id.menu_edu_drawer_layout);
        mToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mToolBar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolBar);

        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
                    case R.id.edit_profile:{
                        Intent intent = new Intent(menu_educator.this, educator_profile.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.change_pass:{
                        Intent intent = new Intent(menu_educator.this, change_password.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.add_child:{
                        Intent intent = new Intent(menu_educator.this, adding_child.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.delete_edu:{
                        popUpDelete();

                    }
                    case R.id.sign_out:{
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(menu_educator.this, userTypeSelection.class);
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
        Button close_btn = (Button) mView.findViewById(R.id.close_btn_delete);
        Button cancel_btn = (Button) mView.findViewById(R.id.cancel_btn_delete);
        Button  confirm= (Button) mView.findViewById(R.id.submit_btn_delete);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

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

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete code
            }
        });

    }
    }



