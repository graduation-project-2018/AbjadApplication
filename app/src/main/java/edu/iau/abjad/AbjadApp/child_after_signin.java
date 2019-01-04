 package edu.iau.abjad.AbjadApp;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

 public class child_after_signin extends AppCompatActivity {

     menu_variables m = new menu_variables();
    ImageView child_pic;
    TextView child_name, loading_label;
    firebase_connection r = new firebase_connection();
    static String id_child ;
    ArrayList <children> children_list;
    children child_obj ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Initialization
        setContentView(R.layout.activity_child_after_signin);
        child_pic = findViewById(R.id.child_pic);
        child_name = findViewById(R.id.child_name);
        loading_label = findViewById(R.id.loading_label_child_after_signin);
        children_list = new ArrayList<children>();
        id_child = "";

        // get all educator children that exist in "educator_home" node.
        DatabaseReference read_children = r.ref.child("educator_home").child(signin_new.id_edu);
        read_children.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    String childname = child.child("first_name").getValue().toString();
                    String photo_url = child.child("photo_URL").getValue().toString();
                    id_child = child.getKey();
                    Log.i("childIID", id_child);
                    child_obj = new children(photo_url,childname,id_child);
                    children_list.add(child_obj);
                    Log.i("childname", children_list.get(0).first_name);
                    Log.i("child_ID", id_child);
                    child_name.setText(children_list.get(0).first_name);
                    Picasso.get().load(children_list.get(0).photo_URL).into(child_pic);
                    id_child = children_list.get(0).child_ID ;

                    // child photo Image view listener to move to child home interface.
                    child_pic.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            finish();
                            startActivity(new Intent(child_after_signin.this,child_home.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
