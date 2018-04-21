package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
   childrenAdapter adapter;
   GridView gv;
   TextView child_name;
   ImageView child_img;
   int counter = 0;
   DatabaseReference db;
   TextView label;
    String childID;
    String child_ID;
    int i =0;
    children child = new children();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educator_home);

        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الرئيسية");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_educator_home, null, false);

        mDrawerLayout.addView(contentView, 0);
        btn = (Button) findViewById(R.id.add_new_child_btn);
        gv = (GridView)findViewById(R.id.gv);
        gv.setNumColumns(2);


        label = (TextView) findViewById(R.id.NoChildren);
     db = FirebaseDatabase.getInstance().getReference().child("educator_home").child(SigninEducator.id_edu);
       db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    long x =dataSnapshot.getChildrenCount();

                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        String id = d.getKey().toString();
                        String photo = d.child("photo_URL").getValue().toString();
                        String name = d.child("first_name").getValue().toString();
                        children s = new children(photo,name,id);
                          i++;
                        children.add(s);

                    }
                    if(i>=x){

                        adapter = new childrenAdapter(educator_home.this,children);
                        gv.setAdapter(adapter);
                    }

                }
                else{

           label.setVisibility(View.VISIBLE);
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
