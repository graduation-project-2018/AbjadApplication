 package edu.iau.abjad.AbjadApp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

 public class child_after_signin extends AppCompatActivity {

     menu_variables m = new menu_variables();
     ArrayList <children> children = new ArrayList<children>();
     TextView  loading_label,noChildlabel;
    firebase_connection r = new firebase_connection();
    static String id_child ;
     childrenAdapter adapter;
     GridView gv;
     DatabaseReference db,db2;
     static children only_child;
     Guideline gr,gl ;
     String x  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
      //  m.title = (TextView) findViewById(R.id.interface_title);
       // m.title.setText("الأطفال");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Initialization
        setContentView(R.layout.activity_child_after_signin);
        gv = (GridView)findViewById(R.id.gv);
        gv.setNumColumns(2);
        loading_label = findViewById(R.id.loading_label_child_after_signin);
        gl = findViewById(R.id.gridViewGL);
        gr = findViewById(R.id.gridView2GL);
        noChildlabel = findViewById(R.id.NoChildren);
        noChildlabel.setText("لا يوجد أطفال مسجلين حاليا, لإضافة طفل جديد الرجاء الرجوع لقسم المربي");
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                noChildlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                loading_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                noChildlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                loading_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                noChildlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                loading_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);

                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                noChildlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                loading_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                break;
            default:
                noChildlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                loading_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        }//end of switch


        // to avoid craching
        if(signin_new.id_edu != null) {

            db = FirebaseDatabase.getInstance().getReference().child("educator_home").child(signin_new.id_edu).child("children");
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {

                        noChildlabel.setVisibility(View.VISIBLE);
                    } else {
                        loading_label.setVisibility(View.VISIBLE);

                        children_Events();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }//end of if block
       }//end of on create function

     public void fetch_children(DataSnapshot dataSnapshot){
         if(dataSnapshot.exists()){
             String id = dataSnapshot.getKey();
             String photo =(String)(dataSnapshot.child("photo_URL").getValue()) ;
             String name = (String) (dataSnapshot.child("first_name").getValue());
             children s = new children(photo,name,id,"childHome");
             children.add(s);
         }
     }//end of fetch_children function
     public void checkNumOfChildren(){
         db2 = FirebaseDatabase.getInstance().getReference().child("educator_home").child(signin_new.id_edu).child("childrenNumber");
         db2.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()) {
                     x = dataSnapshot.getValue().toString();
                     if(x.equals("1")){
                         gv.setNumColumns(1);
                         ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) gr.getLayoutParams();
                         lp.guidePercent = (float) 0.665;
                         gr.setLayoutParams(lp);
                         ConstraintLayout.LayoutParams llp = (ConstraintLayout.LayoutParams) gl.getLayoutParams();
                         llp.guidePercent = (float) 0.435;
                         gl.setLayoutParams(llp);
                     }

                 } else {

                 }
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {
             }
         });

     }//end of function
     public void children_Events(){

         db.addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             checkNumOfChildren();
                 fetch_children(dataSnapshot);
                 adapter = new childrenAdapter(child_after_signin.this,children);
                 gv.setAdapter(adapter);
                 loading_label.setVisibility(View.INVISIBLE);

             }
             @Override
             public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                 loading_label.setVisibility(View.VISIBLE);
                 children_changed(dataSnapshot,"change");


             }
             @Override
             public void onChildRemoved(DataSnapshot dataSnapshot) {
                 loading_label.setVisibility(View.VISIBLE);
                 children_changed(dataSnapshot,"remove");


             }

             @Override
             public void onChildMoved(DataSnapshot dataSnapshot, String s) {


             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });//end of addChildEvent Listener



     }
     public void children_changed(DataSnapshot dataSnapshot, String status){


         String  id = dataSnapshot.getKey();

         for(int i=0;i<children.size();i++){
             String current_ID = children.get(i).getChild_ID();
             if(id.equals(current_ID)){
                 if(status.equals("remove")){
                     children.remove(children.get(i));
                     break;}
                 else {
                     children.get(i).setPhoto_URL(dataSnapshot.child("photo_URL").getValue().toString());
                     children.get(i).setFirst_name(dataSnapshot.child("first_name").getValue().toString());
                     break;
                 }
             }

         }//for loop
         checkNumOfChildren();
         adapter = new childrenAdapter(child_after_signin.this,children);
         gv.setAdapter(adapter);
         loading_label.setVisibility(View.INVISIBLE);
     }//end of children_changed
}//end of class
