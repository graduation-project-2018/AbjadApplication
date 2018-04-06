package edu.iau.abjad.AbjadApp;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;


public class Lesson extends child_menu implements MediaPlayer.OnPreparedListener {
    menu_variables m = new menu_variables();
    TextView word_label;
    TextView sentence_label;
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
    MediaPlayer lesson_audio = new MediaPlayer();
    MediaPlayer audio_instruction = new MediaPlayer();
    Button speaker_btn;
    boolean flag = true; // to stop on Complete media listener
    audio_URLs audio_URLs = new audio_URLs();
    Uri audioUri;
    Button repeat_btn;
    MediaPlayer child_voice = new MediaPlayer();
    MediaRecorder recorder ;
    final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    int child_score ;




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

        ActivityCompat.requestPermissions(this,permissions , REQUEST_RECORD_AUDIO_PERMISSION);

        //if(!checkPermissionFromDevice())
           // requestPermission();




        r = new firebase_connection();
        next_lesson_btn = (Button) findViewById(R.id.next_lesson);
        word_label = (TextView) findViewById(R.id.word_label);
        sentence_label = (TextView) findViewById(R.id.sentence_label);
        lesson_pic = (ImageView) findViewById(R.id.lesson_pic);
        words_counter =0;
        speaker_btn = (Button) findViewById (R.id.speaker_btn);
        repeat_btn = (Button) findViewById(R.id.repeat_btn);
        child_score = 0;


        /*r.ref.child("Lessons").child("lesson4").child("lesson_letter").setValue("ل");
        r.ref.child("Lessons").child("lesson3").child("unitID").setValue("unit1");

        r.ref.child("Lessons").child("lesson4").child("Words").child("word1").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson4").child("Words").child("word1").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson4").child("Words").child("word1").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson4").child("Words").child("word2").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson4").child("Words").child("word2").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson4").child("Words").child("word2").child("pic_file").setValue("-");

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
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentenرce3").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence3").child("pic_file").setValue("-");*/




        //getting the lesson ID of the selected letter in Unit interface.
        Query query = r.ref.child("Lessons").orderByChild("lesson_letter").equalTo("ل");
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

                        DatabaseReference read_words = r.ref.child("Lessons").child(lessonID).child("Words");

                        read_words.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot word_ls : dataSnapshot.getChildren()){

                                    String content = word_ls.child("content").getValue().toString();
                                    String audio = word_ls.child("audio_file").getValue().toString();
                                    String pic = word_ls.child("pic_file").getValue().toString();

                                    lesson_words obj = new lesson_words(content, audio, pic,0);
                                    wordsArrayList.add(obj);
                                    word = wordsArrayList.get(words_counter).content;

                                    word_label.setText(word);
                                    check_alef();
                                    check_ta();
                                    Picasso.get().load(wordsArrayList.get(words_counter).pic_file).into(lesson_pic);


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
                                    playAudio(audio_URLs.lesson_begin);

                                    // On complete listener that fire when the instruction audio finish to start the lesson audio.
                                    lesson_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            //this flag to prevent calling this method multiple times.
                                            if(flag == false){
                                                return;
                                            }
                                            flag = false;
                                            playAudio(wordsArrayList.get(words_counter).audio_file);
                                        }
                                    });

                                    repeat_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try{
                                                lesson_audio.start();
                                            }
                                            catch (Exception e){
                                                System.out.println("Inside catch: Unable to play audio");
                                            }

                                        }
                                    });

                                    speaker_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                        }
                                    });

                                    next_lesson_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            words_counter++;

                                            if(words_counter == 4){
                                                word_label.setVisibility(View.INVISIBLE);
                                                //next_lesson_btn.setVisibility(View.INVISIBLE);
                                            }
                                            if(words_counter < 4){
                                                word = wordsArrayList.get(words_counter).content;
                                                word_label.setText(word);

                                                Picasso.get().load(wordsArrayList.get(words_counter).pic_file).into(lesson_pic);
                                                playAudio(wordsArrayList.get(words_counter).audio_file);
                                            }
                                            else if (words_counter > 3 && words_counter < 7){
                                                word = wordsArrayList.get(words_counter).content;
                                                sentence_label.setText(word);
                                                Picasso.get().load(wordsArrayList.get(words_counter).pic_file).into(lesson_pic);
                                                playAudio(wordsArrayList.get(words_counter).audio_file);
                                            }
                                            else if (words_counter == 7){
                                                // move to unit interface
                                            }

                                            check_alef();
                                            check_ta();
                                        }
                                    });
                                }

                                //Start reading sentences from firebase

                                DatabaseReference read_sentences = r.ref.child("Lessons").child(lessonID).child("sentences");
                                read_sentences.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (final DataSnapshot sentence_ls : dataSnapshot.getChildren()){
                                            String content = sentence_ls.child("content").getValue().toString();
                                            String audio = sentence_ls.child("audio_file").getValue().toString();
                                            String pic = sentence_ls.child("pic_file").getValue().toString();

                                            lesson_words obj = new lesson_words(content, audio, pic,0);
                                            wordsArrayList.add(obj);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w(null, "Failed to read value.", databaseError.toException());
                                    }
                                });


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


            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this); //takes context as a parameter.

            // we need intent to listen to the speech
            mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        /*mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,new Long(20000));
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,new Long(20000));
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,new Long(20000));
        mSpeechRecognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);*/



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
                    Log.d("4"," on onRmsChanged fuction");
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
                    playAudioInstructions(audio_URLs.not_hearing_you);

                    Log.d("6"," On Error function");

                    switch (i){
                        case 1:
                            System.out.println("ERROR_NETWORK_TIMEOUT");
                            break;
                        case 2:
                            System.out.println("ERROR_NETWORK");
                            break;
                        case 3:
                            System.out.println("ERROR_AUDIO");
                            break;
                        case 4:
                            System.out.println("ERROR_SERVER");
                            break;
                        case 5:
                            System.out.println("ERROR_CLIENT");
                            break;
                        case 6:
                            System.out.println("ERROR_SPEECH_TIMEOUT");
                            break;
                        case 7:
                            System.out.println("ERROR_NO_MATCH");
                            break;
                        case 8:
                            System.out.println(" ERROR_RECOGNIZER_BUSY");
                            break;
                        case 9:
                            System.out.println("ERROR_INSUFFICIENT_PERMISSIONS");
                            break;

                    }
                }

                @Override
                public void onResults(Bundle bundle) {
                    String starting = word.substring(0,1);
                    String ending = word.substring(word.length()-1);
                    int word_length = word.length();
                    char[] word_array = new char[word_length];
                    word_array = word.toCharArray();
                    boolean found = false;
                    int match_counter = 0 ;

                    // matches contains many results but we will display the best one and it useually the first one.
                    ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    float[] scores = bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
                    for (int i =0 ; i < scores.length ;i++){
                        Log.d( "1" ,"confidence scores " + scores[i]);
                    }


                    for(int i =0 ; i< matches.size(); i++){
                        Log.d("2", "Results " + matches.get(i));
                        LevenshteinDistance.computeEditDistance(word,matches.get(i));
                        if(matches.get(i).compareTo(word)== 0){
                            Log.d("2", "Matching true!! ");
                            playAudioInstructions(audio_URLs.perfect_top_feedback);
                            found = true;
                            break;
                        }
                    }

                    try{
                        if(found == false){
                            double max_match =0, returnValue=0;
                            String choosenPhrase="";
                            if(sentence_label.getText()==""){
                                for(int i =0 ; i<matches.size(); i++){
                                    returnValue=LevenshteinDistance.computeEditDistance(word,matches.get(i));
                                    if(max_match<returnValue){
                                        max_match = returnValue;
                                        choosenPhrase= matches.get(i);
                                    }
                                }

                            }
                            if(word_length == 3 ) {
                                for (int i = 0; i < matches.size(); i++) {
                                    match_counter = 0;
                                    for (int j = 0; j < word_array.length; j++) {
                                        if (matches.get(i).indexOf(word_array[j]) != -1) {
                                            match_counter++;
                                        }
                                    }
                                    if (match_counter >= 2)
                                        break;
                                }
                            }
                            else{

                                if(sentence_label.getText()!="") {
                                    for(int i =0 ; i<matches.size(); i++){
                                        returnValue=LevenshteinDistance.computeEditDistance(word,matches.get(i));
                                        if(max_match<returnValue){
                                            max_match = returnValue;
                                            choosenPhrase= matches.get(i);
                                        }
                                    }
                                    System.out.println("choosen Sentence: "+choosenPhrase);
                                    if(max_match>=0.89){
                                        //full score
                                        System.out.println("full score!!!!!!!");
                                    }
                                   else if(max_match>=0.75){
                                        System.out.println("11111");
                                   }
                                   else if(max_match <= 0.75 && max_match>=0.5){
                                        System.out.println("2222");

                                   }
                                   else if(max_match<=0.5 && max_match>=0.4){
                                        System.out.println("33333");
                                    }
                                   else if (max_match>= 0.25){
                                        System.out.println("44444");
                                   }

                                }

                                if(choosenPhrase.startsWith(starting)){
                                    child_score++;
                                }
                                if(choosenPhrase.endsWith(ending)){
                                    child_score++;
                                }

                            }
                        }

                    }catch(Exception e){
                        System.out.println("inside catch in if flag == false");
                        System.err.println(e.getMessage());
                    }

                }

                @Override
                public void onPartialResults(Bundle bundle) {
                    System.out.println("onPartialResults function");

                }
                @Override
                public void onEvent(int i, Bundle bundle) {
                    System.out.println("onEvent function");
                }
            });

            try{
                mic_btn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch(motionEvent.getAction()){
                            case MotionEvent.ACTION_UP:{ //user release his finger
                                mic_btn.setBackgroundResource(R.drawable.mic);
                                mSpeechRecognizer.stopListening();

                                break;
                            }
                            case MotionEvent.ACTION_DOWN:{//user press the mic button
                                mic_btn.setBackgroundResource(R.drawable.mic_red);
                                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                                break;
                            }
                        }
                        return false;
                    }
                });
            }catch(Exception e){
                System.out.println("inside catch");
            }
    }

    // to get user permisstion
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

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

    public void playAudioInstructions(String url){
        try {

            audio_instruction.reset();
           audio_instruction.setAudioStreamType(AudioManager.STREAM_MUSIC);
           audio_instruction.setDataSource(url);
           audio_instruction.prepare();
           audio_instruction.start();

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
        audio_instruction.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        lesson_audio.release();
        audio_instruction.release();
    }
    public void check_alef(){
        if(word.indexOf('أ')!= -1){
            word = word.replace('أ','ا');
        }

    }
    public void check_ta(){
        if(word.indexOf('ة')!= -1){
            word = word.replace('ة','ه');
        }
    }
}