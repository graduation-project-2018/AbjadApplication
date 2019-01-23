package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class change_profile_photo extends child_menu {
    menu_variables m = new menu_variables();
    private ImageView childImg;
    private ImageView next;
    private ImageView pre;
    private ArrayList<String> imgsUrl;
    private firebase_connection FBchildPhotoUrl;
    private int imgCont;
    private int imgIndex;
    String photo_url;
    Button SaveChanges;
    String gender="";
    ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("تغيير صورة الملف الشخصي");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_change_profile_photo, null, false);

        myDrawerLayout.addView(contentView, 0);

        next = (ImageView)findViewById(R.id.nextChangeImg);
        pre = (ImageView)findViewById(R.id.prevChangeImg);
        childImg=(ImageView)findViewById(R.id.ChangeChildImg);
        FBchildPhotoUrl = new firebase_connection();
        loading = findViewById(R.id.loading);
        imgIndex = 0;
        photo_url="";

        imgsUrl = new ArrayList<String>();
        SaveChanges=(Button)findViewById(R.id.SaveChangeImg);



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setButton_text_XLarge(SaveChanges);
                m.setTitle_XLarge();

                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setButton_text_Large(SaveChanges);
                m.setTitle_Large();

                Log.i("scsize","Large" );

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setButton_text_Normal(SaveChanges);
                m.setTitle_Normal();

                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                m.setButton_text_Small(SaveChanges);
                m.setTitle_Small();

                Log.i("scsize","Small" );
                break;
            default:
                m.setButton_text_Default(SaveChanges);
                m.setTitle_Default();

        }//end switch





                r.ref.child("Children").child(child_after_signin.id_child).child("gender").addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            gender = dataSnapshot.getValue().toString();
                            if(gender.equals("ذكر")){

                                gender = "boys";
                            }
                            else {
                                gender = "girls";
                            }
                        }//end of if block
                        FBchildPhotoUrl.ref.child("ChildPhoto").child(gender).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                                    photo_url = childSnapShot.getValue().toString();
                                    imgsUrl.add( photo_url);
                                }
                                imgCont = (int)dataSnapshot.getChildrenCount();
                                loading.setVisibility(View.VISIBLE);
                                hide_loading_label();

                                photo_url = imgsUrl.get(0);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                // load();
                next.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // load();
                       loading.setVisibility(View.VISIBLE);
                        imgIndex++;
                        if (imgIndex < imgCont) {
                            hide_loading_label();
                            photo_url=imgsUrl.get(imgIndex);
                        } else {
                            imgIndex = 0;
                           hide_loading_label();
                           photo_url=imgsUrl.get(imgIndex);

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
                           photo_url=imgsUrl.get(imgIndex);

                        } else if (imgIndex == -1) {
                            imgIndex = 5;
                            hide_loading_label();
                            photo_url=imgsUrl.get(imgIndex);

                        } else {
                            imgIndex = 0;
                            hide_loading_label();
                            photo_url=imgsUrl.get(imgIndex);

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(change_profile_photo.this, "حصل خطأ خلال تحميل الصور", Toast.LENGTH_LONG).show();

            }

        });
        //load();


        SaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FBchildPhotoUrl.ref.child("Children").child(child_after_signin.id_child).child("photo_URL").setValue(photo_url);
                r.ref.child("educator_home").child(signin_new.id_edu).child("children").child(child_after_signin.id_child).child("photo_URL").setValue(photo_url);
                Toast.makeText(change_profile_photo.this, "تم تغيير الصورة بنجاح", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),child_home.class);
                startActivity(intent);

            }
        });


        //load();
    }

    public void hide_loading_label(){

        Picasso.get().load(imgsUrl.get(imgIndex))
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

}
