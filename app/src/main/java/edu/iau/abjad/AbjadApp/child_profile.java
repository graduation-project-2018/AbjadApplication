package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class child_profile extends menu_educator{



    menu_variables m = new menu_variables();
    EditText FNChild , LNChild , Username;
    TextView FNChildMsg , LNChildMsg,UsernameMsg;
    ImageView FNChildIcon,LNChildIcon, UsernameIcon,ChildImage;
    String ChildID ;
    firebase_connection r;
    childInformation child;
    DatabaseReference read;
    Button saveChanges;
    int counter;
    Pattern ArabicLetters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الملف الشخصي للطفل");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_child_profile, null, false);

        mDrawerLayout.addView(contentView, 0);

        FNChild = (EditText) findViewById(R.id.FNFieldCP);
        LNChild = (EditText) findViewById(R.id.LNFieldCP);
        //Username = (EditText) findViewById(R.id.UsernameFieldCP);
        FNChildIcon = (ImageView) findViewById(R.id.FNErrorIcon);
        LNChildIcon = (ImageView) findViewById(R.id.LNErrorIcon);
        //UsernameIcon = (ImageView) findViewById(R.id.UsernameErrorIcon);iiiiiiiiii
        ChildImage = (ImageView) findViewById(R.id.ChildImageCP);
        FNChildMsg = (TextView) findViewById(R.id.FNErrorMsgCP);
        LNChildMsg = (TextView) findViewById(R.id.LNErrorMsgCP);
        //UsernameMsg = (TextView) findViewById(R.id.UsernameErrorMsgCP);
        saveChanges = (Button) findViewById(R.id.button6);
        ArabicLetters = Pattern.compile("^[أ-ي ]+$");
        ChildID = "child1";
        child = new childInformation("FN","gn","ln","photo","uname");

        r= new firebase_connection();

read=r.ref.child("Children").child(ChildID);
read.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        child.first_name=(String) dataSnapshot.child("first_name").getValue();
        child.gender = (String) dataSnapshot.child("gender").getValue();
        child.last_name = (String) dataSnapshot.child("last_name").getValue();
        child.photo_URL = (String) dataSnapshot.child("photo_URL").getValue();
        child.username = (String) dataSnapshot.child("email").getValue();
        FNChild.setText(child.first_name);
        LNChild.setText(child.last_name);
        Username.setText(child.username);
        Picasso.get().load(child.photo_URL).into(ChildImage);
 saveChanges.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         FNChildMsg.setVisibility(View.INVISIBLE);
         LNChildMsg.setVisibility(View.INVISIBLE);
         FNChildIcon.setVisibility(View.INVISIBLE);
         LNChildIcon.setVisibility(View.INVISIBLE);
         counter=0;
         if (FNChild.getText().toString().isEmpty()) {
             FNChildMsg.setText("قم بتعبئة الحقل باسم الطفل");
             FNChildMsg.setVisibility(View.VISIBLE);
             FNChildIcon.setVisibility(View.VISIBLE);
         } else if (!FNChild.getText().toString().contains(" ") || FNChild.getText().toString().contains(" ")) {
             if (!ArabicLetters.matcher(FNChild.getText().toString()).matches()) {
                 FNChildMsg.setText("قم بكتابة اسم الطفل باللغة العربية ");
                 FNChildMsg.setVisibility(View.VISIBLE);
                 FNChildIcon.setVisibility(View.VISIBLE);
             } else {
                 counter++;
             }

         }

         if (LNChild.getText().toString().isEmpty()) {
             LNChildMsg.setText("قم بتعبئة الحقل بلقب الطفل");
             LNChildMsg.setVisibility(View.VISIBLE);
             LNChildIcon.setVisibility(View.VISIBLE);
         } else if (!LNChild.getText().toString().contains(" ") || LNChild.getText().toString().contains(" ")) {
             if (!ArabicLetters.matcher(LNChild.getText().toString()).matches()) {
                 LNChildMsg.setText("قم بكتابة لقب الطفل باللغة العربية ");
                 LNChildMsg.setVisibility(View.VISIBLE);
                 LNChildIcon.setVisibility(View.VISIBLE);
             } else {
                 counter++;



             }

         }

         if(counter==2){
             child.first_name=FNChild.getText().toString();
             child.last_name=LNChild.getText().toString();
             r.ref.child("Children").child(ChildID).setValue(child);

         }

     }
 });

    }

    @Override
    public void onCancelled(DatabaseError error) {
        Log.w(null, "Failed to read value.", error.toException());
    }
});
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent=new Intent(this,ChildProgress.class);
        if (resultCode == RESULT_OK) {
            this.ChildID=intent.getStringExtra("childID");
        }
    }
}
