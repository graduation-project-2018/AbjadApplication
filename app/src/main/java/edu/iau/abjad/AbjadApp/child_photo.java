package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;


public class child_photo  extends menu_educator {
    menu_variables b = new menu_variables();
    private ImageView childImg;
    private ImageView next;
    private ImageView pre;
    private ArrayList<String> imgsUri;
    private firebase_connection FBchildPhotoUri;
    private long imgCont;
    private int imgIndex;
    private String photo_url;

   private Bundle childObj;
   private String pass;
   private childInformation completeObj;
   private Button addChild;




    // private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b.title = (TextView) findViewById(R.id.interface_title);
        b.title.setText("إضافة طفل");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_child_photo, null, false);

        mDrawerLayout.addView(contentView, 0);

        //Intiilaization
        next = findViewById(R.id.next);
        pre = findViewById(R.id.lesson2Stars);
        childImg = findViewById(R.id.lesson1Stars);
        imgsUri = new ArrayList<String>();
        FBchildPhotoUri = new firebase_connection();
        imgIndex = 0;
        photo_url="";
        completeObj = new childInformation("","","","","");
        childObj = getIntent().getExtras();
        if(childObj!=null){
            pass=(String) childObj.get("password");
            completeObj=(childInformation)childObj.getSerializable("object");

        }






        FBchildPhotoUri.ref.child("ChildPhoto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                    imgCont = childSnapShot.getChildrenCount();
                    String photoUri = childSnapShot.getValue().toString();
                    imgsUri.add(photoUri);
                    addChild.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                }
                // load();

                Picasso.get().load(imgsUri.get(0)).fit().centerInside().into(childImg);
                photo_url = imgsUri.get(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(child_photo.this, "Error", Toast.LENGTH_LONG).show();

            }

        });
        //load();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load();
                imgIndex++;
                if (imgIndex < 6) {
                    Picasso.get().load(imgsUri.get(imgIndex)).fit().centerInside().into(childImg);
                    photo_url=imgsUri.get(imgIndex);
                } else {
                    imgIndex = 0;
                    Picasso.get().load(imgsUri.get(imgIndex)).fit().centerInside().into(childImg);
                    photo_url=imgsUri.get(imgIndex);

                }

            }
        });
        //load();
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load();
                imgIndex--;
                if (imgIndex < 6 & imgIndex > 0) {
                    Picasso.get().load(imgsUri.get(imgIndex)).fit().centerInside().into(childImg);
                    photo_url=imgsUri.get(imgIndex);

                } else if (imgIndex == -1) {
                    imgIndex = 5;
                    Picasso.get().load(imgsUri.get(imgIndex)).fit().centerInside().into(childImg);
                    photo_url=imgsUri.get(imgIndex);

                } else {
                    imgIndex = 0;
                    Picasso.get().load(imgsUri.get(imgIndex)).fit().centerInside().into(childImg);
                    photo_url=imgsUri.get(imgIndex);

                }
            }
        });
        //load();
    }
}

