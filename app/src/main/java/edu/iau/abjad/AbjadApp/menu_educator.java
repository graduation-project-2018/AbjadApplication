package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class menu_educator extends AppCompatActivity {
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mToggle;
    protected Toolbar mToolBar;
    ImageView home_icon;

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

        home_icon=(ImageView) findViewById(R.id.home_icon);

        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Lesson.words_counter==6){
                    Lesson.computeChildScore();
                }
                Intent intent = new Intent(menu_educator.this, child_home.class);
                startActivity(intent);
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
    }



