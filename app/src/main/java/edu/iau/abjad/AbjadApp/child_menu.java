package edu.iau.abjad.AbjadApp;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class child_menu extends AppCompatActivity {
    protected DrawerLayout myDrawerLayout;
    protected ActionBarDrawerToggle myToggle;
    protected Toolbar myToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_menu);


        myDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout_child);
        myToggle= new ActionBarDrawerToggle(this,myDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        myToolBar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(myToolBar);

        myDrawerLayout.addDrawerListener(myToggle);

        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
