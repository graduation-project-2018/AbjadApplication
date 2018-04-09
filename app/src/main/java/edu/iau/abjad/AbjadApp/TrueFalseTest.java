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

   ArrayList<true_false_test_content> testContentArrayList = new ArrayList<true_false_test_content>();
    Button speaker_btn;
    Button true_btn;
    Button false_btn;
    firebase_connection r;
    TextView sentenceLabel;
    String testID;
    String selectedSentence;
    String selectedSentenceAudio;
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



        //getting the lesson ID of the selected letter in Unit interface.
        Query query = r.ref.child("Tests").orderByChild("test_letters").equalTo("م_ب");//we should pass the test letters (put the letter in a global variable + score)
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d( "1","Exist!!!!!");

                    for (DataSnapshot letter : dataSnapshot.getChildren()) {
                        testID = letter.getKey();

                        break;
                    }
                    if(testID != null){

                        DatabaseReference read = r.ref.child("Tests").child(testID).child("sentences");
                         //create a class for wrong and false test
                        read.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot sentence_ls : dataSnapshot.getChildren()){

                                    String trueContent = sentence_ls.child("content").getValue().toString();
                                    String wrongContent = sentence_ls.child("wrong_content").getValue().toString();
                                    String audio = sentence_ls.child("audio_file").getValue().toString();

                                   true_false_test_content obj = new  true_false_test_content(trueContent, wrongContent,  audio);
                                    testContentArrayList.add(obj); } //end of for loop
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
                                    String test_begin_url = "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D9%87%D9%84%20%D9%82%D8%B1%D8%A7%D8%A1%D8%A9%20%D8%A3%D8%A8%D8%AC%D8%AF%20%D9%84%D9%85%D8%A7%20%D8%AA%D8%B1%D8%A7%D9%87%20%D8%B9%D9%84%D9%89%20%D8%A7%D9%84%D8%B4%D8%A7%D8%B4%D8%A9%20%D8%B5%D8%AD%D9%8A%D8%AD%D8%A9%20%20%D8%A3%D9%85%20%D8%AE%D8%A7%D8%B7%D8%A6%D8%A9.mp3?alt=media&token=d2d662ff-4222-430a-bbe2-dc2b6e114b29";
                                    final String wrong_answer_url = "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D9%84%D9%84%D8%A3%D8%B3%D9%81%20%D9%87%D8%B0%D8%A7%20%D9%84%D9%8A%D8%B3%20%D8%A7%D9%84%D8%A7%D8%AE%D8%AA%D9%8A%D8%A7%D8%B1%20%D8%A7%D9%84%D8%B5%D8%AD%D9%8A%D8%AD.mp3?alt=media&token=b3a465fa-7afb-433e-8159-e6a2ae5bf051";
                                    final String true_answer_url = "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%B1%D8%A7%D8%A6%D8%B9%20%D8%B1%D8%A7%D8%A6%D8%B9%20%D8%A3%D8%AD%D8%B3%D9%86%D8%AA.mp3?alt=media&token=9c5b93a5-d921-4f75-832f-cc558270b810";
                                    // start the instruction audio before the lesson begin
                                    playAudio(test_begin_url);


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
                                                playAudio(wrong_answer_url);
                                                // score = 0;
                                            }
                                            else {
                                                playAudio(true_answer_url);
                                                // score = 10;

                                            }
                                           //redirect

                                        }
                                    });// end of true_btn on click listener
                                    false_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(true_or_false == 0){
                                                playAudio(true_answer_url);
                                                // score = 10;
                                            }
                                            else {
                                                playAudio(wrong_answer_url);
                                                // score = 0;

                                            }
                                            //redirect

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
                else{
                    Log.d( "1","Not Exist!!!!!");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(" ", "loadPost:onCancelled", databaseError.toException());
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

    @SuppressWarnings("serial")
    public static class childInformation implements Serializable {
        String childID;
        String childPhotoURL;//should be added in firebase
        String childFirstName;
        String childLastName;
        String childGender;
        String EducatorID;
        String userName;
        public childInformation(String cID,String eID, String fname, String lname , String gender, String photo, String uname ){
            this. childID = cID;
            this.EducatorID = eID;
            this.childFirstName = fname;
            this.childLastName = lname;
            this.childGender= gender;
            this.childPhotoURL= photo;
            this.userName =uname;
        }//end of cunstructor


    }//end of edu.iau.abjad.AbjadApp.TrueFalseTest.childInformation class
}//end of TrueFalseTest class