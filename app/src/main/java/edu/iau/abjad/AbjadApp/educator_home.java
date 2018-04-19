package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class educator_home extends menu_educator {
    menu_variables m = new menu_variables();
    Button btn;
   firebase_connection r = new firebase_connection();
   ArrayList <children> children = new ArrayList<children>();
   int counter = 0;
    String childID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_educator_home);

        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الرئيسية");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_educator_home, null, false);

        mDrawerLayout.addView(contentView, 0);
        btn = (Button) findViewById(R.id.add_new_child_btn);

        Query query = r.ref.child("Educator_has_child").orderByKey().equalTo(SigninEducator.id_edu);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for( DataSnapshot d : dataSnapshot.getChildren()){
                        for(DataSnapshot d2 : d.getChildren()){
                            childID = d2.getKey().toString();
                            Query query2 = r.ref.child("Children").orderByKey().equalTo(childID);
                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        for(DataSnapshot d : dataSnapshot.getChildren()){
                                            String name = d.child("first_name").getValue().toString();
                                            String photo = d.child("photo_URL").getValue().toString();

                                          children e = new children(photo, name, childID);
                                          children.add(e);

                                          System.out.println("Name: "+ children.get(0).first_name);
                                          System.out.println("Photo: "+ children.get(0).photo_URL);

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(educator_home.this, adding_child.class);
                startActivity(intent);

            }
        });






    }
}
