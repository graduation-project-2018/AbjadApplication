package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import java.io.IOException;

public class splash_screen extends AppCompatActivity {
    ImageView abjad;
    AnimationDrawable anim;
    MediaPlayer splash_audio = new MediaPlayer();
    audio_URLs a = new audio_URLs();
    boolean flag = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_splash_screen);
        abjad = findViewById(R.id.abjad_splash);
        abjad.setBackgroundResource(R.drawable.abjad_animation);
        anim =(AnimationDrawable) abjad.getBackground();
        playAudio(a.splash_screen);
        splash_audio.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer player) {
                // Called when the MediaPlayer is ready to play
                anim.start();
               splash_audio.start();
            }
        });


        splash_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //this flag to prevent calling this method multiple times.
                if(flag == false){
                    return;
                }
                anim.stop();
                flag = false;
                Intent intent = new Intent(getApplicationContext(), signin_new.class);
                startActivity(intent);
            }
        });
    }

    public void playAudio(String url){
        try {

            splash_audio.reset();
            splash_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            splash_audio.setDataSource(url);
            splash_audio.prepareAsync();


        }
        catch (IOException e){
            Log.d("5","inside IOException ");
        }

        catch (IllegalArgumentException e){
            Log.d("5"," inside IllegalArgumentException");
        }

        catch (Exception e) {
            e.printStackTrace();
            Log.d("5","Inside exception");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            splash_audio = null;
            anim.stop();
            System.out.println("onDestroy function");

        }catch (Exception e){
            System.err.println("Unable to destroy activity");
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        try{
            splash_audio.release();
            splash_audio = null;
            anim.stop();

            System.out.println("onStop function");
        }catch (Exception e){
            System.err.println("Unable to stop activity");
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(getApplicationContext(), signin_new.class);
        startActivity(intent);
    }
}

