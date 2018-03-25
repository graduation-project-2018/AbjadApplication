package edu.iau.abjad.AbjadApp;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Lesson extends child_menu implements MediaPlayer.OnPreparedListener {
    menu_variables m = new menu_variables();
    TextView word_label;
    Button mic_btn;
    SpeechRecognizer mSpeechRecognizer ;
    Intent mSpeechRecognizerIntent ;
    Button next_lesson_btn;
    int words_counter;
    String word;
    firebase_connection r;
    ImageView lesson_pic;
    String lessonID;
    ArrayList <lesson_words> wordsArrayList = new ArrayList<lesson_words>();
    ArrayList <lesson_sentences> sentencesArrayList = new ArrayList<lesson_sentences>();
    MediaPlayer lesson_audio = new MediaPlayer();
    MediaPlayer lesson_begin_audio = new MediaPlayer();
    Button speaker_btn;
    private  static CountDownTimer countDownTimer;
    boolean flag = true;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("حرف ( م )");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_lesson, null, false);

        myDrawerLayout.addView(contentView, 0);




        r = new firebase_connection();
        next_lesson_btn = (Button) findViewById(R.id.next_lesson);
        word_label = (TextView) findViewById(R.id.word_label);
        lesson_pic = (ImageView) findViewById(R.id.lesson_pic);
        words_counter =0;
        speaker_btn = (Button) findViewById(R.id.speaker_btn);


        /*r.ref.child("Lessons").child("lesson3").child("lesson_letter").setValue("ل");
        r.ref.child("Lessons").child("lesson3").child("unitID").setValue("unit1");

        r.ref.child("Lessons").child("lesson3").child("Words").child("word1").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word1").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word1").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson3").child("Words").child("word2").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word2").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word2").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson3").child("Words").child("word3").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word3").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word3").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson3").child("Words").child("word4").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word4").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word4").child("pic_file").setValue("-");


        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence1").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence1").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence1").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence2").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence2").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence2").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence3").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence3").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence3").child("pic_file").setValue("-");*/





        //getting the lesson ID of the selected letter in Unit interface.
        Query query = r.ref.child("Lessons").orderByChild("lesson_letter").equalTo("م");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d( "1","Exist!!!!!");

                    for (DataSnapshot letter : dataSnapshot.getChildren()) {
                         lessonID = letter.getKey();
                        Log.d( "1","Lesson ID: "+ lessonID);

                    }
                    if(lessonID != null){

                        DatabaseReference read = r.ref.child("Lessons").child(lessonID).child("Words");

                        read.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot word_ls : dataSnapshot.getChildren()){

                                    String content = word_ls.child("content").getValue().toString();
                                    String audio = word_ls.child("audio_file").getValue().toString();
                                    String pic = word_ls.child("pic_file").getValue().toString();

                                    lesson_words obj = new lesson_words(content, audio, pic);
                                    wordsArrayList.add(obj);
                                    word = wordsArrayList.get(words_counter).content;

                                    word_label.setText(word);
                                    check_alef();
                                    Picasso.get().load(wordsArrayList.get(words_counter).pic_file).into(lesson_pic);
                                    String lesson_begin_url = "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D9%88%D8%A7%D8%AC%D9%87%D8%A9%20%D8%A7%D9%84%D8%AF%D8%B1%D8%B3%20(%D8%A3%D9%88%D9%84%20%D9%86%D8%B5%D9%8A%D8%AD%D8%A9%20%D8%8C%20%D8%A8%D8%AF%D8%A7%D9%8A%D8%A9%20%D9%83%D9%84%20%D8%AF%D8%B1%D8%B3).mp3?alt=media&token=dc8cc360-de38-4eab-96b6-256a602b2f86";

                                   /*countDownTimer= new CountDownTimer(10000, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                            System.out.println("onTick methed");
                                        }

                                        public void onFinish() {

                                            System.out.println("onFinish methed");
                                            playAudio(wordsArrayList.get(words_counter).audio_file);
                                        }
                                    }.start();*/

                                    // start the instruction audio before the lesson begin
                                    playAudio(lesson_begin_url);


                                    // On complete listener that fire when the instruction audio finish to start the lesson audio.
                                    lesson_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            if(flag == false){
                                                return;
                                            }
                                            flag = false;
                                            playAudio(wordsArrayList.get(words_counter).audio_file);

                                        }
                                    });

                                    speaker_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                            lesson_audio.start();
                                        }
                                    });

                                    next_lesson_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            words_counter++;
                                            if(words_counter == 3){
                                                next_lesson_btn.setVisibility(View.INVISIBLE);
                                            }
                                            word = wordsArrayList.get(words_counter).content;
                                            word_label.setText(word);
                                            check_alef();
                                            Picasso.get().load(wordsArrayList.get(words_counter).pic_file).into(lesson_pic);
                                            playAudio(wordsArrayList.get(words_counter).audio_file);
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(null, "Failed to read value.", error.toException());
                            }
                        });
                    }
                }
                else{
                    Log.d( "1","Not Exist!!!!!");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(" ", "loadPost:onCancelled", databaseError.toException());
            }
        });

      //******* Starting speech recognition code ********
        mic_btn = (Button) findViewById(R.id.mic_btn);

        checkPermission();

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this); //takes context as a parameter.

        // we need intent to listen to the speech
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        //set the language that we want to listen for.
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA");

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d("5"," onBeginningOfSpeech function");
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                Log.d("4"," on Buffer Received fuction");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d("3"," At end of speech function");
            }

            @Override
            public void onError(int i) {
                Log.d("6"," On Error function");
            }

            @Override
            public void onResults(Bundle bundle) {
                    // matches contains many results but we will display the best one and it useually the first one.
                    ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    float[] scores = bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
                    for (int i =0 ; i < scores.length ;i++){
                        Log.d( "1","confidence scores " + scores[i]);
                    }

                    for(int i =0 ; i< matches.size(); i++){
                        Log.d("2", "Results " + matches.get(i));
                        if(matches.get(i).compareTo(word)== 0){
                            Log.d("2", "Matching true!! ");
                            break;
                        }
                    }
                    if(matches != null){
                        //word_label.setText(matches.get(0));
                    }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }
            @Override
            public void onEvent(int i, Bundle bundle) {
            }
        });

        mic_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:{ //user release his finger
                      mSpeechRecognizer.stopListening();

                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{ //user press the mic button
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
         //second parameter is the permission type that we want to check
            if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED)){
                //open the device setting to get the permission
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package: " + getPackageName()));
                startActivity(intent);
                finish(); // we can not use the application if the permission not given, so finish the app.
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    public void playAudio(String url){
        try {

            lesson_audio.reset();
            lesson_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            lesson_audio.setDataSource(url);
            lesson_audio.prepare();
            lesson_audio.start();

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
        lesson_audio.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        lesson_audio.release();
    }

    public void check_alef(){
        if(word.indexOf('أ')!= -1){

            word = word.replace('أ','ا');
            Log.d("5","Word is: "+ word);
        }

    }

}