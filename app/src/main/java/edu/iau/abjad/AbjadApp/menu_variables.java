package edu.iau.abjad.AbjadApp;

import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;

/**
 * Created by lobna on 02/03/18.
 */

 class menu_variables {
    TextView title;
    ImageView back_btn;
    boolean endtest=false;
    long EndTime;
    String  childTime;
    int currentScore ;
    String actual_time;
    int total_tests_score;

    firebase_connection r = new firebase_connection();
    public void setTitle_XLarge(){
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
    }

    public void setTitle_Large(){
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
    }

    public void setTitle_Normal(){
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
    }

    public void setTitle_Small(){
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
    }

    public void setTitle_Default(){
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
    }

    public void auth_setRight_icon_XLarge(EditText email, EditText pass){
        if(email != null)
        email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon, 0);
        if(pass !=null)
        pass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_gray, 0);
    }

    public void auth_setRight_icon_Large(EditText email, EditText pass){
        if(email != null)
        email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_2x, 0);
        if(pass !=null)
        pass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_2x, 0);
    }

    public void auth_setRight_icon_Normal(EditText email, EditText pass){
        if(email != null)
        email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_15x, 0);
        if(pass !=null)
        pass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_15x, 0);
    }

    public void auth_setRight_icon_Small(EditText email, EditText pass){
        if(email != null)
        email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_1x, 0);
        if(pass !=null)
        pass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_1x, 0);
    }

    public void auth_setRight_icon_Default(EditText email, EditText pass){
        if(email != null)
        email.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.email_icon_1x, 0);
        if(pass !=null)
        pass.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.password_1x, 0);
    }

    public void setButton_text_XLarge(TextView s){
        s.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
    }

    public void setButton_text_Large(TextView s){
        s.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
    }

    public void setButton_text_Normal(TextView s){
        s.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
    }

    public void setButton_text_Small(TextView s){
        s.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
    }

    public void setButton_text_Default(TextView s){
        s.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
    }

    public void  test_score(final String test_id, final String unitID, final long startTime){
        if(endtest){
            double time = EndTime - startTime;
            time = (time/1000)/60;
            actual_time = new DecimalFormat("##.##").format(time);
            total_tests_score=total_tests_score/4;
            Query query =  r.ref.child("child_takes_test").child(child_after_signin.id_child).child(unitID).orderByKey().equalTo(test_id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        try{
                            DatabaseReference read_score =  r.ref.child("child_takes_test").child(child_after_signin.id_child).child(unitID).child(test_id);
                            read_score.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (final DataSnapshot info: dataSnapshot.getChildren()){
                                        currentScore = Integer.valueOf(dataSnapshot.child("score").getValue().toString());
                                        childTime = dataSnapshot.child("time").getValue().toString();
                                    }
                                    if(currentScore<total_tests_score){
                                        r.ref.child("child_takes_test").child(child_after_signin.id_child)
                                                .child(unitID).child(test_id).child("score").setValue(total_tests_score);
                                        r.ref.child("child_takes_test").child(child_after_signin.id_child)
                                                .child(unitID).child(test_id).child("time").setValue(actual_time);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("Path score not exists!!! inside on cancel function");
                                }
                            });
                        }
                        catch (Exception e){
                            System.out.println("Can't convert string to double");
                        }
                    }
                    else{

                        r.ref.child("child_takes_test").child(child_after_signin.id_child).child(unitID).child(test_id).child("score").setValue(total_tests_score);
                        r.ref.child("child_takes_test").child(child_after_signin.id_child).child(unitID).child(test_id).child("time").setValue(actual_time);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }


}

