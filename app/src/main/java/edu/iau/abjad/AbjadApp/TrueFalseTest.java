package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    MediaPlayer test_sentence_audio, feedback_audio;
    audio_URLs audio_obj = new audio_URLs();
    Button speaker_btn;
    Button true_btn;
    Button false_btn,nextTest;
    firebase_connection r;
    TextView sentenceLabel;
    String selectedSentence;
    int true_false_test_score;
    boolean flag ;
    int true_or_false;
    int sentence_number;
    ImageView abjad;
    AnimationDrawable anim;
    boolean flag2, move_child, finish_child_score;
    String audio;
    String test_id;
    String Test_letter, unitID, first_signIn;
    long startTime;
    int total_score_of_prev_tests;
    ArrayList<Intent> Rand;
    ProgressBar  loading_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("اختبار صح أم خطأ");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_true_false_test, null, false);
        myDrawerLayout.addView(contentView, 0);
        nextTest=findViewById(R.id.next);
        r = new firebase_connection();
        speaker_btn = (Button)findViewById(R.id.ListenIcon);
        true_btn = (Button)findViewById(R.id.imageButton7);
        false_btn = (Button)findViewById(R.id.imageButton6);
        test_sentence_audio = new MediaPlayer();
        feedback_audio = new MediaPlayer();
        sentenceLabel = (TextView)findViewById(R.id.phrase_true_false);
        loading_label = findViewById(R.id.loading_label_true_test);
        abjad = (ImageView) findViewById(R.id.abjad_true_false);
        abjad.setBackgroundResource(R.drawable.abjad_speak);
        anim =(AnimationDrawable) abjad.getBackground();
        flag = true;
        flag2 = true;
        finish_child_score= false;
        Rand = new ArrayList<Intent>();

        Random rand = new Random();
        true_or_false = rand.nextInt(2);
        sentence_number = rand.nextInt(4);
        final int retreive_sentence = sentence_number+1;


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //to get the test letter and unit ID from Unit interface.
        Intent  unitIntent =getIntent();
        Bundle letter_and_unitID = unitIntent.getExtras();
        if(letter_and_unitID !=null){
            Test_letter = letter_and_unitID.getString("test_letter");
            unitID = letter_and_unitID.getString("unitID");
            startTime = letter_and_unitID.getLong("startTime");
            Rand = (ArrayList)letter_and_unitID.get("Rand");
            first_signIn = letter_and_unitID.getString("first_signIn");
            if(letter_and_unitID.getInt("score") != 0){
                total_score_of_prev_tests = letter_and_unitID.getInt("score");
            }
        }


        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                sentenceLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,60);
                m.setTitle_XLarge();
                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                sentenceLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
                m.setTitle_Large();
                Log.i("scsize","Large" );

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                sentenceLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,33);
                m.setTitle_Normal();
                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                sentenceLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,27);
                m.setTitle_Small();
                Log.i("scsize","Small" );
                break;
            default:
                sentenceLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                m.setTitle_Default();

        }//end switch

       test_sentence_audio.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer player) {
                // Called when the MediaPlayer is ready to play
                anim.start();
                test_sentence_audio.start();
            }
        });

        feedback_audio.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer player) {
                if(finish_child_score){
                    abjad.setBackgroundResource(R.drawable.abjad_happy);
                    anim =(AnimationDrawable) abjad.getBackground();
                }
                // Called when the MediaPlayer is ready to play
                anim.start();
                feedback_audio.start();
            }
        });



        //Alaa
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_test_or_go_home();
            }
        });



  System.out.println("Test letter: "+ Test_letter);
        //Alaa
        r.ref.child("Tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    final String key=snapshot.getKey();
                    Log.i("KeyTest",key);
                    DatabaseReference getCurrentTestId=r.ref.child("Tests").child("test_letters");
                    ValueEventListener CurrIDEvent=new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(key!=null){
                                String lettr=snapshot.child("test_letters").getValue().toString();
                                if(lettr.equals(Test_letter)) {
                                    test_id = key;

                                    //Alaa
                                    DatabaseReference read = r.ref.child("Tests").child(test_id).child("sentences").child("sentence" + retreive_sentence);
                                    read.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                             if (dataSnapshot.exists()) {
                                                if (true_or_false == 1)
                                                    selectedSentence = dataSnapshot.child("content").getValue().toString();
                                                else {
                                                    selectedSentence = dataSnapshot.child("wrong_content").getValue().toString();
                                                }
                                                audio = dataSnapshot.child("audio_file").getValue().toString();


                                            }

                                            sentenceLabel.setText(selectedSentence);

                                            // start the instruction audio before the test begin
                                            playAudio(audio_obj.true_false_test_begin_url);
                                            loading_label.setVisibility(View.INVISIBLE);


                                            // On complete listener that fire when the instruction audio finish to start the lesson audio.
                                            test_sentence_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                @Override
                                                public void onCompletion(MediaPlayer mediaPlayer) {
                                                    if (flag == false) {
                                                        return;
                                                    }
                                                    anim.stop();
                                                    flag = false;
                                                    playAudio(audio);
                                                    setOnCompleteListener(test_sentence_audio);

                                                }
                                            });


                                            speaker_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    anim.start();
                                                    test_sentence_audio.start();
                                                    setOnCompleteListener(test_sentence_audio);
                                                }
                                            });
                                            true_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (true_or_false == 0) {
                                                        playAudio_feedback(audio_obj.wrong_answer_url);
                                                        setOnCompleteListener(feedback_audio);
                                                        true_false_test_score = 0;
                                                    } else {
                                                        playAudio_feedback(audio_obj.perfect_top_feedback);
                                                        setOnCompleteListener(feedback_audio);
                                                        true_false_test_score = 10;
                                                        finish_child_score = true;

                                                    }
                                                    //get the next intent and redirect if Iam the last intent I should stop the
                                                    // timer and calculate the score and move to home


                                                }
                                            });// end of true_btn on click listener
                                            false_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (true_or_false == 0) {
                                                        playAudio_feedback(audio_obj.perfect_top_feedback);
                                                        setOnCompleteListener(feedback_audio);
                                                        true_false_test_score = 10;
                                                        finish_child_score = true;

                                                    } else {
                                                        playAudio_feedback(audio_obj.wrong_answer_url);
                                                        setOnCompleteListener(feedback_audio);
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
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };getCurrentTestId.addValueEventListener(CurrIDEvent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
            test_sentence_audio.prepareAsync();

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

    public void playAudio_feedback(String url){
        try {

            feedback_audio.reset();
            feedback_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            feedback_audio.setDataSource(url);
            feedback_audio.prepareAsync();

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
        test_sentence_audio = null;
        feedback_audio = null;
        anim.stop();

    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            test_sentence_audio.release();
            feedback_audio.release();
            test_sentence_audio = null;
            feedback_audio = null;
            anim.stop();
        }catch(Exception e){

        }

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
                if(move_child){
                    Intent intent = new Intent(getApplicationContext(), unit_interface.class);
                    intent.putExtra("unitID",unitID);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                if(finish_child_score){
                    next_test_or_go_home();
                }

            }

        });
        flag2 = true;

    }

    @Override
    protected void onRestart() {

        super.onRestart();
        //Move to unit interface when child close the app while test or lesson
        Intent intent = new Intent(getApplicationContext(), unit_interface.class);
        intent.putExtra("unitID",unitID);
        setResult(RESULT_OK, intent);
        finish();

    }

    public void next_test_or_go_home() {
        if(Rand.size()!=0){
            Intent nextTest=Rand.get(0);
            nextTest.putExtra("unitID", unitID);
            nextTest.putExtra("test_letter", Test_letter);
            nextTest.putExtra("startTime", startTime);
            // this to pass the score of this test and previous test/s "if exist" to the next test
            total_score_of_prev_tests = total_score_of_prev_tests + true_false_test_score;
            nextTest.putExtra("score", total_score_of_prev_tests);
            Rand.remove(nextTest);
            nextTest.putExtra("Rand",Rand);
            nextTest.putExtra("first_signIn", first_signIn);
            startActivity(nextTest);
            finish();
        }
        else{
            m.endtest=true;
            m.EndTime= Calendar.getInstance().getTimeInMillis();
            m.total_tests_score = total_score_of_prev_tests + true_false_test_score;
            Intent intent = new Intent(getApplicationContext(), unit_interface.class);
            intent.putExtra("unitID",unitID);
            intent.putExtra("preIntent","readingTest");
            setResult(RESULT_OK, intent);
            System.out.println("Testttt ID: "+ test_id);
            m.test_score(test_id, unitID,startTime);
            startActivity(intent);
            finish();
        }

    }




}//end of TrueFalseTest class