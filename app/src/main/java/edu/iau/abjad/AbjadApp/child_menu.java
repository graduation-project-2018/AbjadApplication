package edu.iau.abjad.AbjadApp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.util.Objects;

public class child_menu extends AppCompatActivity {
    protected DrawerLayout myDrawerLayout;
    protected ActionBarDrawerToggle myToggle;
    protected Toolbar myToolBar;
    ImageView home_icon;
    NavigationView navigationView ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_menu);


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
                    case R.id.report_problem:{
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
        final EditText edu_pass = (EditText) mView.findViewById(R.id.edu_pass);
        Button close_btn = (Button) mView.findViewById(R.id.close_btn);
        Button cancel_btn = (Button) mView.findViewById(R.id.cancel_btn);
        Button submit_btn = (Button) mView.findViewById(R.id.submit_btn);
        final TextView error_msg = (TextView) mView.findViewById(R.id.error_msg);
        error_msg.setVisibility(View.INVISIBLE);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

           close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edu_pass.setText("");
                dialog.dismiss();
            }
        });

           cancel_btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   edu_pass.setText("");
                   dialog.dismiss();
               }
           });

           submit_btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(String.valueOf(edu_pass.getText()).isEmpty()){
                       error_msg.setVisibility(View.VISIBLE);
                   }else{
                       error_msg.setVisibility(View.INVISIBLE);
                       String pass = String.valueOf(edu_pass.getText());
                   }

               }
           });
    }

    }




