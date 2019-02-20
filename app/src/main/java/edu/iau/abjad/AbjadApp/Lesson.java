package edu.iau.abjad.AbjadApp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.util.TypedValue;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Lesson extends child_menu implements MediaPlayer.OnPreparedListener {
    menu_variables m = new menu_variables();
    TextView word_label;
    TextView sentence_label, nextLabel, prevLabel;
    Button mic_btn;
    SpeechRecognizer mSpeechRecognizer ;
    Intent mSpeechRecognizerIntent ;
    Button next_lesson_btn, prev_lesson_btn;
    int words_counter;
    String word;
    firebase_connection r;
    ImageView lesson_pic;
    String lessonID;
    ArrayList <lesson_words> wordsArrayList;
    MediaPlayer lesson_audio;
    MediaPlayer audio_instruction;
    Button speaker_btn;
    boolean flag , flag2 ; // to stop on Complete media listener
    audio_URLs audio_URLs = new audio_URLs();
    final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    int child_score ,currentScore ;
    String status,childTime;
    long startTime, endTime;
    int sum;
    boolean incomplete;
    String acTime;
    boolean isEndOfSpeech ;
    ImageView abjad;
    AnimationDrawable anim;
    MediaPlayer a1;
    String letter ;
    ImageView score_img;
    boolean move_child ;
    String choosenPhrase;
    LevenshteinDistance algorithmObj = new LevenshteinDistance();
    String unitID;
    boolean child_skip_exercise;
    ProgressBar loading_label, lesson_pic_progress_bar;
    String img_score_one, img_score_two, img_score_three, img_score_four, img_score_five, img_score_six, img_score_seven;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = findViewById(R.id.interface_title);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_lesson, null, false);
        myDrawerLayout.addView(contentView, 0);

        //to get user permission of mice
        ActivityCompat.requestPermissions(this,permissions , REQUEST_RECORD_AUDIO_PERMISSION);


        r = new firebase_connection();

        //to get the lesson letter and unit ID from Unit interface.
        Intent  unitIntent =getIntent();
        Bundle letter_and_unitID = unitIntent.getExtras();
        if(letter_and_unitID !=null){
            letter = letter_and_unitID.getString("Lessonltr");
            unitID = letter_and_unitID.getString("unitID");
        }


        //set Lesson title
        m.title.setText(  "حرف "+"( " +letter+ " ) " );

        wordsArrayList = new ArrayList<lesson_words>();
        nextLabel = findViewById(R.id.nextLabel);
        next_lesson_btn = findViewById(R.id.next_lesson);
        prev_lesson_btn = findViewById(R.id.prev_lesson);
        prevLabel = findViewById(R.id.prev_label);
        word_label = findViewById(R.id.word_label);
        sentence_label = findViewById(R.id.sentence_label);
        lesson_pic =  findViewById(R.id.lesson_pic);
        loading_label = findViewById(R.id.loading_label);
        lesson_pic_progress_bar = findViewById(R.id.lesson_pic_progress_bar);
        words_counter =0;
        speaker_btn = findViewById (R.id.speaker_btn);
        child_score = 0;
        //set animation for Abjad
        abjad = findViewById(R.id.abjad);
        abjad.setBackgroundResource(R.drawable.abjad_speak);
        anim =(AnimationDrawable) abjad.getBackground();
        
        score_img = findViewById(R.id.score_img);
        incomplete = false;
        sum=0;
        child_score=0;
        currentScore =0;
        flag = true;
        flag2 = true;
        move_child = false;
        child_skip_exercise = true;
        status="";
        childTime="";
        a1= new MediaPlayer();
        lesson_audio = new MediaPlayer();
        audio_instruction = new MediaPlayer();

        // images scores URLs in the database
        img_score_one="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%B5%D9%88%D8%B1%20%D8%AF%D8%B1%D8%AC%D8%A7%D8%AA%20%D8%A7%D9%84%D8%AF%D8%B1%D8%B3%2Fone.png?alt=media&token=73f3f3f0-0e27-49af-a120-fdb2ca0dfce6";
        img_score_two="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%B5%D9%88%D8%B1%20%D8%AF%D8%B1%D8%AC%D8%A7%D8%AA%20%D8%A7%D9%84%D8%AF%D8%B1%D8%B3%2Ftwo.png?alt=media&token=78af2e83-b27b-43b0-bb9c-b74a8c9c46bb";
        img_score_three="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%B5%D9%88%D8%B1%20%D8%AF%D8%B1%D8%AC%D8%A7%D8%AA%20%D8%A7%D9%84%D8%AF%D8%B1%D8%B3%2Fthree.png?alt=media&token=8e8e7152-b15a-4110-bd0b-e339fca7c7ae";
        img_score_four="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%B5%D9%88%D8%B1%20%D8%AF%D8%B1%D8%AC%D8%A7%D8%AA%20%D8%A7%D9%84%D8%AF%D8%B1%D8%B3%2Ffour.png?alt=media&token=79db3afc-0709-48a5-8c82-2714aeee7079";
        img_score_five="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%B5%D9%88%D8%B1%20%D8%AF%D8%B1%D8%AC%D8%A7%D8%AA%20%D8%A7%D9%84%D8%AF%D8%B1%D8%B3%2Ffive.png?alt=media&token=779beaaf-2100-4eec-b19f-2413636b83d8";
        img_score_six="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%B5%D9%88%D8%B1%20%D8%AF%D8%B1%D8%AC%D8%A7%D8%AA%20%D8%A7%D9%84%D8%AF%D8%B1%D8%B3%2Fsix.png?alt=media&token=52085ac5-3ac4-4269-8f93-2a62a1c39b62";
        img_score_seven="https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%B5%D9%88%D8%B1%20%D8%AF%D8%B1%D8%AC%D8%A7%D8%AA%20%D8%A7%D9%84%D8%AF%D8%B1%D8%B3%2Fseven.png?alt=media&token=80fc60ff-4743-479f-847a-2f689d31f01a";

        // back btn
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), unit_interface.class);
                intent.putExtra("unitID",unitID);
                intent.putExtra("preIntent","Lesson");
                setResult(RESULT_OK, intent);
                finish();
                startActivity(intent);
            }
        });

        lesson_pic_progress_bar.setVisibility(View.VISIBLE);


        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                word_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,70);
                sentence_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,47);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,32);
                prevLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,32);
                m.setTitle_XLarge();
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                word_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,60);
                sentence_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,37);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                prevLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                m.setTitle_Large();

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                word_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                sentence_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                prevLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                m.setTitle_Normal();
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                word_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                sentence_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                prevLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                m.setTitle_Small();
                break;
            default:
                word_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                sentence_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nextLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                prevLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                m.setTitle_Default();

        }//end switch


        lesson_audio.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer player) {
                // Called when the MediaPlayer is ready to play
                anim.start();
                lesson_audio.start();
            }
        });
        a1.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer player) {
                // Called when the MediaPlayer is ready to play
                anim.start();
                a1.start();
            }
        });





        //getting the lesson ID of the selected letter in Unit interface.
        Query query = r.ref.child("Lessons").orderByChild("lesson_letter").equalTo(letter);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot letter : dataSnapshot.getChildren()) {
                         lessonID = letter.getKey();}
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
                                    // Add to the arrayList
                                    wordsArrayList.add(obj);
                                    word = wordsArrayList.get(words_counter).content;
                                    word_label.setText(word);
                                    //word = "هذه طائرة ورقية";
                                    //sentence_label.setText(word);

                                    check_alef();
                                    check_ta();
                                    Picasso.get().load(wordsArrayList.get(words_counter).pic_file).fit().
                                            memoryPolicy(MemoryPolicy.NO_CACHE).into(lesson_pic,new Callback() {
                                        @Override
                                        public void onSuccess(){
                                           lesson_pic_progress_bar.setVisibility(View.INVISIBLE);
                                        }
                                        @Override
                                        public void onError(Exception e) {
                                        }
                                    });


                                    // start the instruction audio before the lesson begin
                                    playAudio(audio_URLs.lesson_begin);
                                    loading_label.setVisibility(View.INVISIBLE);

                                    try{
                                        // On complete listener that fire when the instruction audio finish to start the lesson audio.
                                        lesson_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mediaPlayer) {
                                                //this flag to prevent calling this method multiple times.
                                                if(flag == false)
                                                    return;

                                                anim.stop();
                                                flag = false;

                                                try{
                                                    a1.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                    a1.setDataSource(wordsArrayList.get(words_counter).audio_file);
                                                    a1.prepareAsync();
                                                }catch (Exception e){

                                                }
                                                startTime = Calendar.getInstance().getTimeInMillis();

                                            }
                                        });
                                     }catch(Exception e){

                                    }

                                    try{
                                        setOnCompleteListener(a1);
                                    }catch (Exception e){

                                    }


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



                                    //next button listener
                                    next_lesson_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            words_counter++;
                                       move_to_word_listener();
                                            prev_lesson_btn.setVisibility(View.VISIBLE);
                                            prevLabel.setVisibility(View.VISIBLE);
                                            lesson_pic_progress_bar.setVisibility(View.VISIBLE);
                                        }
                                    });

                                    nextLabel.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View view) {
                                            words_counter++;
                                            move_to_word_listener();
                                            prev_lesson_btn.setVisibility(View.VISIBLE);
                                            prevLabel.setVisibility(View.VISIBLE);
                                            lesson_pic_progress_bar.setVisibility(View.VISIBLE);
                                        }
                                    });

                                    //previous button listener
                                    prev_lesson_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            words_counter--;
                                            move_to_word_listener();
                                            lesson_pic_progress_bar.setVisibility(View.VISIBLE);
                                            if(words_counter ==0 ){
                                                prev_lesson_btn.setVisibility(View.INVISIBLE);
                                                prevLabel.setVisibility(View.INVISIBLE);
                                            }
                                            else{
                                                prev_lesson_btn.setVisibility(View.VISIBLE);
                                                prevLabel.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });

                                   prevLabel.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View view) {
                                            words_counter--;
                                            move_to_word_listener();
                                            lesson_pic_progress_bar.setVisibility(View.VISIBLE);
                                            if(words_counter ==0 ){
                                                prev_lesson_btn.setVisibility(View.INVISIBLE);
                                                prevLabel.setVisibility(View.INVISIBLE);
                                            }
                                            else{
                                                prev_lesson_btn.setVisibility(View.VISIBLE);
                                                prevLabel.setVisibility(View.VISIBLE);
                                            }
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
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                            }
                        });
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


      // ******* Starting speech recognition code ********
            mic_btn = (Button) findViewById(R.id.mic_btn);
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this); //takes context as a parameter.

            // we need intent to listen to the speech
            mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            //set the language that we want to listen for (Arabic)
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA");

            mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {
                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {
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

                    // play 'not hearing you' audio
                    try{
                        a1.reset();
                        a1.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        a1.setDataSource(audio_URLs.not_hearing_you);
                        a1.prepareAsync();
                        setOnCompleteListener(a1);

                    }catch(Exception e){

                    }

                }

                @Override
                public void onResults(Bundle bundle) {
                    int word_length = word.length();
                    boolean found = false, found_with_repetition=false;

                    // matches contains many results but we will display the best one and it useually the first one.
                    ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    float[] scores = bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
                    for (int i =0 ; i < scores.length ;i++){
                       // Log.d( "1" ,"confidence scores " + scores[i]);
                    }



                    // find the phrase exactly
                    for(int i =0 ; i< matches.size(); i++){
                       // Log.d("2", "Results " + matches.get(i));
                        if(matches.get(i).compareTo(word)== 0){
                            Log.d("2", "Matching true!! ");
                            fullScore();
                            found = true;
                            break;
                        }
                    }
                    //check if there is a repetition in words only.
                    if(sentence_label.getText()==""){
                        for(int i =0 ; i<matches.size();i++){
                            String[] duplicates= matches.get(i).split(" ");
                            if(duplicates.length>=2){
                                for(int j =0 ;j<duplicates.length; j++){
                                    if(duplicates[j].compareTo(word)==0){
                                        System.out.println("النطق صحيح مع التكرار");
                                        fullScore();
                                        found_with_repetition = true;
                                        break;}
                                }}
                            if(found_with_repetition)
                                break;}}


                    try{
                        if(found == false && found_with_repetition == false){

                            double max_match =0, returnValue=0;
                            int globalCost =0;
                            choosenPhrase="";


                            for(int i =0 ; i<matches.size(); i++){
                                returnValue=algorithmObj.computeEditDistance(word,matches.get(i));
                                if(max_match<=returnValue){
                                    max_match = returnValue;
                                    choosenPhrase= matches.get(i);
                                    globalCost=algorithmObj.globalCost;
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

                        if(words_counter == 6){
                            computeChildScore();
                            child_skip_exercise = false;
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
        try{
            switch (requestCode){
                case REQUEST_RECORD_AUDIO_PERMISSION:
                    permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    break;
            }
            if (!permissionToRecordAccepted ) finish();
        }catch (Exception e){

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
            lesson_audio.prepareAsync();
        }
        catch (IOException e){
           // Log.d("5","inside IOException ");
        }
        catch (IllegalArgumentException e){
          //  Log.d("5"," inside IllegalArgumentException");
        }
        catch (Exception e) {
            e.printStackTrace();
          //  Log.d("5","Inside exception");
        }
    }
    public void playAudioInstructions(String url){
        try {
            audio_instruction.reset();
           audio_instruction.setAudioStreamType(AudioManager.STREAM_MUSIC);
           audio_instruction.setDataSource(url);
           audio_instruction.prepareAsync();

        }
        catch (IOException e){
           // Log.d("5","inside IOException ");
        }
        catch (IllegalArgumentException e){
           // Log.d("5"," inside IllegalArgumentException");
        }
        catch (Exception e) {
            e.printStackTrace();
           // Log.d("5","Inside exception");
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        try{
            lesson_audio.release();
            audio_instruction.release();
            a1.release();
            lesson_audio =null;
            audio_instruction = null;
            a1= null;
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            anim.stop();
        }catch (Exception e){
        }

    }

  /*  @Override
    protected void onPause() {
        super.onPause();
        try{
            lesson_audio.release();
            audio_instruction.release();
            a1.release();
            //mSpeechRecognizer.cancel();
           // mSpeechRecognizer.destroy();
            anim.stop();
        }catch (Exception e){
        }

    }*/
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
    public void computeChildScore(){

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
        System.out.println("Sum is: "+ sum);
        System.out.println("Unit id is "+ unitID);
        System.out.println("lesson id is "+ lessonID);
        Query query =  r.ref.child("child_takes_lesson").child(child_after_signin.id_child).child(unitID).orderByKey().equalTo(lessonID);
       query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){

                   try{
                       DatabaseReference read_score =  r.ref.child("child_takes_lesson").child(child_after_signin.id_child).child(unitID).child(lessonID);
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
                                       r.ref.child("child_takes_lesson").child(child_after_signin.id_child).child(unitID).child(lessonID).child("score").setValue(sum);
                                       r.ref.child("child_takes_lesson").child(child_after_signin.id_child).child(unitID).child(lessonID).child("time").setValue(acTime);

                                   }
                                   if(incomplete==false && status != "مكتمل"){
                                       r.ref.child("child_takes_lesson").child(child_after_signin.id_child).child(unitID).child(lessonID).child("status").setValue("مكتمل");
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
                       r.ref.child("child_takes_lesson").child(child_after_signin.id_child).child(unitID).child(lessonID).child("status").setValue("غير مكتمل");
                   }
                   else{
                       r.ref.child("child_takes_lesson").child(child_after_signin.id_child).child(unitID).child(lessonID).child("status").setValue("مكتمل");

                   }
                   System.out.println("Sum total: "+ sum);
                   r.ref.child("child_takes_lesson").child(child_after_signin.id_child).child(unitID).child(lessonID).child("score").setValue(sum);
                   r.ref.child("child_takes_lesson").child(child_after_signin.id_child).child(unitID).child(lessonID).child("time").setValue(acTime);
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
            anim.stop();
            lesson_audio =null;
            audio_instruction = null;
            a1= null;
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
        //Move to unit interface when child close the app while test or lesson
        Intent intent = new Intent(getApplicationContext(), unit_interface.class);
        intent.putExtra("unitID",unitID);
        intent.putExtra("preIntent","Lesson");
        setResult(RESULT_OK, intent);
        finish();
        startActivity(intent);



    }

    public void listen_word_feedback(int globalCost, int word_length, double max_match){
        if(word.equals("لعبت")){
            if(choosenPhrase.startsWith("لعب") || choosenPhrase.endsWith("ات")){
                fullScore();
                return;
            }
        }

        if(word.equals("قرا")){
            if(choosenPhrase.startsWith("قرا")){
                fullScore();
                return;
            }
        }
        if(word.equals("وعاء") && globalCost == 1){
            fullScore();
            return;

        }
        if(globalCost == 1){
            playAudioInstructions(audio_URLs.perfect_only_one_mistake);
            setOnCompleteListener(audio_instruction);
            onPreparedListener(img_score_six);
            child_score =6;
        }
        //very bad
        else if(globalCost >=2 && word_length == 3){
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(audio_instruction);
            onPreparedListener(img_score_one);
            child_score = 1;
        }

        else if(max_match>=0.49 && word_length > 3){
            playAudioInstructions(audio_URLs.not_fully_good);
            setOnCompleteListener(audio_instruction);
            onPreparedListener(img_score_four);
            child_score = 4;

        }

        else if(max_match<=0.49 && max_match >= 0.39 && word_length>3){
            playAudioInstructions(audio_URLs.good_with_revision);
            setOnCompleteListener(audio_instruction);
            onPreparedListener(img_score_three);
            child_score =3;
        }
        else if(max_match<0.39 && word_length>3){
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(audio_instruction);
            onPreparedListener(img_score_one);
            child_score=1;
        }

    }

    public void listen_sentence_feedback(int globalCost, int word_length, double max_match){

        if(globalCost==1){
            fullScore();
            return;
        }
        if(globalCost == 2 && word.equals("المطر غزير")){
         fullScore();
         return;
        }
        if( word.equals("ذهب مهند ليغسل يديه")){
            if(choosenPhrase.startsWith("ذهب مهند") && choosenPhrase.endsWith("يغسل يديه")){
             fullScore();
             return;
            }
        }

        if(word.equals("انا نجود مدينتي الرياض")){
            if(choosenPhrase.startsWith("انا نجود مدين") && choosenPhrase.endsWith("الرياض")){
                fullScore();
                return;
            }
        }


        if(word.equals("فرح الفريق بالفوز") && globalCost == 2){
            fullScore();
            return;
        }

        else if(max_match>=0.89){
            playAudioInstructions(audio_URLs.not_fully_good);
            setOnCompleteListener(audio_instruction);
            child_score =6;
            onPreparedListener(img_score_six);
            return;

        }
        else if(max_match>=0.75){
            child_score=5;
            playAudioInstructions(audio_URLs.not_fully_good);
            setOnCompleteListener(audio_instruction);
            onPreparedListener(img_score_five);
            return;
        }
        else if(max_match <= 0.75 && max_match>=0.5){
            child_score=4;
            playAudioInstructions(audio_URLs.not_fully_good);
            setOnCompleteListener(audio_instruction);
            onPreparedListener(img_score_four);
            return;

        }
        else if(max_match<=0.5 && max_match>=0.4){
            child_score=3;
            playAudioInstructions(audio_URLs.good_with_revision);
            setOnCompleteListener(audio_instruction);
            onPreparedListener(img_score_three);
            return;
        }
        else if (max_match>=0.25){
            child_score=2;
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(audio_instruction);
            onPreparedListener(img_score_two);
            return;
        }
        else if(max_match<0.25){
            child_score=1;
            playAudioInstructions(audio_URLs.listen_to_abjad);
            setOnCompleteListener(audio_instruction);
            onPreparedListener(img_score_one);
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

            }

        });
        flag2 = true;
    }
    public void fullScore(){
        child_score=7;
        playAudioInstructions(audio_URLs.perfect_top_feedback);
        setOnCompleteListener(audio_instruction);
        onPreparedListener(img_score_seven);
    }

    // this function to move to next or previous word based on the button clicked
    public void move_to_word_listener(){
        audio_instruction.stop();
        abjad.setBackgroundResource(R.drawable.abjad_speak);
        anim =(AnimationDrawable) abjad.getBackground();
        score_img.setVisibility(View.INVISIBLE);
        if(words_counter == 4){
            word_label.setVisibility(View.INVISIBLE);
        }
        if(words_counter < 4){
            word = wordsArrayList.get(words_counter).content;
            word_label.setVisibility(View.VISIBLE);
            sentence_label.setVisibility(View.INVISIBLE);
            word_label.setText(word);
            Picasso.get().load(wordsArrayList.get(words_counter).pic_file).fit().memoryPolicy(MemoryPolicy.NO_CACHE).into(lesson_pic,new Callback() {
                @Override
                public void onSuccess(){
                    lesson_pic_progress_bar.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onError(Exception e) {
                }
            });

            playAudio(wordsArrayList.get(words_counter).audio_file);
            setOnCompleteListener(lesson_audio);
        }
        else if (words_counter > 3 && words_counter < 7){
            word_label.setVisibility(View.INVISIBLE);
            sentence_label.setVisibility(View.VISIBLE);
            word = wordsArrayList.get(words_counter).content;
            sentence_label.setText(word);
            Picasso.get().load(wordsArrayList.get(words_counter).pic_file).fit().memoryPolicy(MemoryPolicy.NO_CACHE).into(lesson_pic,new Callback() {
                @Override
                public void onSuccess(){
                    lesson_pic_progress_bar.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onError(Exception e) {
                }
            });

            playAudio(wordsArrayList.get(words_counter).audio_file);
            setOnCompleteListener(lesson_audio);
        }
        else if (words_counter == 7){
            // move to unit interface
            if(child_skip_exercise){
                computeChildScore();
            }
            Intent intent = new Intent(getApplicationContext(), unit_interface.class);
            intent.putExtra("unitID",unitID);
            intent.putExtra("preIntent","Lesson");
            setResult(RESULT_OK, intent);
            finish();
            startActivity(intent);

        }
        check_alef();
        check_ta();
    }

    public void onPreparedListener(final String image){
        audio_instruction.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer player) {
                // Called when the MediaPlayer is ready to play
                if(child_score == 7){
                    abjad.setBackgroundResource(R.drawable.abjad_happy);
                    anim =(AnimationDrawable) abjad.getBackground();
                }
                anim.start();
                audio_instruction.start();
                    score_img.setVisibility(View.VISIBLE);
                 Picasso.get().load(image).fit().into(score_img);


            }
        });
    }









}// end class