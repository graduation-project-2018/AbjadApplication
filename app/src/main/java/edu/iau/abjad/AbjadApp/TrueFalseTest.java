package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class TrueFalseTest extends child_menu implements MediaPlayer.OnPreparedListener  {
    menu_variables m = new menu_variables();
    MediaPlayer test_sentence_audio = new MediaPlayer();
    audio_URLs audio_obj = new audio_URLs();
    Button speaker_btn;
    Button true_btn;
    Button false_btn,nextTest;
    firebase_connection r;
    TextView sentenceLabel;
    String testID;
    //previous_Intent = getIntent();
    String selectedSentence;
    String selectedSentenceAudio;
    static int true_false_test_score;
    boolean flag ;
    int true_or_false;
    int sentence_number;
    private  static CountDownTimer countDownTimer;
    ImageView abjad;
    AnimationDrawable anim;
    boolean flag2;
    String audio;
    String test_id;
    String Test_letter;
    ArrayList<Intent> testIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("اختبار صح أم خطأ");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_true_false_test, null, false);
        myDrawerLayout.addView(contentView, 0);

        r = new firebase_connection();
        speaker_btn = (Button)findViewById(R.id.ListenIcon);
        true_btn = (Button)findViewById(R.id.imageButton7);
        false_btn = (Button)findViewById(R.id.imageButton6);
        sentenceLabel = (TextView)findViewById(R.id.textView3);
        abjad = (ImageView) findViewById(R.id.abjad_true_false);
        abjad.setBackgroundResource(R.drawable.abjad_speak);
        anim =(AnimationDrawable) abjad.getBackground();
        flag = true;
        flag2 = true;
        Random rand = new Random();
        true_or_false = rand.nextInt(2);
        sentence_number = rand.nextInt(4);
        int retreive_sentence = sentence_number+1;

                        DatabaseReference read = r.ref.child("Tests").child("Test1").child("sentences").child("sentence"+retreive_sentence);

                        read.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(true_or_false == 1)
                                        selectedSentence = dataSnapshot.child("content").getValue().toString();
                                    else{
                                    selectedSentence = dataSnapshot.child("wrong_content").getValue().toString();
                                         }
                                   audio = dataSnapshot.child("audio_file").getValue().toString();


                                }

                                    sentenceLabel.setText(selectedSentence);

                                   // start the instruction audio before the test begin
                                    anim.start();
                                    playAudio(audio_obj.true_false_test_begin_url);


                                    // On complete listener that fire when the instruction audio finish to start the lesson audio.
                                    test_sentence_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            if(flag == false){
                                                return;
                                            }
                                            anim.stop();
                                            flag = false;
                                            anim.start();
                                            playAudio(audio);
                                            setOnCompleteListener(test_sentence_audio);

                                        }
                                    });


                                    speaker_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            anim.start();
                                            playAudio(audio);
                                            setOnCompleteListener(test_sentence_audio);
                                        }
                                    });
                                   true_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(true_or_false == 0){
                                                anim.start();
                                                playAudio(audio_obj.wrong_answer_url);
                                                setOnCompleteListener(test_sentence_audio);
                                                true_false_test_score = 0;
                                            }
                                            else {
                                                abjad.setBackgroundResource(R.drawable.abjad_happy);
                                                anim =(AnimationDrawable) abjad.getBackground();
                                                anim.start();
                                                playAudio(audio_obj.perfect_top_feedback);
                                                setOnCompleteListener(test_sentence_audio);
                                                true_false_test_score = 10;

                                            }
                                            //get the next intent and redirect if Iam the last intent I should stop the
                                            // timer and calculate the score and move to home


                                        }
                                    });// end of true_btn on click listener
                                    false_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(true_or_false == 0){
                                                abjad.setBackgroundResource(R.drawable.abjad_happy);
                                                anim =(AnimationDrawable) abjad.getBackground();
                                                anim.start();
                                                playAudio(audio_obj.perfect_top_feedback);
                                                setOnCompleteListener(test_sentence_audio);
                                                true_false_test_score = 10;

                                            }
                                            else {
                                                anim.start();
                                                playAudio(audio_obj.wrong_answer_url);
                                                setOnCompleteListener(test_sentence_audio);
                                                true_false_test_score = 0;

                                            }
                                            //get the next intent and redirect if Iam the last intent I should stop the
                                            // timer and calculate the score and move to home

                                        }
                                    });//end of false_btn on click listener


                            } //onDataChange

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(null, "Failed to read value.", error.toException());
                            }
                        });


    }//end of onCreate function



    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }//end of onPrepared function
    public void playAudio(String url){
        try {

            test_sentence_audio.reset();
            test_sentence_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            test_sentence_audio.setDataSource(url);
            test_sentence_audio.prepare();
            test_sentence_audio.start();

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
    }//end of playAudio function



    @Override
    public void onDestroy() {
        super.onDestroy();
        test_sentence_audio.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        test_sentence_audio.release();
        anim.stop();

    }

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

            }

        });
        flag2 = true;

    }



}//end of TrueFalseTest class