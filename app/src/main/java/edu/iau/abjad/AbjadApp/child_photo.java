package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;


public class child_photo  extends menu_educator {
    menu_variables m = new menu_variables();
    private ImageView childImg;
    private ImageView next;
    private ImageView pre;
    private ArrayList<String> imgsUri;
    private firebase_connection FBchildPhotoUri, r;
    private int imgCont;
    private int imgIndex;
    private String photo_url;
    private FirebaseAuth auth=FirebaseAuth.getInstance();
   private Bundle childObj;
   private String pass;
   private childInformation completeObj;
   private Button addChild;
   private String child_ID;
   private Intent backEducatorHome;




    // private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("إضافة طفل");
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
        addChild = findViewById(R.id.addChild);
        child_ID="";
        r=new firebase_connection();
        backEducatorHome = new Intent(this,educator_home.class);

        childObj = getIntent().getExtras();
        if(childObj!=null){
            pass=(String) childObj.get("password");
            completeObj=(childInformation)childObj.getSerializable("object");

        }

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setButton_text_XLarge(addChild);
                m.setTitle_XLarge();
                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setButton_text_Large(addChild);
                m.setTitle_Large();
                Log.i("scsize","Large" );

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setButton_text_Normal(addChild);
                m.setTitle_Normal();
                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setButton_text_Small(addChild);
                m.setTitle_Small();
                Log.i("scsize","Small" );
                break;
            default:
                m.setButton_text_Default(addChild);
                m.setTitle_Default();

        }//end switch



        FBchildPhotoUri.ref.child("ChildPhoto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                    String photoUri = childSnapShot.getValue().toString();
                    imgsUri.add(photoUri);
                }
                // load();
                imgCont = (int)dataSnapshot.getChildrenCount();
                Picasso.get().load(imgsUri.get(0)).fit().centerInside().into(childImg);
                photo_url = imgsUri.get(0);

                addChild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        completeObj.photo_URL=photo_url;
                        addChild();
                    }
                });
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
                if (imgIndex < imgCont) {
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
                if (imgIndex < imgCont & imgIndex > 0) {
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

    private void addChild(){

     auth.createUserWithEmailAndPassword(completeObj.email,pass).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(child_photo.this, "تمت إضافة الطفل بنجاح", Toast.LENGTH_LONG).show();
                            child_ID= auth.getCurrentUser().getUid();
                            addChildInfo();
                            auth.getInstance().signOut();
                            finish();
                            startActivity(backEducatorHome);
                        }

                        else{

                            Toast.makeText(child_photo.this, "لم تتم إضافة الطفل , الرجاء المحاولة لاحقا", Toast.LENGTH_LONG).show();
                        }
                    }
                });


   }//end of addChild function


    public void addChildInfo(){
                r.ref.child("Children").child(child_ID).setValue(completeObj);
                r.ref.child("Children").child(child_ID).child("educator_id").setValue(SigninEducator.id_edu);
                r.ref.child("educator_home").child(SigninEducator.id_edu).child(child_ID).child("photo_URL").setValue(photo_url);
                r.ref.child("educator_home").child(SigninEducator.id_edu).child(child_ID).child("first_name").setValue(completeObj.first_name);

    }//end of addChildInfo

}

