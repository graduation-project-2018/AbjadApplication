package edu.iau.abjad.AbjadApp;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lobna on 02/03/18.
 */

public class menu_variables {
    TextView title;

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
        s.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
    }

    public void setButton_text_Normal(TextView s){
        s.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
    }

    public void setButton_text_Small(TextView s){
        s.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
    }

    public void setButton_text_Default(TextView s){
        s.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
    }


}

