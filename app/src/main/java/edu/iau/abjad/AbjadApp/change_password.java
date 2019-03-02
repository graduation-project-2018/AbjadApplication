package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class change_password extends menu_educator {
    menu_variables m = new menu_variables();
    FirebaseUser user;
    String id_edu;
    EditText current_pass, new_pass, confirm_pass;
    String curr, new_p , con_pass;
    firebase_connection r = new firebase_connection();
    String node, email;
    Button save_changes;
    int counter =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("تغيير كلمة المرور");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_change_password, null, false);

        mDrawerLayout.addView(contentView, 0);
        current_pass = (EditText) findViewById(R.id.current_pass);
        new_pass = (EditText) findViewById(R.id.new_pass);
        confirm_pass = (EditText) findViewById(R.id.confirm_pass);
        save_changes = (Button) findViewById(R.id.save_changes);

         user = FirebaseAuth.getInstance().getCurrentUser();
         node = "Educators";

        try{
            id_edu = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){

        }




        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                m.setButton_text_XLarge(save_changes);
                m.setTitle_XLarge();
                m.auth_setRight_icon_XLarge(null,current_pass);
                m.auth_setRight_icon_XLarge(null,new_pass);
                m.auth_setRight_icon_XLarge(null,confirm_pass);

                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                m.setButton_text_Large(save_changes);
                m.setTitle_Large();
                m.auth_setRight_icon_Large(null,current_pass);
                m.auth_setRight_icon_Large(null,new_pass);
                m.auth_setRight_icon_Large(null,confirm_pass);


                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                m.setButton_text_Normal(save_changes);
                m.setTitle_Normal();
                m.auth_setRight_icon_Normal(null,current_pass);
                m.auth_setRight_icon_Normal(null,new_pass);
                m.auth_setRight_icon_Normal(null,confirm_pass);

                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
               m.setButton_text_Small(save_changes);
                m.setTitle_Small();
                m.auth_setRight_icon_Small(null, current_pass);
                m.auth_setRight_icon_Small(null, new_pass);
                m.auth_setRight_icon_Small(null, confirm_pass);

                break;
            default:
                m.setButton_text_Default(save_changes);
                m.auth_setRight_icon_Default(null, current_pass);
                m.auth_setRight_icon_Default(null, new_pass);
                m.auth_setRight_icon_Default(null, confirm_pass);
                m.setTitle_Default();

        }//end switch

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),educator_home.class));
                finish();
            }
        });


         save_changes.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 counter =0;
                 curr = current_pass.getText().toString();
                 new_p = new_pass.getText().toString();
                 con_pass = confirm_pass.getText().toString();

                 if(curr.isEmpty()){
                     current_pass.setError("الرجاء إدخال كلمة المرور الحالية");
                     current_pass.requestFocus();
                     counter++;
                 }
                 if(new_p.isEmpty()){
                     new_pass.setError("الرجاء تعبئة كلمة المرور الجديدة");
                     new_pass.requestFocus();
                     counter++;
                 }
                 if(con_pass.isEmpty()){
                     confirm_pass.setError("الرجاء إعادة تعبئة كلمة المرور الجديدة");
                     confirm_pass.requestFocus();
                     counter++;
                 }
                 if(new_p.equals(con_pass) == false){
                     confirm_pass.setError("كلمتا المرور غير متطابقتان");
                     confirm_pass.requestFocus();
                     counter++;
                 }
                 if(new_p.length()<6){
                     new_pass.setError("كلمة المرور يجب أن لا تقل عن 6 خانات");
                     new_pass.requestFocus();
                     counter++;
                 }
                 if(counter == 0){
                     Query query = r.ref.child(node).orderByKey().equalTo(id_edu);
                     query.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             if(dataSnapshot.exists()){

                                 for(DataSnapshot info : dataSnapshot.getChildren()){
                                     email = info.child("email").getValue().toString();
                                 }
                                 AuthCredential credential = EmailAuthProvider
                                         .getCredential(email, curr);
                                 user.reauthenticate(credential)
                                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if (task.isSuccessful()) {
                                                     user.updatePassword(new_p).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             if (task.isSuccessful()) {
                                                                 Toast.makeText(getApplicationContext(),
                                                                         "تم تغيير كلمة السر بنجاح", Toast.LENGTH_SHORT).show();
                                                                 new_pass.setText("");
                                                                 current_pass.setText("");
                                                                 confirm_pass.setText("");

                                                             } else {
                                                                 if(task.getException().getMessage().startsWith("The given password is invalid")){
                                                                     new_pass.setError("كلمة السر يجب أن لا تقل عن 6 خانات");
                                                                     new_pass.requestFocus();

                                                                 }
                                                                 else {
                                                                     Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                                                 }

                                                             }
                                                         }
                                                     });


                                                 } else {
                                                     current_pass.setError("الرجاء إدخال كلمة المرور الحالية بشكل صحيح");
                                                     current_pass.requestFocus();

                                                 }
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
         });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),educator_home.class));
        finish();

    }
    @Override
    public void onPause() {
        super.onPause();
        finish();

    }
}
