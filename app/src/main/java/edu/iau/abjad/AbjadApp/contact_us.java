package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class contact_us extends menu_educator{
    menu_variables m = new menu_variables();
    ImageView email_icon,ficon,ticon,iicon, mail, bird, msg, gray_bird;
    TextView email,faccount,iaccount,taccount;
    String mail_url, bird_url, msg_url, gray_bird_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("اتصل بنا");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_contact_us, null, false);
        mDrawerLayout.addView(contentView, 0);
        email = findViewById(R.id.email_address);
        faccount= findViewById(R.id.facebook_account);
        iaccount = findViewById(R.id.instagram_account);
        taccount = findViewById(R.id.twitter_account);
        email_icon=findViewById(R.id.email_icon);
        ficon=findViewById(R.id.facebbok_icon);
        iicon=findViewById(R.id.instagram_icon);
        ticon=findViewById(R.id.twitter_icon);
        mail = findViewById(R.id.mail);
        bird = findViewById(R.id.bird);
        msg= findViewById(R.id.message);
        gray_bird=findViewById(R.id.gray_bird);

        mail_url= "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/contact_us_images%2Fnew_mail_box.png?alt=media&token=f138d3c8-2f59-485f-94c2-1371afee7aac";
        bird_url = "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/contact_us_images%2Fbirdd.png?alt=media&token=ac9ac47e-3283-4acb-9551-77ea7c8d12ab";
        msg_url="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/contact_us_images%2Fmail.png?alt=media&token=7666c9bf-5820-4eba-b802-eee14a42a045";
        gray_bird_url="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/contact_us_images%2Fnew_gray_bird.png?alt=media&token=73cae540-4c92-4759-91fb-454d0c5d2a74";

        // Display images
        Picasso.get().load(mail_url).into(mail);
        Picasso.get().load(bird_url).into(bird);
        Picasso.get().load(msg_url).into(msg);
        Picasso.get().load(gray_bird_url).into(gray_bird);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
               email.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                faccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                iaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                taccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
              email.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                faccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                iaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                taccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
              email.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                faccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                iaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                taccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
             email.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                faccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,4);
                iaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,4);
                taccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,4);

                break;
            default:
                email.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                faccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,5);
                iaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,5);
                taccount.setTextSize(TypedValue.COMPLEX_UNIT_SP,5);

        }//end switch

        email.setText("abjad.application@gmail.com");
        taccount.setText("abjad_app");
        iaccount.setText("abjad.app");
        faccount.setText("abjad.application");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),educator_home.class));
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

        //Opening the facebook account page when pressing facebook icon or facebook textView
        ficon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://m.facebook.com/abjad.application");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        faccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://m.facebook.com/abjad.application");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //Opening the twitter account page when pressing twitter icon or twitter textView
        ticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://twitter.com/abjad_app");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        taccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://twitter.com/abjad_app");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //Opening the instagram account page when pressing instagram icon or instagram textView
        iicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://instagram.com/abjad.app?utm_source=ig_profile_share&igshid=1rsae2xhwvsz9");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        iaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://instagram.com/abjad.app?utm_source=ig_profile_share&igshid=1rsae2xhwvsz9");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }//end of onCreate function

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(),educator_home.class));

    }
}//end of class
