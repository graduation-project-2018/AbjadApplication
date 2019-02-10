package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class new_child_photo extends menu_educator  {
    menu_variables m = new menu_variables();
    private ImageView childImg;
    private ImageView next;
    private ImageView pre;
    private ArrayList<String> imgsUri;
    private firebase_connection FBchildPhotoUri, r;
    private int imgCont;
    private int imgIndex;
    private String photo_url;
    private Bundle childObj;
    private child_info_new completeObj;
    private Button addChild;
    private Intent backEducatorHome;
    String gender;
    String category;
    ProgressBar loading;
    int width, height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("إضافة طفل");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_new_child_photo, null, false);
        mDrawerLayout.addView(contentView, 0);
        //Intiilaization
        next = findViewById(R.id.next);
        pre = findViewById(R.id.lesson2Stars);
        childImg = findViewById(R.id.lesson1Stars);
        imgsUri = new ArrayList<String>();
        FBchildPhotoUri = new firebase_connection();
        imgIndex = 0;
        photo_url="";
        loading = findViewById(R.id.loading);
        addChild = findViewById(R.id.addChild);
        r=new firebase_connection();
        backEducatorHome = new Intent(this,educator_home.class);

        childObj = getIntent().getExtras();
        if(childObj!=null){
            completeObj=(child_info_new)childObj.getSerializable("object");
            gender = completeObj.gender;
        }

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setButton_text_XLarge(addChild);
                m.setTitle_XLarge();
                width = 820;
                height = 520;

                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setButton_text_Large(addChild);
                m.setTitle_Large();
                width = 820;
                height = 520;

                Log.i("scsize","Large" );

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setButton_text_Normal(addChild);
                m.setTitle_Normal();
                width = 1300;
                height = 700;

                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setButton_text_Small(addChild);
                m.setTitle_Small();
                width = 1300;
                height = 700;

                Log.i("scsize","Small" );
                break;
            default:
                m.setButton_text_Default(addChild);
                m.setTitle_Default();
                width = 1000;
                height = 600;

        }//end switch
        if(gender.equals("ذكر")){
            category = "boys";
        }
        else{

            category = "girls";
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        FBchildPhotoUri.ref.child("ChildPhoto").child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                    String photoUri = childSnapShot.getValue().toString();
                    imgsUri.add(photoUri);
                }
                // load();
                imgCont = (int)dataSnapshot.getChildrenCount();
                loading.setVisibility(View.VISIBLE);
                hide_loading_label();

                photo_url = imgsUri.get(0);

                //add button listener
                addChild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        completeObj.photo_URL=photo_url;

                        // get random ID from firebase, then use that ID to add the new child
                        String push_id = r.ref.push().getKey();
                        r.ref.child("Children").child(push_id).setValue(completeObj);
                        r.ref.child("Children").child(push_id).child("educator_id").setValue(signin_new.id_edu);
                        signin_new.current_child_number++;
                        r.ref.child("educator_home").child(signin_new.id_edu).child("childrenNumber").setValue( signin_new.current_child_number.toString());
                        r.ref.child("educator_home").child(signin_new.id_edu).child("children").child(push_id).child("photo_URL").setValue(photo_url);
                        r.ref.child("educator_home").child(signin_new.id_edu).child("children").child(push_id).child("first_name").setValue(completeObj.first_name);
                        Toast.makeText(getApplicationContext(), "تمت إضافة الطفل بنجاح", Toast.LENGTH_LONG).show();
                        popUp_move_or_add();

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "حدث خطأ، الرجاء المحاولة لاحقاً", Toast.LENGTH_LONG).show();

            }

        });

        //load();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                imgIndex++;
                if (imgIndex < imgCont) {
                    hide_loading_label();

                    photo_url=imgsUri.get(imgIndex);
                } else {
                    imgIndex = 0;
                    hide_loading_label();

                    photo_url=imgsUri.get(imgIndex);

                }

            }
        });
        //load();
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load();
                loading.setVisibility(View.VISIBLE);
                imgIndex--;
                if (imgIndex < imgCont & imgIndex > 0) {
                    hide_loading_label();

                    photo_url=imgsUri.get(imgIndex);

                } else if (imgIndex == -1) {
                    imgIndex = 5;
                    hide_loading_label();

                    photo_url=imgsUri.get(imgIndex);

                } else {
                    imgIndex = 0;
                    hide_loading_label();

                    photo_url=imgsUri.get(imgIndex);

                }
            }
        });
        //load();
    }
    public void hide_loading_label(){

        Picasso.get().load(imgsUri.get(imgIndex))
                .into(childImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        loading.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                });
    }//end of function hide_loading_label

    public void popUp_move_or_add(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(new_child_photo.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_first_child_adding_popup,null);
        Button move_to_child_section_btn = mView.findViewById(R.id.move_to_child_section_btn);
        Button add_another_child_btn = mView.findViewById(R.id.add_another_child_btn);
        Button move_to_edu_scetion_btn = mView.findViewById(R.id.move_to_edu_btn);


        move_to_child_section_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),child_after_signin.class));

            }
        });

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        Window window =dialog.getWindow();
        window.setLayout(width,height);
        add_another_child_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(),new_add_child.class));
            }
        });

        move_to_edu_scetion_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),educator_home.class));
            }
        });

    }// end Popup popUp_move_or_add





}
