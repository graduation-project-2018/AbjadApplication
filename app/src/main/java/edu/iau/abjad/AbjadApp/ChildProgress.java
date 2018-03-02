package edu.iau.abjad.AbjadApp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChildProgress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_progress);
    }

    /*public static class AddChild extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_child_progress);
        }
    }*/

    public static class AddingChild extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_adding_child);
        }
    }
}
