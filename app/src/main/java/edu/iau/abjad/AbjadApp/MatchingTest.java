package edu.iau.abjad.AbjadApp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.*;


public class MatchingTest extends child_menu {
    menu_variables m = new menu_variables();
    ImageView Pic1,Pic2 ,Pic3;
    TextView Word1,Word2,Word3, Text1,Text2,Text3;
    ImageButton  Restart, Restart2, Restart3;
    ImageView Next, Next2, Next3;
    ArrayList<MatchingTestContent> Content;
    MatchingTestContent obj;
    firebase_connection rfb;
    int TestNum,counter ;
    int score;
    DatabaseReference read;
    int[]  WordsNumber ;
    String PicDB , WordDB;
    Random r ;
    MediaPlayer MatchingTest =  new MediaPlayer();
    MatchingTestContent[] Checking;
    audio_URLs voice = new audio_URLs();
    boolean correct ;
    AnimationDrawable anim;
    ImageView abjad ;
    boolean flag2,move_child ;
    String Test_letter,test_id;
    boolean test_finish;
    ProgressBar loading_label_large, loading_label_normal, loading_label_small;
    boolean full_child_score;
    String unitID;
    long startTime;
    int total_score_of_prev_tests;
    ArrayList<Intent> Rand;
    String first_signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);



        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("اختبار التوصيل");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_matching_test, null, false);

        myDrawerLayout.addView(contentView, 0);

         Pic1 = (ImageView) findViewById(R.id.Pic1);
         Pic2 = (ImageView) findViewById(R.id.Pic2);
         Pic3 = (ImageView) findViewById(R.id.Pic3);
         Word1 = (TextView) findViewById(R.id.Word1);
         Word2 = (TextView) findViewById(R.id.Word2);
         Word3 = (TextView) findViewById(R.id.Word3);
         Text1 = (TextView) findViewById(R.id.Text1);
         Text2 = (TextView) findViewById(R.id.Text2);
         Text3 = (TextView) findViewById(R.id.Text3);
         abjad = (ImageView) findViewById(R.id.abjad_matching);
        abjad.setBackgroundResource(R.drawable.abjad_speak);
        anim =(AnimationDrawable) abjad.getBackground();
        Next = findViewById(R.id.NextTest);
        Next2 = findViewById(R.id.NextTest2);
        Next3 = findViewById(R.id.NextTest3);
        Restart = findViewById(R.id.Restart);
        Restart2 = findViewById(R.id.Restart2);
        Restart3 = findViewById(R.id.Restart3);
        loading_label_large = findViewById(R.id.loading_matching_large);
        loading_label_normal = findViewById(R.id.loading_matching_normal);
        loading_label_small = findViewById(R.id.loading_matching_small);
        Content =  new ArrayList<MatchingTestContent>();
        Checking=new MatchingTestContent[3];
        rfb=new firebase_connection();
        WordDB=PicDB="";
        TestNum=3;
        r = new Random();
        WordsNumber = new int[3];
        flag2 = true;
        move_child = false;
        test_finish = false;
        correct=false;
        counter=0;
        full_child_score = false;
        Rand = new ArrayList<Intent>();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), unit_interface.class);
                intent.putExtra("unitID",unitID);
                intent.putExtra("preIntent","matchingTest");
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();
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
                Word3.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                Word2.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                Word1.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                Text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                Text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                Text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                Restart.setVisibility(View.VISIBLE);
                Next.setVisibility(View.VISIBLE);
                loading_label_large.setVisibility(View.VISIBLE);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                Word3.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                Word2.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                Word1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                Text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                Text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                Text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                Restart.setVisibility(View.VISIBLE);
                Next.setVisibility(View.VISIBLE);
                loading_label_large.setVisibility(View.VISIBLE);

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                Word3.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                Word2.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                Word1.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                Text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                Text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                Text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                Restart2.setVisibility(View.VISIBLE);
                Next2.setVisibility(View.VISIBLE);
                loading_label_normal.setVisibility(View.VISIBLE);

                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                Word3.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                Word2.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                Word1.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                Text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                Text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                Text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                Restart3.setVisibility(View.VISIBLE);
                Next3.setVisibility(View.VISIBLE);
                loading_label_small.setVisibility(View.VISIBLE);

                break;
            default:
                Word3.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                Word2.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                Word1.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                Text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                Text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                Text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                Restart2.setVisibility(View.VISIBLE);
                Next2.setVisibility(View.VISIBLE);
                loading_label_normal.setVisibility(View.VISIBLE);

        } //end

        Word1.setOnLongClickListener(longClickListener);
        Word2.setOnLongClickListener(longClickListener);
        Word3.setOnLongClickListener(longClickListener);
        Text1.setOnDragListener(dragListener1);
        Text2.setOnDragListener(dragListener2);
        Text3.setOnDragListener(dragListener3);
        Restart.setOnClickListener(RestartListener);
        Restart2.setOnClickListener(RestartListener);
        Restart3.setOnClickListener(RestartListener);
        Next.setOnClickListener(nextBtnListener);
        Next2.setOnClickListener(nextBtnListener);
        Next3.setOnClickListener(nextBtnListener);
        WordsNumber[0] = r.nextInt(7);
        while(WordsNumber[0]==0){
        WordsNumber[0] = r.nextInt(7);
    }
   WordsNumber[1]=r.nextInt(7);
    while(WordsNumber[1]==0||WordsNumber[1]==WordsNumber[0]){
        WordsNumber[1]=r.nextInt(7);
    }
    WordsNumber[2]=r.nextInt(7);
        while(WordsNumber[2]==0||WordsNumber[2]==WordsNumber[0] || WordsNumber[2]==WordsNumber[1]){
            WordsNumber[2]=r.nextInt(7);
        }

       MatchingTest.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer player) {
                if(full_child_score){
                    abjad.setBackgroundResource(R.drawable.abjad_happy);
                    anim =(AnimationDrawable) abjad.getBackground();
                }
                // Called when the MediaPlayer is ready to play
                anim.start();
                MatchingTest.start();
            }
        });



        //Alaa
        rfb.ref.child("Tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    final String key=snapshot.getKey();
                    DatabaseReference getCurrentTestId=rfb.ref.child("Tests").child("test_letters");
                    ValueEventListener CurrIDEvent=new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(key!=null){
                                String lettr=snapshot.child("test_letters").getValue().toString();
                                if(lettr.equals(Test_letter)) {
                                    test_id = key;

                                    //Alaa
   read = rfb.ref.child("Tests").child(test_id).child("words");
   read.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (int i =0 ; i<WordsNumber.length;i++){
            PicDB = (String) dataSnapshot.child("word"+WordsNumber[i]).child("pic_file").getValue();
            WordDB = (String) dataSnapshot.child("word"+WordsNumber[i]).child("content").getValue();
            obj = new MatchingTestContent(PicDB, WordDB);
            Content.add(obj);
}
            WordsNumber[0] = r.nextInt(3);
            Word1.setText(Content.get(WordsNumber[0]).Word);
            WordsNumber[1]=r.nextInt(3);
            while(WordsNumber[1]==WordsNumber[0]){
                WordsNumber[1]=r.nextInt(3);
            }
            Word2.setText(Content.get(WordsNumber[1]).Word);
            WordsNumber[2]=r.nextInt(3);
            while(WordsNumber[2]==WordsNumber[0] || WordsNumber[2]==WordsNumber[1]){
                WordsNumber[2]=r.nextInt(3);
            }
            Word3.setText(Content.get(WordsNumber[2]).Word);
            WordsNumber[0] = r.nextInt(3);
            Picasso.get().load(Content.get(WordsNumber[0]).Pic).fit().memoryPolicy(MemoryPolicy.NO_CACHE).into(Pic1);

            WordsNumber[1]=r.nextInt(3);
            while(WordsNumber[1]==WordsNumber[0]){
                WordsNumber[1]=r.nextInt(3);
            }
            Picasso.get().load(Content.get(WordsNumber[1]).Pic).fit().memoryPolicy(MemoryPolicy.NO_CACHE).into(Pic2);

            WordsNumber[2]=r.nextInt(3);
            while(WordsNumber[2]==WordsNumber[0] || WordsNumber[2]==WordsNumber[1]){
                WordsNumber[2]=r.nextInt(3);
            }
            Picasso.get().load(Content.get(WordsNumber[2]).Pic).fit().memoryPolicy(MemoryPolicy.NO_CACHE).into(Pic3);
            try{
                if(first_signIn !=null){
                    if(first_signIn.equals("110") || first_signIn.equals("100")){
                        playAudio(voice.MatchingTest_first_Inst);
                    }
                    else{
                        playAudio(voice.MatchingTestInst);
                    }
                } else{
                    playAudio(voice.MatchingTestInst);
                }

            }catch(Exception e){
                    System.out.println("Matching test catch");
            }


            loading_label_large.setVisibility(View.INVISIBLE);
            loading_label_normal.setVisibility(View.INVISIBLE);
            loading_label_small.setVisibility(View.INVISIBLE);
            setOnCompleteListener(MatchingTest);

for (int i =0 ; i<Checking.length ; i++){
    Checking[i]= new MatchingTestContent(Content.get(WordsNumber[i]).Pic,"");
}
        }

        @Override
        public void onCancelled(DatabaseError error) {
          //  Log.w(null, "Failed to read value.", error.toException());
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
    }

View.OnClickListener RestartListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Text1.setText("");
        Text1.setBackgroundColor(0xFFe6e6e6);
        Text2.setBackgroundColor(0xFFe6e6e6);
        Text3.setBackgroundColor(0xFFe6e6e6);
        Text1.setEnabled(true);
        Text2.setText("");
        Text2.setEnabled(true);
        Text3.setText("");
        Text3.setEnabled(true);
        Word1.setVisibility(View.VISIBLE);
        Word2.setVisibility(View.VISIBLE);
        Word3.setVisibility(View.VISIBLE);
        counter=0;
        correct=false;
    }
};

    View.OnClickListener nextBtnListener = new View.OnClickListener(){
        public void onClick(View view) {
            if(Text1.getText().toString().isEmpty()&& Text2.getText().toString().isEmpty()&&Text3.getText().toString().isEmpty()){

            }
            else{

                for(int j = 0 ; j<Checking.length ; j++){
                    if(Checking[0].Pic.equals(Content.get(j).Pic)){
                        if(Text1.getText().toString().equals(Content.get(j).Word)){
                            counter++;
                            correct=true;


                        }
                    }

                }
                if(correct==true){
                    Text1.setBackgroundColor(0xFF9bf5aa);
                    correct=false;
                }
                else {Text1.setBackgroundColor(0xFFf98e8e);
                }
                for(int j = 0 ; j<Checking.length ; j++){
                    if(Checking[1].Pic.equals(Content.get(j).Pic)){
                        if(Text2.getText().toString().equals(Content.get(j).Word)){
                            counter++;
                            correct=true;

                        }
                    }}
                if(correct==true){
                    Text2.setBackgroundColor(0xFF9bf5aa);
                    correct=false;
                }
                else{ Text2.setBackgroundColor(0xFFf98e8e);

                }
                for(int j = 0 ; j<Checking.length ; j++){
                    if(Checking[2].Pic.equals(Content.get(j).Pic)){
                        if(Text3.getText().toString().equals(Content.get(j).Word)){
                            counter++;
                            correct=true;

                        }
                    }

                }
                if(correct==true){
                    Text3.setBackgroundColor(0xFF9bf5aa);
                    correct=false;
                }
                else {Text3.setBackgroundColor(0xFFf98e8e);
                }

                if(counter==3){

                    score=10;
                    test_finish = true;
                    full_child_score = true;
                    playAudio(voice.perfect_top_feedback);
                    setOnCompleteListener(MatchingTest);

                }
                else if(counter==2){
                    score=7;
                    test_finish = true;
                    playAudio(voice.good_feedback);
                    setOnCompleteListener(MatchingTest);
                }
                else if(counter==1){
                    score=3;
                    test_finish = true;
                    playAudio(voice.good_feedback);
                    setOnCompleteListener(MatchingTest);
                }
                else if(counter==0){
                    score=0;
                    test_finish = true;
                    playAudio(voice.revise_previous_lessons);
                    setOnCompleteListener(MatchingTest);

                }
            }



        }
    };

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data,myShadowBuilder,view,0);
            view.setBackgroundColor(0xFFe6e6e6);
            return true;
        }
    };
View.OnDragListener dragListener1 = new View.OnDragListener() {
    @Override
    public boolean onDrag(View view, DragEvent event) {
       int dragEvent = event.getAction();
        final View v = (View) event.getLocalState();
       switch(dragEvent){
           case DragEvent.ACTION_DRAG_ENTERED:
           {
               if(v.getId() == R.id.Word1){
                   Text1.setText(Word1.getText());
               }
               else if(v.getId()== R.id.Word2){
                   Text1.setText(Word2.getText());
               }
               else if(v.getId()== R.id.Word3){
                   Text1.setText(Word3.getText());
               }
               break ;
           }
           case DragEvent.ACTION_DRAG_EXITED:
               Text1.setText("");
               break;


           case DragEvent.ACTION_DROP:
           {if(Text1.isEnabled()) {
               if (v.getId() == R.id.Word1) {
                   Text1.setText((String) Word1.getText());
                   Word1.setVisibility(View.INVISIBLE);
               } else if (v.getId() == R.id.Word2) {
                   Text1.setText((String) Word2.getText());
                   Word2.setVisibility(View.INVISIBLE);
               } else if (v.getId() == R.id.Word3) {
                   Text1.setText((String) Word3.getText());
                   Word3.setVisibility(View.INVISIBLE);
               }
           }
               Text1.setEnabled(false);
               break ;
           }
       }
        return true;
    }
};
    View.OnDragListener dragListener2 = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            int dragEvent = event.getAction();
            final View v = (View) event.getLocalState();
            switch(dragEvent){
                case DragEvent.ACTION_DRAG_ENTERED:
                {
                    if(v.getId() == R.id.Word1){
                        Text2.setText(Word1.getText());
                    }
                    else if(v.getId()== R.id.Word2){
                        Text2.setText(Word2.getText());
                    }
                    else if(v.getId()== R.id.Word3){
                        Text2.setText(Word3.getText());
                    }
                    break ;
                }
                case DragEvent.ACTION_DRAG_EXITED:
                    Text2.setText("");
                    break;

                case DragEvent.ACTION_DROP:
                {if(Text2.isEnabled()) {
                    if (v.getId() == R.id.Word1) {
                        Text2.setText((String) Word1.getText());
                        Word1.setVisibility(View.INVISIBLE);
                    } else if (v.getId() == R.id.Word2) {
                        Text2.setText((String) Word2.getText());
                        Word2.setVisibility(View.INVISIBLE);
                    } else if (v.getId() == R.id.Word3) {
                        Text2.setText((String) Word3.getText());
                        Word3.setVisibility(View.INVISIBLE);
                    }
                }
                    Text2.setEnabled(false);
                    break ;
                }
            }
            return true;
        }
    };
    View.OnDragListener dragListener3 = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            int dragEvent = event.getAction();
            final View v = (View) event.getLocalState();

            switch(dragEvent){
                case DragEvent.ACTION_DRAG_ENTERED:
                {
                    if(v.getId() == R.id.Word1){
                        Text3.setText(Word1.getText());
                    }
                    else if(v.getId()== R.id.Word2){
                        Text3.setText(Word2.getText());
                    }
                    else if(v.getId()== R.id.Word3){
                        Text3.setText(Word3.getText());
                    }
                    break ;
                }
                case DragEvent.ACTION_DRAG_EXITED:
                    Text3.setText("");
                    break;


                case DragEvent.ACTION_DROP:
                {if(Text3.isEnabled()) {
                    if (v.getId() == R.id.Word1) {
                        Text3.setText((String) Word1.getText());
                        Word1.setVisibility(View.INVISIBLE);
                    } else if (v.getId() == R.id.Word2) {
                        Text3.setText((String) Word2.getText());
                        Word2.setVisibility(View.INVISIBLE);
                    } else if (v.getId() == R.id.Word3) {
                        Text3.setText((String) Word3.getText());
                        Word3.setVisibility(View.INVISIBLE);
                    }
                }
                    Text3.setEnabled(false);
                    break ;
                }
            }
            return true;
        }
    };

    public void playAudio(String url){
        try {

            MatchingTest.reset();
            MatchingTest.setAudioStreamType(AudioManager.STREAM_MUSIC);
            MatchingTest.setDataSource(url);
            MatchingTest.prepareAsync();

        }
        catch (IOException e){
           // Log.d("5","inside IOException ");
        }

        catch (IllegalArgumentException e){
          //  Log.d("5"," inside IllegalArgumentException");
        }

        catch (Exception e) {
            e.printStackTrace();
           // Log.d("5","Inside exception");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        anim.stop();
        MatchingTest = null;
    }

    @Override
    protected void onStop() {
       try {
           super.onStop();
           MatchingTest.release();
           MatchingTest = null;
           anim.stop();

       }
    catch (Exception e){

    }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            MatchingTest.release();
            anim.stop();
        }
        catch (Exception e){

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
                // (111) means child finish all user manual(unit, lesson, and matching test) (
                if(first_signIn.equals("110")){
                    rfb.ref.child("Children").child(child_after_signin.id_child).child("first_signIn").setValue("111");
                }
                /* (101) means child finish unit and matching test user manual only, and Lesson is remaining,
                 that happen if child does not enter lesson first, but enter test first. */
                else if(first_signIn.equals("100")){
                    rfb.ref.child("Children").child(child_after_signin.id_child).child("first_signIn").setValue("101");
                }
                if(test_finish){
                    if(Rand.size()!=0){
                        Intent nextTest=Rand.get(0);
                        nextTest.putExtra("unitID", unitID);
                        nextTest.putExtra("test_letter", Test_letter);
                        nextTest.putExtra("startTime", startTime);
                        // this to pass the score of this test and previous test/s "if exist" to the next test
                        total_score_of_prev_tests = total_score_of_prev_tests + score;
                        nextTest.putExtra("score", total_score_of_prev_tests);
                        Rand.remove(nextTest);
                        nextTest.putExtra("Rand",Rand);
                        startActivity(nextTest);
                        finish();
                    }
                    else{
                        m.endtest=true;
                        m.total_tests_score = total_score_of_prev_tests + score;
                        m.EndTime= Calendar.getInstance().getTimeInMillis();
                        Intent intent = new Intent(getApplicationContext(), unit_interface.class);
                        intent.putExtra("unitID",unitID);
                        intent.putExtra("preIntent","readingTest");
                        setResult(RESULT_OK, intent);
                        System.out.println("Testttt ID: "+ test_id);
                        m.test_score(test_id, unitID, startTime);
                        startActivity(intent);
                        finish();
                    }
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
        intent.putExtra("preIntent","matchingTest");
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), unit_interface.class);
        intent.putExtra("unitID",unitID);
        intent.putExtra("preIntent","matchingTest");
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();

    }

}
