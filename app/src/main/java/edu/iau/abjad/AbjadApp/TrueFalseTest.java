package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class TrueFalseTest extends child_menu implements MediaPlayer.OnPreparedListener  {
    menu_variables m = new menu_variables();
    MediaPlayer test_sentence_audio = new MediaPlayer();
    audio_URLs audio_obj = new audio_URLs();
   ArrayList<true_false_test_content> testContentArrayList = new ArrayList<true_false_test_content>();
    Button speaker_btn;
    Button true_btn;
    Button false_btn;
    firebase_connection r;
    TextView sentenceLabel;
    String testID;
    //previous_Intent = getIntent();
    String selectedSentence;
    String selectedSentenceAudio;
    static int true_false_test_score;
    boolean flag = true;
    int true_or_false;
    int sentence_number;
    private  static CountDownTimer countDownTimer;

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


                        //int test_ID = previous_Intent.getStringExtra("test_ID");
                        DatabaseReference read = r.ref.child("Tests").child("Test1").child("sentences");
                         //create a class for wrong and false test
                        read.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot sentence_ls : dataSnapshot.getChildren()){

                                    String trueContent = sentence_ls.child("content").getValue().toString();
                                    String wrongContent = sentence_ls.child("wrong_content").getValue().toString();
                                    String audio = sentence_ls.child("audio_file").getValue().toString();

                                   true_false_test_content obj = new  true_false_test_content(trueContent, wrongContent,  audio);
                                    testContentArrayList.add(obj);
                                } //end of for loop
                                    Random rand = new Random();
                                    true_or_false = rand.nextInt(2);
                                   sentence_number = rand.nextInt(4);

                                   if(true_or_false == 1)
                                   { selectedSentence = testContentArrayList.get( sentence_number).trueContent;

                                   }
                                    else{
                                       selectedSentence = testContentArrayList.get( sentence_number).wrongContent;

                                   }
                                    sentenceLabel.setText(selectedSentence);
                                   // start the instruction audio before the lesson begin
                                    playAudio(audio_obj.true_false_test_begin_url);


                                    // On complete listener that fire when the instruction audio finish to start the lesson audio.
                                    test_sentence_audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            if(flag == false){
                                                return;
                                            }
                                            flag = false;
                                            playAudio(testContentArrayList.get( sentence_number).audioUrl);

                                        }
                                    });

                                    speaker_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            selectedSentenceAudio = testContentArrayList.get( sentence_number).audioUrl;
                                            playAudio(selectedSentenceAudio);
                                        }
                                    });
                                   true_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(true_or_false == 0){
                                                playAudio(audio_obj.wrong_answer_url);
                                                true_false_test_score = 0;
                                            }
                                            else {
                                                playAudio(audio_obj.perfect_top_feedback);
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
                                                playAudio(audio_obj.perfect_top_feedback);
                                                true_false_test_score = 10;

                                            }
                                            else {
                                                playAudio(audio_obj.wrong_answer_url);
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
    }


}//end of TrueFalseTest class