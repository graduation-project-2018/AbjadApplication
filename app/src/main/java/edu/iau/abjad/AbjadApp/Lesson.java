package edu.iau.abjad.AbjadApp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Lesson extends child_menu implements MediaPlayer.OnPreparedListener {
    menu_variables m = new menu_variables();
    TextView word_label;
    TextView sentence_label;
    Button mic_btn;
    SpeechRecognizer mSpeechRecognizer ;
    Intent mSpeechRecognizerIntent ;
    Button next_lesson_btn;
    static int words_counter=0;
    String word;
    static firebase_connection r;
    ImageView lesson_pic;
    static String lessonID;
    static ArrayList <lesson_words> wordsArrayList = new ArrayList<lesson_words>();
    MediaPlayer lesson_audio = new MediaPlayer();
    MediaPlayer audio_instruction = new MediaPlayer();
    Button speaker_btn;
    boolean flag = true, flag2 = true; // to stop on Complete media listener
    audio_URLs audio_URLs = new audio_URLs();
    final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    static int child_score=0,currentScore =0;
    static String status="",childTime="";
    static long startTime, endTime;
    static int sum=0;
    static boolean incomplete = false;
    static String acTime;
    boolean isEndOfSpeech ;
    ImageView abjad;
    AnimationDrawable anim;
    MediaPlayer a1= new MediaPlayer();
    String letter ;
    ImageView score_img;
    static String unit_id;
    boolean move_child = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_lesson, null, false);
        myDrawerLayout.addView(contentView, 0);

        //to get user permission of mice
        ActivityCompat.requestPermissions(this,permissions , REQUEST_RECORD_AUDIO_PERMISSION);

        r = new firebase_connection();
        letter = "ح";
        unit_id = "unit2";

        m.title.setText(  "حرف "+"( " +letter+ " ) " );
        next_lesson_btn = (Button) findViewById(R.id.next_lesson);
        word_label = (TextView) findViewById(R.id.word_label);
        sentence_label = (TextView) findViewById(R.id.sentence_label);
        lesson_pic = (ImageView) findViewById(R.id.lesson_pic);
        words_counter =0;
        speaker_btn = (Button) findViewById (R.id.speaker_btn);
        child_score = 0;
        abjad = (ImageView) findViewById(R.id.abjad);
        abjad.setBackgroundResource(R.drawable.abjad_speak);
        anim =(AnimationDrawable) abjad.getBackground();
        score_img = (ImageView) findViewById(R.id.score_img);




        //getting the lesson ID of the selected letter in Unit interface.
        Query query = r.ref.child("Lessons").orderByChild("lesson_letter").equalTo(letter);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot letter : dataSnapshot.getChildren()) {
                         lessonID = letter.getKey();
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

                                    // start the instruction audio before the lesson begin
                                    anim.start();
                                    playAudio(audio_URLs.lesson_begin);

                                    // On complete listener that fire when the instruction audio finish to start the lesson audio.
                                    lesson_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            //this flag to prevent calling this method multiple times.
                                            if(flag == false){
                                                return;
                                            }
                                            anim.stop();
                                            flag = false;

                                         try{
                                             a1.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                             a1.setDataSource(wordsArrayList.get(words_counter).audio_file);
                                             a1.prepare();
                                             a1.start();
                                             anim.start();
                                         }catch (Exception e){

                                         }
                                            startTime = Calendar.getInstance().getTimeInMillis();

                                        }
                                    });

                                  setOnCompleteListener(a1);

                                    speaker_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try{
                                                if(words_counter ==0 ){
                                                    a1.start();
                                                    anim.start();
                                                    setOnCompleteListener(a1);

                                                }
                                                else {
                                                    anim.start();
                                                    lesson_audio.start();
                                                    setOnCompleteListener(lesson_audio);
                                                }


                                            }
                                            catch (Exception e){
                                                System.out.println("Inside catch: Unable to play audio");
                                            }
                                        }
                                    });
                                    next_lesson_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            words_counter++;
                                            score_img.setVisibility(View.INVISIBLE);

                                            if(words_counter == 4){
                                                word_label.setVisibility(View.INVISIBLE);
                                                //next_lesson_btn.setVisibility(View.INVISIBLE);
                                            }
                                            if(words_counter < 4){
                                                word = wordsArrayList.get(words_counter).content;
                                                word_label.setText(word);

                                                Picasso.get().load(wordsArrayList.get(words_counter).pic_file).into(lesson_pic);
                                                anim.start();
                                                playAudio(wordsArrayList.get(words_counter).audio_file);
                                                setOnCompleteListener(lesson_audio);
                                            }
                                            else if (words_counter > 3 && words_counter < 7){
                                                word = wordsArrayList.get(words_counter).content;
                                                sentence_label.setText(word);
                                                Picasso.get().load(wordsArrayList.get(words_counter).pic_file).into(lesson_pic);
                                                anim.start();
                                                playAudio(wordsArrayList.get(words_counter).audio_file);
                                                setOnCompleteListener(lesson_audio);

                                            }
                                            else if (words_counter == 7){
                                                // move to unit interface
                                                computeChildScore();
                                                Intent intent = new Intent(Lesson.this, unit_interface.class);
                                                startActivity(intent);
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
                    setOnCompleteListener(audio_instruction);
                }

                @Override
                public void onResults(Bundle bundle) {
                    int word_length = word.length();
                    boolean found = false, found_with_repetion=false;

                    // matches contains many results but we will display the best one and it useually the first one.
                    ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    float[] scores = bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
                    for (int i =0 ; i < scores.length ;i++){
                        Log.d( "1" ,"confidence scores " + scores[i]);
                    }



                    // find the phrase exactly
                    for(int i =0 ; i< matches.size(); i++){
                        Log.d("2", "Results " + matches.get(i));
                        if(matches.get(i).compareTo(word)== 0){
                            Log.d("2", "Matching true!! ");
                            abjad.setBackgroundResource(R.drawable.abjad_happy);
                            anim =(AnimationDrawable) abjad.getBackground();
                            anim.start();
                            playAudioInstructions(audio_URLs.perfect_top_feedback);
                            setOnCompleteListener(audio_instruction);
                            score_img.setVisibility(View.VISIBLE);
                            score_img.setImageResource(R.drawable.seven);
                            found = true;
                            child_score =7;
                            break;
                        }
                    }
                    if(sentence_label.getText()==""){
                        for(int i =0 ; i<matches.size();i++){
                            String[] duplicates= matches.get(i).split(" ");
                            if(duplicates.length>=2){
                                for(int j =0 ;j<duplicates.length; j++){
                                    if(duplicates[j].compareTo(word)==0){
                                        System.out.println("النطق صحيح مع التكرار");
                                        abjad.setBackgroundResource(R.drawable.abjad_happy);
                                        anim =(AnimationDrawable) abjad.getBackground();
                                        anim.start();
                                        playAudioInstructions(audio_URLs.perfect_top_feedback);
                                        setOnCompleteListener(audio_instruction);
                                        score_img.setVisibility(View.VISIBLE);
                                        score_img.setImageResource(R.drawable.seven);
                                        found_with_repetion = true;
                                        child_score =7;
                                        break;
                                    }
                                }
                            }
                            if(found_with_repetion)
                                break;
                        }
                    }

                    try{
                        if(found == false && found_with_repetion == false){

                            double max_match =0, returnValue=0;
                            int globalCost =0;
                            String choosenPhrase="";


                            for(int i =0 ; i<matches.size(); i++){
                                returnValue=LevenshteinDistance.computeEditDistance(word,matches.get(i));
                                if(max_match<=returnValue){
                                    max_match = returnValue;
                                    choosenPhrase= matches.get(i);
                                    globalCost=LevenshteinDistance.globalCost;
                                }
                            }
                            System.out.println("choosen Phrase: "+choosenPhrase);
                            // The displayed phrase is word.
                            if(sentence_label.getText()==""){
                               listen_word_feedback(globalCost,word_length,max_match);
                            }
                            //The displayed phrase is sentence
                            else{
                               listen_sentence_feedback(globalCost,word_length,max_match);

                            }
                        }



                        if(child_score> wordsArrayList.get(words_counter).child_score ){
                            System.out.println("Prevois score: "+  wordsArrayList.get(words_counter).child_score);
                            System.out.println("New score: "+child_score);
                            wordsArrayList.get(words_counter).child_score= child_score;
                        }
                        isEndOfSpeech = true;
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
    protected void onStop() {
        super.onStop();
        try{
            lesson_audio.release();
            audio_instruction.release();
            lesson_audio =null;
            a1.release();
            audio_instruction = null;
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            anim.stop();
            System.out.println("onStop function");
        }catch (Exception e){
            System.err.println("Unable to stop activity");
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
    public static void computeChildScore(){

        endTime = Calendar.getInstance().getTimeInMillis();
        double actualTime = endTime - startTime;
        actualTime= (actualTime/1000)/60;
        acTime = new DecimalFormat("##.##").format(actualTime);
        System.out.println("Time: "+acTime);
        for(int i =0 ; i<wordsArrayList.size();i++){
            System.out.println("Child score #"+i+": "+ wordsArrayList.get(i).child_score);
            sum=sum+wordsArrayList.get(i).child_score;
            if(wordsArrayList.get(i).child_score==0){
                incomplete= true;
            }
        }
        sum=sum/7; //get avg
        Query query =  r.ref.child("child_takes_lesson").child("childID").child(unit_id).orderByKey().equalTo(lessonID);
       query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   try{
                       DatabaseReference read_score =  r.ref.child("child_takes_lesson").child(unit_id).child("childID").child(lessonID);
                       read_score.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                                   System.out.println("Inside read");

                                   for (final DataSnapshot info: dataSnapshot.getChildren()){
                                       currentScore = Integer.valueOf(dataSnapshot.child("score").getValue().toString());
                                       status = dataSnapshot.child("status").getValue().toString();
                                       childTime = dataSnapshot.child("time").getValue().toString();
                                   }
                                   if(currentScore<sum){
                                       r.ref.child("child_takes_lesson").child("childID").child(unit_id).child(lessonID).child("score").setValue(sum);
                                   }
                                   if(Double.valueOf(childTime)>Double.valueOf(acTime)){
                                       r.ref.child("child_takes_lesson").child("childID").child(unit_id).child(lessonID).child("time").setValue(acTime);
                                   }

                                   if(incomplete==false && status != "مكتمل"){
                                       r.ref.child("child_takes_lesson").child("childID").child(unit_id).child(lessonID).child("status").setValue("مكتمل");
                                   }
                           }
                           @Override
                           public void onCancelled(DatabaseError databaseError) {
                               System.out.println("Path score not exists!!! inside on cancel function");
                           }
                       });
                   }
                   catch (Exception e){
                       System.out.println("Can't convert string to double");
                   }
               }
               else{
                   if(incomplete){
                       r.ref.child("child_takes_lesson").child("childID").child(unit_id).child(lessonID).child("status").setValue("غير مكتمل");
                   }
                   else{
                       r.ref.child("child_takes_lesson").child("childID").child(unit_id).child(lessonID).child("status").setValue("مكتمل");

                   }
                   r.ref.child("child_takes_lesson").child("childID").child(unit_id).child(lessonID).child("score").setValue(sum);
                   r.ref.child("child_takes_lesson").child("childID").child(unit_id).child(lessonID).child("time").setValue(acTime);
               }
           }
           @Override
           public void onCancelled(DatabaseError databaseError) {
           }
       });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            lesson_audio.release();
            audio_instruction.release();
            a1.release();
            lesson_audio =null;
            audio_instruction = null;
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            System.out.println("onDestroy function");

        }catch (Exception e){
            System.err.println("Unable to destroy activity");
        }
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        System.out.println("onRestart function");
        audio_instruction = new MediaPlayer();
        anim.start();
        playAudioInstructions(audio_URLs.cannot_complete);
        move_child= true;
        setOnCompleteListener(audio_instruction);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart function");
    }

    public void listen_word_feedback(int globalCost, int word_length, double max_match){
        if(globalCost == 1 && word_length ==3){
            anim.start();
            playAudioInstructions(audio_URLs.perfect_only_one_mistake);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.six);
            child_score =6;
        }
        //very bad
        else if(globalCost >=2 && word_length == 3){
            anim.start();
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.one);
            child_score = 1;

        }
        else if(globalCost == 1 && word_length>3){
            child_score =6;
            anim.start();
            playAudioInstructions(audio_URLs.excellent);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.six);
        }
        else if(max_match>=0.49 && word_length > 3){
            anim.start();
            playAudioInstructions(audio_URLs.good_feedback);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.four);
            child_score = 4;

        }
        else if(max_match<=0.49 && max_match >= 0.39 && word_length>3){
            anim.start();
            playAudioInstructions(audio_URLs.good_with_revision);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.three);
            child_score =3;
        }
        else if(max_match<0.39 && word_length>3){
            anim.start();
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.one);
            child_score=1;
        }

    }

    public void listen_sentence_feedback(int globalCost, int word_length, double max_match){

        if(globalCost==1){
            System.out.println("full score!!!!!!!");
            child_score=7;
            abjad.setBackgroundResource(R.drawable.abjad_happy);
            anim =(AnimationDrawable) abjad.getBackground();
            anim.start();
            playAudioInstructions(audio_URLs.perfect_top_feedback);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.seven);
        }
        else if(max_match>=0.89){
            anim.start();
            playAudioInstructions(audio_URLs.excellent);
            setOnCompleteListener(audio_instruction);
            child_score =6;
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.six);

        }
        else if(max_match>=0.75){
            child_score=5;
            anim.start();
            playAudioInstructions(audio_URLs.excellent);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.five);
        }
        else if(max_match <= 0.75 && max_match>=0.5){
            child_score=4;
            anim.start();
            playAudioInstructions(audio_URLs.good_feedback);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.four);

        }
        else if(max_match<=0.5 && max_match>=0.4){
            child_score=3;
            anim.start();
            playAudioInstructions(audio_URLs.good_with_revision);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.three);
        }
        else if (max_match>=0.25){
            child_score=2;
            anim.start();
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(audio_instruction);
        }
        else if(max_match<0.25){
            child_score=1;
            anim.start();
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(audio_instruction);
            score_img.setVisibility(View.VISIBLE);
            score_img.setImageResource(R.drawable.one);
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
                    Intent intent = new Intent(Lesson.this, unit_interface.class);
                    startActivity(intent);

                }

            }

        });
        flag2 = true;

    }
}