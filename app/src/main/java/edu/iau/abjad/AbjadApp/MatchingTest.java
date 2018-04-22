package edu.iau.abjad.AbjadApp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.*;


public class MatchingTest extends child_menu {
    menu_variables m = new menu_variables();
    ImageView Pic1,Pic2 ,Pic3;
    TextView Word1,Word2,Word3, Text1,Text2,Text3;
    ImageButton  Restart;
    ImageView Next;
    ArrayList<MatchingTestContent> Content;
    MatchingTestContent obj;
    firebase_connection rfb;
    int TestNum,counter ;
    static  int score;
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
    static String Test_letter,test_id;
    boolean test_finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Next = (ImageView) findViewById(R.id.NextTest);
        Restart = (ImageButton) findViewById(R.id.Restart);
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
        Word1.setOnLongClickListener(longClickListener);
        Word2.setOnLongClickListener(longClickListener);
        Word3.setOnLongClickListener(longClickListener);
        Text1.setOnDragListener(dragListener1);
        Text2.setOnDragListener(dragListener2);
        Text3.setOnDragListener(dragListener3);
        Restart.setOnClickListener(RestartListener);
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
//Alaa

       /* Intent test=getIntent();
        Bundle b=test.getExtras();
        if(b!=null){
            Test_letter=b.getString("test_letter");
        }*/

        Test_letter=unit_interface.test_letter;

        //Alaa
        rfb.ref.child("Tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    final String key=snapshot.getKey();
                    Log.i("KeyTest",key);
                    DatabaseReference getCurrentTestId=rfb.ref.child("Tests").child("test_letters");
                    ValueEventListener CurrIDEvent=new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(key!=null){
                                String lettr=snapshot.child("test_letters").getValue().toString();
                                Log.i("w2w2",lettr);
                                if(lettr.equals(Test_letter)) {
                                    test_id = key;
                                    Log.i("1234567", Test_letter + " " + test_id);

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
            Picasso.get().load(Content.get(WordsNumber[0]).Pic).into(Pic1);

            WordsNumber[1]=r.nextInt(3);
            while(WordsNumber[1]==WordsNumber[0]){
                WordsNumber[1]=r.nextInt(3);
            }
            Picasso.get().load(Content.get(WordsNumber[1]).Pic).into(Pic2);

            WordsNumber[2]=r.nextInt(3);
            while(WordsNumber[2]==WordsNumber[0] || WordsNumber[2]==WordsNumber[1]){
                WordsNumber[2]=r.nextInt(3);
            }
            Picasso.get().load(Content.get(WordsNumber[2]).Pic).into(Pic3);
            anim.start();
            playAudio(voice.MatchingTestInst);
            setOnCompleteListener(MatchingTest);

for (int i =0 ; i<Checking.length ; i++){

    Checking[i]= new MatchingTestContent(Content.get(WordsNumber[i]).Pic,"");
}

           Next.setOnClickListener(new View.OnClickListener() {
               @Override
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
                               Text1.setBackgroundColor(getColor(R.color.correct));
                               correct=false;
                           }
                           else {Text1.setBackgroundColor(getColor(R.color.wrong));
                               }
                       for(int j = 0 ; j<Checking.length ; j++){
                           if(Checking[1].Pic.equals(Content.get(j).Pic)){
                               if(Text2.getText().toString().equals(Content.get(j).Word)){
                                   counter++;
                                   correct=true;

                               }
                           }}
                           if(correct==true){
                               Text2.setBackgroundColor(getColor(R.color.correct));
                               correct=false;
                           }
                           else{ Text2.setBackgroundColor(getColor(R.color.wrong));

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
                           Text3.setBackgroundColor(getColor(R.color.correct));
                           correct=false;
                       }
                       else {Text3.setBackgroundColor(getColor(R.color.wrong));
                           }

                       if(counter==3){
                           abjad.setBackgroundResource(R.drawable.abjad_happy);
                           anim =(AnimationDrawable) abjad.getBackground();
                          anim.start();
                           score=10;
                           test_finish = true;
                          playAudio(voice.perfect_top_feedback);
                          setOnCompleteListener(MatchingTest);

                       }
                       else if(counter==1){
                           anim.start();
                           score=3;
                           test_finish = true;
                       playAudio(voice.good_feedback);
                       setOnCompleteListener(MatchingTest);

                       }
                       else if(counter==0){
                       anim.start();
                           score=0;
                           test_finish = true;
                       playAudio(voice.revise_previous_lessons);
                       setOnCompleteListener(MatchingTest);

                       }
               }



               }
           });

        }

        @Override
        public void onCancelled(DatabaseError error) {
            Log.w(null, "Failed to read value.", error.toException());
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
        Text1.setBackgroundColor(getColor(R.color.normal));
        Text2.setBackgroundColor(getColor(R.color.normal));
        Text3.setBackgroundColor(getColor(R.color.normal));
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

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data,myShadowBuilder,view,0);
            view.setBackgroundColor(getColor(R.color.normal));
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
            MatchingTest.prepare();
            MatchingTest.start();

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
        MatchingTest.release();
    }

    @Override
    protected void onStop() {
       try {
           super.onStop();
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
                    Intent intent = new Intent(MatchingTest.this, child_home.class);
                    startActivity(intent);
                }
                if(test_finish){
                    if(unit_interface.Rand.size()!=0){
                        Intent nextTest=unit_interface.Rand.get(0);
                        unit_interface.Rand.remove(nextTest);
                        startActivity(nextTest);
                        finish();

                    }
                    else{
                        unit_interface.endtest=true;
                        unit_interface.EndTime= Calendar.getInstance().getTimeInMillis();
                        Intent intent = new Intent(MatchingTest.this, unit_interface.class);
                        intent.putExtra("unitID",unit_interface.unitID);
                        intent.putExtra("preIntent","matchingTest");
                        setResult(RESULT_OK, intent);
                        System.out.println("Testttt ID: "+ test_id);
                        unit_interface.test_score(test_id);
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
        System.out.println("onRestart function");
       MatchingTest = new MediaPlayer();
        anim.start();
       playAudio(voice.cant_continue_test);
        move_child = true;
        setOnCompleteListener(MatchingTest);
    }

}
