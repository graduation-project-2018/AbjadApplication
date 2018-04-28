package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

        abjad= (ImageView) findViewById(R.id.abjad_splash);
        abjad.setBackgroundResource(R.drawable.abjad_animation);

        anim =(AnimationDrawable) abjad.getBackground();
        anim.start();
        playAudio(a.splash_screen);

        splash_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //this flag to prevent calling this method multiple times.
                if(flag == false){
                    return;
                }
                anim.stop();
                flag = false;
                Intent intent = new Intent(splash_screen.this, userTypeSelection.class);
                startActivity(intent);
            }
        });
    }

    public void playAudio(String url){
        try {

            splash_audio.reset();
            splash_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            splash_audio.setDataSource(url);
            splash_audio.prepare();
            splash_audio.start();

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
            splash_audio.release();
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
            anim.stop();

            System.out.println("onStop function");
        }catch (Exception e){
            System.err.println("Unable to stop activity");
        }

    }
}

