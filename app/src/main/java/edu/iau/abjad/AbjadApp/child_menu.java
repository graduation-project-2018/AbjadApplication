package edu.iau.abjad.AbjadApp;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class child_menu extends AppCompatActivity {
    menu_variables m = new menu_variables();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_menu);


        m.mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout_child);
        m.mToggle= new ActionBarDrawerToggle(this,m.mDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        m.mToolBar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(m.mToolBar);

        m.mDrawerLayout.addDrawerListener(m.mToggle);

        m.mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(m.mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
