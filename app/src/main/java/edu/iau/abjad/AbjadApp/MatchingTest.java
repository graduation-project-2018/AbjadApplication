package edu.iau.abjad.AbjadApp;

import android.content.ClipData;
import android.content.Context;
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
    int TestNum,counter,score ;
    DatabaseReference read;
    int[]  WordsNumber ;
    String PicDB , WordDB,TestInst;
    Random r ;
    MediaPlayer MatchingTestInst ;
    MatchingTestContent[] Checking;
    boolean correct ;

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
        Next = (ImageView) findViewById(R.id.NextTest);
        Restart = (ImageButton) findViewById(R.id.Restart);
        Content =  new ArrayList<MatchingTestContent>();
        Checking=new MatchingTestContent[3];
        rfb=new firebase_connection();
        WordDB=PicDB="";
        TestNum=1;
        r = new Random();
        WordsNumber = new int[3];
        MatchingTestInst = new MediaPlayer();
        correct=false;
        counter=0;
        TestInst = "https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D9%87%D9%84%20%D9%82%D8%B1%D8%A7%D8%A1%D8%A9%20%D8%A3%D8%A8%D8%AC%D8%AF%20%D9%84%D9%85%D8%A7%20%D8%AA%D8%B1%D8%A7%D9%87%20%D8%B9%D9%84%D9%89%20%D8%A7%D9%84%D8%B4%D8%A7%D8%B4%D8%A9%20%D8%B5%D8%AD%D9%8A%D8%AD%D8%A9%20%20%D8%A3%D9%85%20%D8%AE%D8%A7%D8%B7%D8%A6%D8%A9.mp3?alt=media&token=0be949b0-1b2f-40fa-bc9d-956227529b0f";
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


   read = rfb.ref.child("Tests").child("Test"+TestNum).child("words");



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

            playAudio(TestInst);
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

                           }
                           else {Text1.setBackgroundColor(getColor(R.color.wrong));
                               correct=false;}
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
                          playAudio("https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%B1%D8%A7%D8%A6%D8%B9%20%D8%B1%D8%A7%D8%A6%D8%B9%20%D8%A3%D8%AD%D8%B3%D9%86%D8%AA.mp3?alt=media&token=9c5b93a5-d921-4f75-832f-cc558270b810");
                          score=10;
                       }
                       else if(counter==1){
                       playAudio("https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%AC%D9%8A%D8%AF.mp3?alt=media&token=11ce846f-c8c4-49f0-a394-73ba2c869380");
                       score=3;
                       }
                       else{
                       playAudio("https://firebasestorage.googleapis.com/v0/b/abjad-a0f5e.appspot.com/o/%D8%AD%D8%A7%D9%88%D9%84%20%D9%85%D8%B1%D8%A7%D8%AC%D8%B9%D8%A9%20%D8%A7%D9%84%D8%AF%D8%B1%D9%88%D8%B3%20%D8%A7%D9%84%D8%B3%D8%A7%D8%A8%D9%82%D8%A9.mp3?alt=media&token=1518ad0b-213f-44ef-b198-9a0cc75a26f4");
                       score=0;
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

            MatchingTestInst.reset();
            MatchingTestInst.setAudioStreamType(AudioManager.STREAM_MUSIC);
            MatchingTestInst.setDataSource(url);
            MatchingTestInst.prepare();
            MatchingTestInst.start();

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
        MatchingTestInst.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MatchingTestInst.release();
    }

}
