package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class contact_us extends menu_educator{
    menu_variables m = new menu_variables();
    ImageView phone_icon,email_icon,ficon,ticon,iicon;
TextView email,phone,faccount,iaccount,taccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("اتصل بنا");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.activity_contact_us, null, false);
        mDrawerLayout.addView(contentView, 0);
        email = findViewById(R.id.email_address);
        phone = findViewById(R.id.phone_number);
        faccount= findViewById(R.id.facebook_account);
        iaccount = findViewById(R.id.instagram_account);
        taccount = findViewById(R.id.twitter_account);
        phone_icon= findViewById(R.id.phone_icon);
        email_icon=findViewById(R.id.email_icon);
        ficon=findViewById(R.id.facebbok_icon);
        iicon=findViewById(R.id.instagram_icon);
        ticon=findViewById(R.id.twitter_icon);
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
               email.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                phone.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                faccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                iaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                taccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
              email.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                phone.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                faccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                iaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                taccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
              email.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                phone.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                faccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                iaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                taccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
             email.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                phone.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                faccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,4);
                iaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,4);
                taccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,4);

                break;
            default:
                email.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                phone.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                faccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,5);
                iaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,5);
                taccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,5);

        }//end switch

        email.setText("abjad.application@gmail.com");
        phone.setText("013-8280398");
        taccount.setText("abjad_app_twi");
        iaccount.setText("abjad_app_ins");
        faccount.setText("abjad_app_fac");

        //Opening the dial interface when pressing phone icon or phone textView
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("tel: 0138280398"));
               startActivity(i);
            }
        });
        phone_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("tel: 0138280398"));
                startActivity(i);
            }
        });
        //Opening the email application when pressing email icon or email textView
        email_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("mailto:abjad.application@gmail.com");
                // missing 'http://' will cause crashed, when redirecting the user to a web link
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
          @Override
             public void onClick(View v) {

               Uri uri = Uri.parse("mailto:abjad.application@gmail.com");
              Intent intent = new Intent(Intent.ACTION_VIEW, uri);
               startActivity(intent);
              }
        });
        /*
        //Opening the facebook account page when pressing facebook icon or facebook textView
        ficon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("http://......");
                // missing 'http://' will cause crashed, when redirecting the user to a web link
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        faccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("http://......");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //Opening the twitter account page when pressing twitter icon or twitter textView
        ticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("http://......");
                // missing 'http://' will cause crashed, when redirecting the user to a web link
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        taccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("http://......");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //Opening the instagram account page when pressing instagram icon or instagram textView
        iicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("http://......");
                // missing 'http://' will cause crashed, when redirecting the user to a web link
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        iaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("http://......");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        */
    }//end of onCreate function

}//end of class
