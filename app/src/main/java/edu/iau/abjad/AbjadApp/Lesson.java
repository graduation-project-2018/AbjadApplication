package edu.iau.abjad.AbjadApp;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Lesson extends child_menu {
    menu_variables m = new menu_variables();
    TextView result;
    Button mic_btn;
    SpeechRecognizer mSpeechRecognizer ;
    Intent mSpeechRecognizerIntent ;

    String lessonID;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الدرس الأول");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_lesson, null, false);

        myDrawerLayout.addView(contentView, 0);

        final firebase_connection r = new firebase_connection();


       /* r.ref.child("Lessons").push().child("sentences").child("sentence1").
                child("content").setValue("زجاج النافذة نظيف");
        r.ref.child("Lessons").child("lesson1").child("sentences").child("sentence1").
                child("audio_file").setValue("clean_window");
        r.ref.child("Lessons").child("lesson1").child("sentences").child("sentence1").
                child("pic_file").setValue("clean_window");
        r.ref.child("Lessons").child("unitID").setValue("unit1");*/

       //getting the lesson ID for the selected letter in Unit interface.
        Query query = r.ref.child("Lessons").orderByChild("lesson_letter").equalTo("م");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d( "1","Exist!!!!!");

                    for (DataSnapshot letter : dataSnapshot.getChildren()) {
                         lessonID = letter.getKey();
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

        DatabaseReference read = r.ref.child("Lessons").child(lessonID).child("Words");

        read.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot word : dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(null, "Failed to read value.", error.toException());
            }
        });














        result = (TextView) findViewById(R.id.word_label);
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
                    }

                    if(matches != null){
                        result.setText(matches.get(0));
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
                      result.setText("You will see result here");
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{ //user press the mic button
                        result.setText("");
                        result.setText("Listening...");
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
}