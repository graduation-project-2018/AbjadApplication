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
import android.widget.ProgressBar;
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
    firebase_connection r;
    TextView word_test_label, sentence_test_label ;
    final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {android.Manifest.permission.RECORD_AUDIO};
    String test_id ;
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
    int reading_child_score;
    boolean flag ;
    ImageView abjad;
    AnimationDrawable anim;
    boolean flag2, move_child, finish_child_score ;
    TextView nextLabel;
    // flag to make Abjad raise his hands when child answer correctly
    boolean word_audio_flag;
    LevenshteinDistance algorithmObj = new LevenshteinDistance();
    long startTime;
    int total_score_of_prev_tests;
    ArrayList<Intent> Rand;
    ProgressBar loading_label;
    firebase_connection Test_Id,testIdq2;
    String Test_letter, unitID, first_signIn;
    int counter_of_OnErrorsCalls;

    int starttingTime =0;
    menu_variables m2 ;
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
        loading_label = findViewById(R.id.loading_label_reading_test);
        test_id = "";
        child_score=0;
        reading_child_score =0;
        flag = true;
        flag2 = true;
        move_child = false;
        finish_child_score = false;
        // this flag to prevent playing word audio in all cases, it should be played only if child can't read correctly.
        word_audio_flag = false;
        Rand = new ArrayList<Intent>();
        m2 =new menu_variables(ReadingTest.this);

        //Alaa
        Test_Id=new firebase_connection();
        testIdq2=new firebase_connection();
        speaker_btn.setVisibility(View.INVISIBLE);
        next=findViewById(R.id.next);
        nextLabel = findViewById(R.id.nextLabel_test);

        // this counter to count the number of calling OnError function and play in the
        // second call "to hear you" audio to help child record in correct way
        counter_of_OnErrorsCalls =0;


        //to get the test letter and unit ID from Unit interface.
        Intent  unitIntent =getIntent();
        Bundle letter_and_unitID = unitIntent.getExtras();
        if(letter_and_unitID !=null){
            Test_letter = letter_and_unitID.getString("test_letter");
            unitID = letter_and_unitID.getString("unitID");
            Rand = (ArrayList)letter_and_unitID.get("Rand");
            first_signIn = letter_and_unitID.getString("first_signIn");
            starttingTime =letter_and_unitID.getInt("starttingTime");
            if(letter_and_unitID.getInt("score") != 0){
                total_score_of_prev_tests = letter_and_unitID.getInt("score");
            }
        }


        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:

                word_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,70);
                sentence_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,32);
                m.setTitle_XLarge();
             //   Log.i("scsize","X Large" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                word_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,60);
                sentence_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                m.setTitle_Large();
             //   Log.i("scsize","Large" );

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                word_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                sentence_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,33);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                m.setTitle_Normal();
            //    Log.i("scsize","Normal" );
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                word_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                sentence_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                m.setTitle_Small();
            //    Log.i("scsize","Small" );
                break;
            default:
                word_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,35);
                sentence_test_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                m.setTitle_Default();
        }//end switch

        m2.t.start();

        test_audio.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer player) {
                if(!word_audio_flag)
                    return;
                // Called when the MediaPlayer is ready to play
                anim.start();
                test_audio.start();
            }
        });
try{
    feedback_audio.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
        @Override
        public void onPrepared(MediaPlayer player) {
            // Called when the MediaPlayer is ready to play

            if( child_score == 10){
                abjad.setBackgroundResource(R.drawable.abjad_happy);
                anim =(AnimationDrawable) abjad.getBackground();
            }
            anim.start();
            feedback_audio.start();
        }
    });

}catch (Exception e){

}


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), unit_interface.class);
                intent.putExtra("unitID",unitID);
                intent.putExtra("preIntent","readingTest");
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            next_test_or_go_home();
            }
        });
        choose_phrase =  rand.nextInt(100) + 1;
        // choosen phrase is word
        if(choose_phrase <=50){
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

                        playAudio_feedback(audio_URLs.reading_test);
                        loading_label.setVisibility(View.INVISIBLE);

                     feedback_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                //this flag to prevent calling this method multiple times.
                                if(flag == false){
                                    return;
                                }
                                flag = false;
                                try {
                                    anim.stop();
                                    playAudio(word_audio);

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
              //  Log.w(null, "Failed to find test.", databaseError.toException());

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

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext()); //takes context as a parameter.

        // we need intent to listen to the speech
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"edu.iau.abjad.AbjadApp");

                //set the language that we want to listen for.
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA");

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
               // Log.d("5"," onReadyForSpeech function");
            }

            @Override
            public void onBeginningOfSpeech() {
                //Log.d("5"," onBeginningOfSpeech function");
            }

            @Override
            public void onRmsChanged(float v) {
               // Log.d("4"," on onRmsChanged fuction");
            }

            @Override
            public void onBufferReceived(byte[] bytes) {

                //Log.d("4"," on Buffer Received fuction");
            }

            @Override
            public void onEndOfSpeech() {
              //  Log.d("3"," At end of speech function");

            }

            @Override
            public void onError(int i) {
              //  Log.d("6"," On Error function");
                if(isEndOfSpeech){
                    return;
                }

                counter_of_OnErrorsCalls++;

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

                if(counter_of_OnErrorsCalls == 2){
                    playAudio_feedback(audio_URLs.to_hear_you);
                }
                else{
                    playAudio_feedback(audio_URLs.not_hearing_you);
                }


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
                  //  Log.d("1", "confidence scores " + scores[i]);
                }


                // find the phrase exactly
                for (int i = 0; i < matches.size(); i++) {
                  //  Log.d("2", "Results " + matches.get(i));
                    if (matches.get(i).compareTo(word) == 0) {
                        fullScore();
                    //    Log.d("2",  "Matching true!! ");
                        found = true;
                        break;
                    }
                }
                //choosen phrase is word
                if (choose_phrase <= 5) {
                    for (int i = 0; i < matches.size(); i++) {
                        String[] duplicates = matches.get(i).split(" ");
                        if (duplicates.length >= 2) {
                            for (int j = 0; j < duplicates.length; j++) {
                                if (duplicates[j].compareTo(word) == 0) {
                                    fullScore();
                                    System.out.println("النطق صحيح مع التكرار");
                                    found_with_repetion = true;
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
                            returnValue = algorithmObj.computeEditDistance(word, matches.get(i));
                            if (max_match <= returnValue) {
                                max_match = returnValue;
                                choosenPhrase = matches.get(i);
                                globalCost = algorithmObj.globalCost;
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
                    }
                    // to use in onError function.
                    isEndOfSpeech = true;

                    // to move to the next text or go home
                    if(child_score>5){
                        finish_child_score = true;
                    }
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
            test_audio.prepareAsync();


        }
        catch (IOException e){
          //  Log.d("5","inside IOException ");
        }

        catch (IllegalArgumentException e){
          //  Log.d("5"," inside IllegalArgumentException");
        }

        catch (Exception e) {
            e.printStackTrace();
          //  Log.d("5","Inside exception");
        }
    }
    public void playAudio_feedback(String url){
        try {
            feedback_audio.reset();
            feedback_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            feedback_audio.setDataSource(url);
            feedback_audio.prepareAsync();

        }
        catch (IOException e){
           // Log.d("5","inside IOException ");
        }

        catch (IllegalArgumentException e){
           // Log.d("5"," inside IllegalArgumentException");
        }

        catch (Exception e) {
            e.printStackTrace();
          //  Log.d("5","Inside exception");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            test_audio = null;
            feedback_audio = null;
            anim.stop();
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
            feedback_audio = null;
            test_audio = null;
            anim.stop();
        }catch (Exception e){
            System.err.println("Unable to stop activity");
        }

    }

    public void listen_word_feedback(int globalCost, int word_length, double max_match){
        if(globalCost == 1 && word_length ==3){
            playAudio_feedback(audio_URLs.perfect_only_one_mistake);
            setOnCompleteListener(feedback_audio);
            child_score =8;
        }
        //very bad
        else if(globalCost >=2 && word_length == 3){
            playAudio_feedback(audio_URLs.listen_to_abjad);
            setOnCompleteListener(feedback_audio);
            child_score = 1;

        }
        else if(globalCost == 1 && word_length>3){
            child_score =8;
            playAudio_feedback(audio_URLs.not_fully_good);
            setOnCompleteListener(feedback_audio);
        }
        else if(max_match>=0.49 && word_length > 3){
            playAudio_feedback(audio_URLs.not_fully_good);
            child_score = 5;
            setOnCompleteListener(feedback_audio);

        }
        else if(max_match<=0.49 && max_match >= 0.39 && word_length>3){
            playAudio_feedback(audio_URLs.good_with_revision);
            setOnCompleteListener(feedback_audio);
            child_score =3;
        }
        else if(max_match<0.39 && word_length>3){
            playAudio_feedback(audio_URLs.listen_to_abjad);
            setOnCompleteListener(feedback_audio);
            child_score=1;
        }

    }

    public void listen_sentence_feedback(int globalCost, int word_length, double max_match){

        if(globalCost==1){
            fullScore();
            return;
        }

        if(word.equals("صنعت قلعه من الرمل") && globalCost == 2){
            fullScore();
            return;
        }
        if(word.equals("الصبار يعيش في الصحراء") && globalCost == 2){
            fullScore();
            return;
        }
        if(word.equals("انظف اسناني بالفرشاه") && globalCost == 2){
            fullScore();
            return;
        }
        if(word.equals("حلق الطائر في السماء") && globalCost == 2){
            fullScore();
            return;
        }
        else if(max_match>=0.89){
            playAudio_feedback(audio_URLs.not_fully_good);
            setOnCompleteListener(feedback_audio);
            child_score =8;
            return;

        }
        else if(max_match>=0.75){
            child_score=7;
            playAudio_feedback(audio_URLs.not_fully_good);
            setOnCompleteListener(feedback_audio);
            return;
        }
        else if(max_match <= 0.75 && max_match>=0.5){
            child_score=6;
            playAudio_feedback(audio_URLs.not_fully_good);
            setOnCompleteListener(feedback_audio);
            return;

        }
        else if(max_match<=0.5 && max_match>=0.4){
            child_score=4;
            playAudio_feedback(audio_URLs.good_with_revision);
            setOnCompleteListener(feedback_audio);
            return;
        }
        else if (max_match>=0.25){
            child_score=2;
            playAudio_feedback(audio_URLs.listen_to_abjad);
            setOnCompleteListener(feedback_audio);
            return;
        }
        else if(max_match<0.25){
            child_score=1;
            playAudio_feedback(audio_URLs.listen_to_abjad);
            setOnCompleteListener(feedback_audio);
        }

    }

    private void fullScore() {
        child_score=10;
        playAudio_feedback(audio_URLs.perfect_top_feedback);
        setOnCompleteListener(feedback_audio);
    }

    public void check_alef(){
        if(word.indexOf('أ')!= -1 || word.indexOf('إ')!= -1 || word.indexOf('آ')!= -1){
            word = word.replace('أ','ا');
            word = word.replace('آ','ا');
            word = word.replace('إ','ا');
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
        intent.putExtra("preIntent","readingTest");
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();

    }

    public void next_test_or_go_home(){
        m2.t.interrupt();
        int timeTillNow = m2.counter + starttingTime;
        if(Rand.size()!=0){
            Intent nextTest=Rand.get(0);
            nextTest.putExtra("unitID", unitID);
            nextTest.putExtra("test_letter", Test_letter);
            nextTest.putExtra("starttingTime",timeTillNow);
            // this to pass the score of this test and previous test/s "if exist" to the next test
            total_score_of_prev_tests = total_score_of_prev_tests + reading_child_score;
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
            m.total_tests_score = total_score_of_prev_tests + reading_child_score;
            Intent intent = new Intent(getApplicationContext(), unit_interface.class);
            m2.total_tests_score = total_score_of_prev_tests + reading_child_score;
            m2.endtest=true;
            m2.test_score(test_id,unitID, timeTillNow);
            intent.putExtra("unitID",unitID);
            intent.putExtra("preIntent","readingTest");
            setResult(RESULT_OK, intent);
            System.out.println("Testttt ID: "+ test_id);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), unit_interface.class);
        intent.putExtra("unitID",unitID);
        intent.putExtra("preIntent","readingTest");
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();

    }

}