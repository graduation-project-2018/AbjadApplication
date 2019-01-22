package edu.iau.abjad.AbjadApp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.VideoView;
public class um extends AppCompatActivity {
    VideoView video;
    String video_url;
    String letter;
    ProgressBar loading_label;
    Uri uri;
    Intent intent,h;
    firebase_connection r = new firebase_connection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_um);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        video=findViewById(R.id.vid);
        video_url="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/Mix_Final_Version.mp4?alt=media&token=1abfe892-1179-47e5-9a78-5fe6f701df79";
        uri = Uri.parse(video_url);
        intent= new Intent(this,Lesson.class);
        h=getIntent();
        letter = h.getStringExtra("Lessonltr");
        loading_label =findViewById(R.id.loading);
        video.setVideoURI(uri);
        video.requestFocus();
        video.setOnInfoListener(new MediaPlayer.OnInfoListener()
        {


            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if(what == mp.MEDIA_INFO_BUFFERING_START){
                    loading_label.setVisibility(View.VISIBLE);
                }
                else if(what == mp.MEDIA_INFO_BUFFERING_END){
                    loading_label.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        video.start();
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {  r.ref.child("Children").child(child_after_signin.id_child).child("um").setValue("3");
                intent.putExtra("Lessonltr",letter);
                finish();
                startActivity(intent);

            }
        });
    }//end of onCreate function
}//end of class
