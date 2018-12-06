package edu.iau.abjad.AbjadApp;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionProvider;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ReadingTest extends child_menu {
    menu_variables m = new menu_variables();
    Button mic_btn, speaker_btn;
    static firebase_connection r;
    TextView word_test_label, sentence_test_label ;
    final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {android.Manifest.permission.RECORD_AUDIO};
    static String test_id ;
    int choose_phrase ;
    Button next;
    int chosen_index;
    Random rand = new Random();
    String chosen_word, path, word, word_audio ;
    SpeechRecognizer mSpeechRecognizer ;
    Intent mSpeechRecognizerIntent ;
    boolean isEndOfSpeech ;
    audio_URLs audio_URLs = new audio_URLs();
    MediaPlayer test_audio = new MediaPlayer();
    MediaPlayer feedback_audio = new MediaPlayer();
    int child_score;
    static  int reading_child_score;
    boolean flag ;
    ImageView abjad;
    AnimationDrawable anim;
    boolean flag2, move_child ;
    TextView nextLabel;

    //Alaa
    firebase_connection Test_Id,testIdq2;
    String Test_letter;
    ArrayList<Intent> testIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("اختبار القراءة");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_reading_test, null, false);

        myDrawerLayout.addView(contentView, 0);
        //to get user permission of mice
        ActivityCompat.requestPermissions(this,permissions , REQUEST_RECORD_AUDIO_PERMISSION);

        abjad =  findViewById(R.id.abjad_reading_test);
        abjad.setBackgroundResource(R.drawable.abjad_speak);
        anim =(AnimationDrawable) abjad.getBackground();

        mic_btn=  findViewById(R.id.test_mic_btn);
        speaker_btn =  findViewById(R.id.test_speaker_btn);
        r = new firebase_connection();
        word_test_label = findViewById(R.id.word_test);
        sentence_test_label = findViewById(R.id.sentence_test);
        test_id = "Test1";
        child_score=0;
        reading_child_score =0;
        flag = true;
        flag2 = true;
        move_child = false;
        //Alaa
        Test_Id=new firebase_connection();
        testIdq2=new firebase_connection();
        testIntent=new ArrayList<Intent>();
        Test_letter=unit_interface.test_letter;
        speaker_btn.setVisibility(View.INVISIBLE);
        next=findViewById(R.id.next);
        nextLabel = findViewById(R.id.nextLabel_test);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:

                word_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,70);
                sentence_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,32);
                m.setTitle_XLarge();
                Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                word_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,60);
                sentence_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,43);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                m.setTitle_Large();
                Log.i("scsize","Large" );

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                word_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                sentence_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,27);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                m.setTitle_Normal();
                Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                word_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                sentence_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                m.setTitle_Small();
                Log.i("scsize","Small" );
                break;
            default:
                word_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,35);
                sentence_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                m.setTitle_Default();

        }//end switch

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(unit_interface.Rand.size()!=0){
                    Intent nextTest=unit_interface.Rand.get(0);
                    unit_interface.Rand.remove(nextTest);
                    startActivity(nextTest);
                    finish();

                }
                else{
                    unit_interface.endtest=true;
                    unit_interface.EndTime= Calendar.getInstance().getTimeInMillis();
                    Intent intent = new Intent(ReadingTest.this, unit_interface.class);
                    intent.putExtra("unitID",unit_interface.unitID);
                    intent.putExtra("preIntent","readingTest");
                    setResult(RESULT_OK, intent);
                    System.out.println("Testttt ID: "+ test_id);
                    unit_interface.test_score(test_id);
                    startActivity(intent);
                    finish();
                }

            }
        });
        choose_phrase =  rand.nextInt(10) + 1;
        // choosen phrase is word
        if(choose_phrase <=5){
            chosen_index=  rand.nextInt(6) + 1;
            chosen_word = "word" + chosen_index;
            path = "words";
        }
        // choosen phrase is sentence
        else {
            chosen_index=  rand.nextInt(4) + 1;
            chosen_word = "sentence" + chosen_index;
            path = "sentences";
        }




        //Alaa
        Test_Id.ref.child("Tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    final String key=snapshot.getKey();
                    Log.i("KeyTest",key);
                    DatabaseReference getCurrentTestId=testIdq2.ref.child("Tests").child("test_letters");
                    ValueEventListener CurrIDEvent=new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(key!=null){
                            String lettr=snapshot.child("test_letters").getValue().toString();
                            if(lettr.equals(Test_letter)){
                            test_id=key;
                            }//Alaa

        Query query = r.ref.child("Tests").orderByKey().equalTo(test_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot test : dataSnapshot.getChildren()){
                        word = test.child(path).child(chosen_word).child("content").getValue().toString();
                        word_audio = test.child(path).child(chosen_word).child("audio_file").getValue().toString();

                        if (path.equals("sentences")){
                            sentence_test_label.setText(word);
                        }
                        else{
                            word_test_label.setText(word);
                        }



                        check_ta();
                        check_alef();

                        anim.start();
                        playAudio(audio_URLs.reading_test);

                     test_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                //this flag to prevent calling this method multiple times.
                                if(flag == false){
                                    return;
                                }
                                flag = false;
                                try {
                                    anim.stop();
                                    test_audio.reset();
                                    test_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    test_audio.setDataSource(word_audio);
                                    test_audio.prepare();

                                }catch (Exception e){

                                }


                            }
                        });
                    }
                }
                else{
                    System.out.println("Test not found !!!!!!!!!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(null, "Failed to find test.", databaseError.toException());

            }
        }); }
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

        speaker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    anim.start();
                    test_audio.start();
                    setOnCompleteListener(test_audio);
                }catch (Exception e){

                }

            }
        });


        //******* Starting speech recognition code ********

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
                Log.d("5"," onReadyForSpeech function");
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
                Log.d("6"," On Error function");
                if(isEndOfSpeech){
                    return;
                }

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
                        mSpeechRecognizer.cancel();
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                    case 9:
                        System.out.println("ERROR_INSUFFICIENT_PERMISSIONS");
                        break;

                }
                anim.start();
                playAudioInstructions(audio_URLs.not_hearing_you);
                setOnCompleteListener(feedback_audio);
            }
            @Override
            public void onResults(Bundle bundle) {

                int word_length = word.length();
                boolean found = false, found_with_repetion = false;

                // matches contains many results but we will display the best one and it useually the first one.
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                float[] scores = bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
                for (int i = 0; i < scores.length; i++) {
                    Log.d("1", "confidence scores " + scores[i]);
                }


                // find the phrase exactly
                for (int i = 0; i < matches.size(); i++) {
                    Log.d("2", "Results " + matches.get(i));
                    if (matches.get(i).compareTo(word) == 0) {
                        Log.d("2",  "Matching true!! ");
                        abjad.setBackgroundResource(R.drawable.abjad_happy);
                        anim =(AnimationDrawable) abjad.getBackground();
                        anim.start();
                        playAudioInstructions(audio_URLs.perfect_top_feedback);
                        setOnCompleteListener(feedback_audio);
                        found = true;
                        child_score = 10;
                        break;
                    }
                }
                if (choose_phrase <= 5) {
                    for (int i = 0; i < matches.size(); i++) {
                        String[] duplicates = matches.get(i).split(" ");
                        if (duplicates.length >= 2) {
                            for (int j = 0; j < duplicates.length; j++) {
                                if (duplicates[j].compareTo(word) == 0) {
                                    System.out.println("النطق صحيح مع التكرار");
                                    abjad.setBackgroundResource(R.drawable.abjad_happy);
                                    anim =(AnimationDrawable) abjad.getBackground();
                                    anim.start();
                                    playAudioInstructions(audio_URLs.perfect_top_feedback);
                                    setOnCompleteListener(feedback_audio);
                                    found_with_repetion = true;
                                    child_score = 10;
                                    break;
                                }
                            }
                        }
                        if (found_with_repetion)
                            break;
                    }
                }

                try {
                    if (found == false && found_with_repetion == false) {
                        double max_match = 0, returnValue = 0;
                        int globalCost = 0;
                        String choosenPhrase = "";


                        for (int i = 0; i < matches.size(); i++) {
                            returnValue = LevenshteinDistance.computeEditDistance(word, matches.get(i));
                            if (max_match <= returnValue) {
                                max_match = returnValue;
                                choosenPhrase = matches.get(i);
                                globalCost = LevenshteinDistance.globalCost;
                            }
                        }
                        System.out.println("choosen Phrase: " + choosenPhrase);
                        System.out.println("Max match: "+ max_match);
                        System.out.println("Global cost: "+ globalCost);
                        // The displayed phrase is word.
                        if (choose_phrase <= 5) {
                            listen_word_feedback(globalCost, word_length, max_match);
                        }
                        //The displayed phrase is sentence
                        else {
                            listen_sentence_feedback(globalCost, word_length, max_match);

                        }
                    }

                    if(reading_child_score ==0){
                     reading_child_score = child_score;
                     System.out.println("علامة الطفل : "+ child_score);
                    }
                    isEndOfSpeech = true;

                    if(child_score<=2){
                        speaker_btn.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    System.err.println("Error in onResult");
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

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
                            isEndOfSpeech = false;
                            break;
                        }
                    }
                    return false;
                }
            });
        }catch(Exception e){
            System.out.println("inside catch for mice button");
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
    public void playAudioInstructions(String url){
        try {
            feedback_audio.reset();
            feedback_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            feedback_audio.setDataSource(url);
            feedback_audio.prepare();
            feedback_audio.start();

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
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            test_audio.release();
            feedback_audio.release();
            System.out.println("onDestroy function");

        }catch (Exception e){
            System.err.println("Unable to destroy activity");
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        try{
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            test_audio.release();
            feedback_audio.release();
            anim.stop();

            System.out.println("onStop function");
        }catch (Exception e){
            System.err.println("Unable to stop activity");
        }

    }

    public void listen_word_feedback(int globalCost, int word_length, double max_match){
        if(globalCost == 1 && word_length ==3){
            anim.start();
            playAudioInstructions(audio_URLs.perfect_only_one_mistake);
            setOnCompleteListener(feedback_audio);
            child_score =8;
        }
        //very bad
        else if(globalCost >=2 && word_length == 3){
            anim.start();
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(feedback_audio);
            child_score = 1;

        }
        else if(globalCost == 1 && word_length>3){
            child_score =8;
            anim.start();
            playAudioInstructions(audio_URLs.not_fully_good);
            setOnCompleteListener(feedback_audio);
        }
        else if(max_match>=0.49 && word_length > 3){
            anim.start();
            playAudioInstructions(audio_URLs.not_fully_good);
            child_score = 5;
            setOnCompleteListener(feedback_audio);

        }
        else if(max_match<=0.49 && max_match >= 0.39 && word_length>3){
            anim.start();
            playAudioInstructions(audio_URLs.good_with_revision);
            setOnCompleteListener(feedback_audio);
            child_score =3;
        }
        else if(max_match<0.39 && word_length>3){
            anim.start();
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(feedback_audio);
            child_score=1;
        }

    }

    public void listen_sentence_feedback(int globalCost, int word_length, double max_match){

        if(globalCost==1){
            System.out.println("full score!!!!!!!");
            child_score=10;
            abjad.setBackgroundResource(R.drawable.abjad_happy);
            anim =(AnimationDrawable) abjad.getBackground();
            anim.start();
            playAudioInstructions(audio_URLs.perfect_top_feedback);
            setOnCompleteListener(feedback_audio);
        }
        else if(max_match>=0.89){
            anim.start();
            playAudioInstructions(audio_URLs.not_fully_good);
            setOnCompleteListener(feedback_audio);
            child_score =8;

        }
        else if(max_match>=0.75){
            child_score=7;
            anim.start();
            playAudioInstructions(audio_URLs.not_fully_good);
            setOnCompleteListener(feedback_audio);
        }
        else if(max_match <= 0.75 && max_match>=0.5){
            child_score=6;
            anim.start();
            playAudioInstructions(audio_URLs.not_fully_good);
            setOnCompleteListener(feedback_audio);

        }
        else if(max_match<=0.5 && max_match>=0.4){
            child_score=4;
            anim.start();
            playAudioInstructions(audio_URLs.good_with_revision);
            setOnCompleteListener(feedback_audio);
        }
        else if (max_match>=0.25){
            child_score=2;
            anim.start();
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(feedback_audio);
        }
        else if(max_match<0.25){
            child_score=1;
            anim.start();
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(feedback_audio);
        }

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
                    //move to unit interface
                    Intent intent = new Intent(ReadingTest.this, child_home.class);
                    startActivity(intent);
                }

            }

        });
        flag2 = true;

    }
    @Override
    protected void onRestart() {

        super.onRestart();
        System.out.println("onRestart function");
        feedback_audio = new MediaPlayer();
        anim.start();
        playAudioInstructions(audio_URLs.cant_continue_test);
        move_child = true;
        setOnCompleteListener(feedback_audio);
    }
}