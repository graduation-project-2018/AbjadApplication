package edu.iau.abjad.AbjadApp;

import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class firebaseHelper {
   DatabaseReference db;

    public firebaseHelper(DatabaseReference db) {
        this.db = db;
    }


    ArrayList<children> childrenArrayList = new ArrayList<children>();


    public void fetch(DataSnapshot dataSnapshot){
        childrenArrayList.clear();
for(DataSnapshot d: dataSnapshot.getChildren()){
    String photo = d.child("photo_URL").getValue().toString();
    String name = d.child("first_name").getValue().toString();
    children s = new children(photo,name);
    System.out.println("NAMEEEEEEEEEEEEEEEEEEEEEEEE"+name);
    childrenArrayList.add(s);
}
    }//end of fetch function

    public  ArrayList<children> getData(){

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    fetch(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetch(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return childrenArrayList;
    }//end of getData function


}//end of firebaseHelper class
