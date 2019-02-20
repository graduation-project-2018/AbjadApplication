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
import android.widget.ProgressBar;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class splash_screen extends AppCompatActivity {
    ImageView abjad, splash_screen_bg, letters_bg;
    AnimationDrawable anim;
    MediaPlayer splash_audio = new MediaPlayer();
    audio_URLs a = new audio_URLs();
    boolean flag = true;
    ProgressBar splash_screen_progress_bar, letters_img_pg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_splash_screen);
        abjad = findViewById(R.id.abjad_splash);
        splash_screen_bg = findViewById(R.id.splash_screen_bg);
        letters_bg = findViewById(R.id.letters_bg);
        splash_screen_progress_bar = findViewById(R.id.splach_screen_pg);
        letters_img_pg = findViewById(R.id.letters_bg_pg);
        splash_screen_progress_bar.setVisibility(View.VISIBLE);
        letters_img_pg.setVisibility(View.VISIBLE);
        abjad.setBackgroundResource(R.drawable.abjad_animation);
        abjad.setVisibility(View.INVISIBLE);
        anim =(AnimationDrawable) abjad.getBackground();

        String splach_screen_bg_URL = "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/backgrounds%2Fsplash_screen_bg.jpg?alt=media&token=86579c24-759b-4e1b-ae51-91473e76a044";
        String letters_bg_URL = "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/backgrounds%2Fabjad_bg3.png?alt=media&token=c0e9c11f-016f-42ba-98b7-a308e94e2b9c";

        playAudio(a.splash_screen);
        // Display splach screen images
        Picasso.get().load(splach_screen_bg_URL).fit().memoryPolicy(MemoryPolicy.NO_CACHE).into(splash_screen_bg,new Callback() {
            @Override
            public void onSuccess(){
                splash_screen_progress_bar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onError(Exception e) {

            }

        });

        Picasso.get().load(letters_bg_URL).fit().memoryPolicy(MemoryPolicy.NO_CACHE).into(letters_bg,new Callback() {
            @Override
            public void onSuccess(){
                letters_img_pg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }

        });



        splash_audio.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer player) {
                // Called when the MediaPlayer is ready to play
                abjad.setVisibility(View.VISIBLE);
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
    protected void onPause() {
        super.onPause();
        try{
            splash_audio.release();
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

