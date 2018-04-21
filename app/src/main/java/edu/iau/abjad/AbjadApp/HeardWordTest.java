package edu.iau.abjad.AbjadApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.Random;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HeardWordTest extends child_menu  {

    //private Random word_index;
    menu_variables m = new menu_variables();
    // private DatabaseReference pointer;
    private Button w1,w2,w3;
    private FirebaseAuth auth;
    private firebase_connection r;
    private ArrayList<heard_word_content> wordsGroupList = new ArrayList<heard_word_content>();
    private MediaPlayer test_audio = new MediaPlayer();
    private MediaPlayer audio_feedback = new MediaPlayer();
    private ImageButton speaker;
    String selected_word ;
    String url;
    audio_URLs audio_urLs = new audio_URLs();
    boolean flag , child_amswer;
    static  int final_heard_child_score;
    AnimationDrawable anim;
    ImageView abjad;
    boolean flag2, move_child;


    //check if user is signed in or return 'em back a sign in look
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        /*FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser==null)
        {
            finish();
            startActivity(new Intent(this, Signin.class));
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("اختبار الكلمة المسموعة");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_heard_word_test, null, false);
        myDrawerLayout.addView(contentView, 0);


        //retriving data from DB
        r = new firebase_connection();
        w1=(Button)findViewById(R.id.word1);
        w2=(Button)findViewById(R.id.word2);
        w3=(Button)findViewById(R.id.word3);
        abjad = (ImageView) findViewById(R.id.abjad_heard_word);
        final_heard_child_score =-1;
        speaker= (ImageButton) findViewById(R.id.speaker);
        flag = true;
        flag2 = true;
        move_child = false;
        selected_word ="";

        abjad.setBackgroundResource(R.drawable.abjad_speak);
        anim =(AnimationDrawable) abjad.getBackground();

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try{
                   anim.start();
                   test_audio.start();
                   setOnCompleteListener(test_audio);
               }catch (Exception e){

               }

            }
        });

        final int x= rndm(3); //#groups
        final int y= rndm( 3); //#words in each group

        String sltG="group"+x;



        Query WG = r.ref.child("Tests").child("Test1").child("heard_word_test").orderByKey().equalTo(sltG);   //to retive the word audio


        WG.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                if(dataSnapshot.exists()){
                    for (DataSnapshot word_ls : dataSnapshot.getChildren()){


                        String content1 = word_ls.child("word1").child("content").getValue().toString();
                        String audio1 = word_ls.child("word1").child("audio_file").getValue().toString();
                        heard_word_content h1 = new heard_word_content(content1, audio1);
                        wordsGroupList.add(h1);
                        w1.setText(content1);

                        String content2 = word_ls.child("word2").child("content").getValue().toString();
                        String audio2 = word_ls.child("word2").child("audio_file").getValue().toString();
                        heard_word_content h2 = new heard_word_content(content2, audio2);
                        wordsGroupList.add(h2);
                        w2.setText(content2);
                        String content3 = word_ls.child("word3").child("content").getValue().toString();
                        String audio3 = word_ls.child("word3").child("audio_file").getValue().toString();
                        heard_word_content h3 = new heard_word_content(content3, audio3);
                        wordsGroupList.add(h3);
                        w3.setText(content3);

                        selected_word = wordsGroupList.get(y-1).content;
                        anim.start();
                        playAudioFeedback(audio_urLs.choose_heard_audio);

                        // On complete listener that fire when the instruction audio finish to start the lesson audio.
                       audio_feedback.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                //this flag to prevent calling this method multiple times.
                                if(flag == false){
                                    return;
                                }
                                flag = false;
                                anim.stop();
                                playAudio(wordsGroupList.get(y-1).audio_file);
                                anim.start();

                            }
                        });
                            setOnCompleteListener(test_audio);
                        check_ans();




                    }

                }


                else  System.out.println("ERROR WITH THE DATASNAPSHOT");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(null, "Failed to read value.", databaseError.toException());
            }
        });


    }

    public int rndm(int lim) //lim is either 3 when looking for group or 1 when audio
    {
        Random word_index =new Random();
        final int index=1+word_index.nextInt(lim);
        return index;
    }
    public void check_ans()
    {

        w1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String selected=w1.getText().toString();
                if(selected_word.equalsIgnoreCase(selected))
                {
                    abjad.setBackgroundResource(R.drawable.abjad_happy);
                    anim =(AnimationDrawable) abjad.getBackground();
                    anim.start();
                    playAudioFeedback(audio_urLs.perfect_top_feedback);
                    setOnCompleteListener(audio_feedback);
                    System.out.println("داااخل الزر");
                    if(final_heard_child_score ==-1){
                    final_heard_child_score = 10;
                        System.out.println("الجواب صح");

                    }

                }
                else
                {
                    anim.start();
                   playAudioFeedback(audio_urLs.wrong_choice);
                   setOnCompleteListener(audio_feedback);
                    if(final_heard_child_score ==-1){
                        final_heard_child_score = 0;

                    }
                }


            }
        });
        w2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String selected=w2.getText().toString();
                if(selected_word.equalsIgnoreCase(selected))
                {

                    abjad.setBackgroundResource(R.drawable.abjad_happy);
                    anim =(AnimationDrawable) abjad.getBackground();
                    anim.start();
                    playAudioFeedback(audio_urLs.perfect_top_feedback);
                    setOnCompleteListener(audio_feedback);
                    System.out.println("داااخل الزر");
                    if(final_heard_child_score ==-1){
                        final_heard_child_score = 10;
                        System.out.println("الجواب صح");

                    }


                }
                else
                {
                    anim.start();
                    playAudioFeedback(audio_urLs.wrong_choice);
                    setOnCompleteListener(audio_feedback);
                    if(final_heard_child_score ==-1){
                        final_heard_child_score = 0;

                    }
                }


            }
        });



        w3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String selected=w3.getText().toString();
                if(selected_word.equalsIgnoreCase(selected))
                {
                    abjad.setBackgroundResource(R.drawable.abjad_happy);
                    anim =(AnimationDrawable) abjad.getBackground();
                    anim.start();
                    playAudioFeedback(audio_urLs.perfect_top_feedback);
                    setOnCompleteListener(audio_feedback);
                    System.out.println("داااخل الزر");
                    if(final_heard_child_score ==-1){
                        final_heard_child_score = 10;
                        System.out.println("الجواب صح");

                    }

                }
                else
                {
                    anim.start();
                    playAudioFeedback(audio_urLs.wrong_choice);
                    setOnCompleteListener(audio_feedback);
                    if(final_heard_child_score ==-1){
                        final_heard_child_score = 0;

                    }

                }

            }
        });

    }

    public void playAudio(String url){
        try {

            test_audio.reset();
            test_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            test_audio.setDataSource(url);
            test_audio.prepare();
            test_audio.start();

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

    public void playAudioFeedback(String url){
        try {

            audio_feedback.reset();
            audio_feedback.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audio_feedback.setDataSource(url);
            audio_feedback.prepare();
            audio_feedback.start();

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
    public void onDestroy() {
        super.onDestroy();
        test_audio.release();
        audio_feedback.release();

    }

    @Override
    protected void onStop() {
        super.onStop();
        test_audio.release();
        audio_feedback.release();
        anim.stop();
    }

//this one for a button listener to signOut
    //auth.getInstance().signOut();

    public void setOnCompleteListener(MediaPlayer obj){
        obj.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //this flag to prevent calling this method multiple times.
                if (flag2 == false) {
                    return;
                }
                anim.stop();
                flag2 = false;
                abjad.setBackgroundResource(R.drawable.abjad_speak);
                anim =(AnimationDrawable) abjad.getBackground();
                if(move_child){
                    Intent intent = new Intent(HeardWordTest.this, unit_interface.class);
                    intent.putExtra("unitID",unit_interface.unitID);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }

        });
        flag2 = true;

    }


    @Override
    protected void onRestart() {

        super.onRestart();
        System.out.println("onRestart function");
       audio_feedback = new MediaPlayer();
        anim.start();
        playAudio(audio_urLs.cant_continue_test);
        move_child = true;
        setOnCompleteListener(audio_feedback);
    }

}