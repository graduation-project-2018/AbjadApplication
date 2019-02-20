package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;


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
                Intent intent = new Intent(getApplicationContext(), educator_home.class);
                finish();
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.move_to_child_section:{
                        Intent intent = new Intent(getApplicationContext(), child_after_signin.class);
                        finish();
                        startActivity(intent);
                        return true;
                    }
                    case R.id.edit_profile:{
                        Intent intent = new Intent(getApplicationContext(), educator_profile.class);
                        finish();
                        startActivity(intent);
                        return true;
                    }
                    case R.id.change_pass:{
                        Intent intent = new Intent(getApplicationContext(), change_password.class);
                        finish();
                        startActivity(intent);
                        return true;
                    }
                    case R.id.add_child:{
                        Intent intent = new Intent(getApplicationContext(), new_add_child.class);
                        finish();
                        startActivity(intent);
                        return true;
                    }

                    case R.id.sign_out:{
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), signin_new.class);
                        finish();
                        startActivity(intent);
                        return true;

                    }
                    case R.id.contact_us:{
                        Intent intent = new Intent(getApplicationContext(), contact_us.class);
                        finish();
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



}// end class