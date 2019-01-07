package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
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
    TextView  loading_label;
    DatabaseReference db,db2;
    Guideline gr,gl ;
    String x  ;
   TextView label;

    Boolean first_time = true;
    children child = new children();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الرئيسية");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_educator_home, null, false);

        mDrawerLayout.addView(contentView, 0);
        btn = (Button) findViewById(R.id.add_new_child_btn);

        gv = (GridView)findViewById(R.id.gv);
        gv.setNumColumns(2);
        first_time = true;
        label = (TextView) findViewById(R.id.NoChildren);
        label.setText("لا يوجد لديك أطفال مسجلين حاليا, لإضافة طفل جديد لطفا اضغط زر الإضافة");
        gl = findViewById(R.id.gridViewGL);
        gr = findViewById(R.id.gridView2GL);
        loading_label = findViewById(R.id.loading_label_child_after_signin);
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                loading_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                loading_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                loading_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);

                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                loading_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                break;
            default:
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                loading_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(new Intent(educator_home.this,select_user_type.class));
            }
        });
        // to avoid craching

        if(signin_new.id_edu != null) {
            db = FirebaseDatabase.getInstance().getReference().child("educator_home").child(signin_new.id_edu).child("children");
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {

                        label.setVisibility(View.VISIBLE);
                    } else {
                        loading_label.setVisibility(View.VISIBLE);
                        children_Events();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(educator_home.this, new_add_child.class);
                    startActivity(intent);

                }
            });//end of btn listener
        }// end if

    }//end of onCreate function



public void fetch_children(DataSnapshot dataSnapshot){
    if(dataSnapshot.exists()){
        String id = dataSnapshot.getKey();
        String photo =(String)(dataSnapshot.child("photo_URL").getValue()) ;
        String name = (String) (dataSnapshot.child("first_name").getValue());
        children s = new children(photo,name,id,"childProgress");

        children.add(s);

    }

}//end of fetch_children function
public void children_Events(){

    db.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            fetch_children(dataSnapshot);
            checkNumOfChildren();
            adapter = new childrenAdapter(educator_home.this,children);
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
    adapter = new childrenAdapter(educator_home.this,children);
    gv.setAdapter(adapter);
    loading_label.setVisibility(View.INVISIBLE);
    }//end of children_changed

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(educator_home.this,select_user_type.class));
    }

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

}//end of the class
