package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class unit_interface extends child_menu {
    menu_variables m = new menu_variables();
    private Button test1,test2,test3,lesson1,lesson2,lesson3,lesson4,lesson5,lesson6;
    private ImageView lock1,lock2,lock3,lock4,lock5,lock6,test1Stars,test2Stars,test3Stars,
            lesson1Stars,lesson2Stars,lesson3Stars,lesson4Stars,lesson5Stars,lesson6Stars ;
    private firebase_connection unitConnicetion;
    private String unitID;
    private Bundle chilHomeIntent;
    private ArrayList<String> lessons;
    private ArrayList <Button> blessons;
    private ArrayList <Button> btests;
    private Random randomTestNo;
    private ArrayList<Intent> testIntent;
    private ArrayList <Intent>rTest=new ArrayList<Intent>();
    private Intent MatchingTest,ReadingTest,TrueFalseTest,HeardWordTest;
    private  String childID;
    private ArrayList<String> TestStringForTesting;
    private ArrayList<String> TestStringForTesting2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("مدرستي");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_unit_interface, null, false);
        //initilization
        unitConnicetion= new firebase_connection();
        chilHomeIntent=getIntent().getExtras();
        unitID="unit2";
                // chilHomeIntent.getString("unitId");
        lessons=new ArrayList<String>();
        blessons=new ArrayList<Button>();
        btests=new ArrayList<Button>();
        TestStringForTesting=new ArrayList<String>();
        TestStringForTesting2=new ArrayList<String>();
        randomTestNo=new Random();
        testIntent=new ArrayList<Intent>();
        MatchingTest=  new Intent(this, MatchingTest.class );
        ReadingTest=   new Intent(this, ReadingTest.class );
        TrueFalseTest= new  Intent(this, TrueFalseTest.class );
        HeardWordTest= new Intent(this, HeardWordTest.class );
        testIntent.add(MatchingTest);
        TestStringForTesting.add("MatchingTest");
        testIntent.add(ReadingTest);
        TestStringForTesting.add("ReadingTest");
        testIntent.add(TrueFalseTest);
        TestStringForTesting.add("TrueFalseTest");
        testIntent.add(HeardWordTest);
        TestStringForTesting.add("HeardWordTest");
        test1=findViewById(R.id.test1);
        test2=findViewById(R.id.test2);
        test3=findViewById(R.id.test3);
        lesson1=findViewById(R.id.lesson1);
        lesson2=findViewById(R.id.lesson2);
        lesson3=findViewById(R.id.lesson3);
        lesson4=findViewById(R.id.lesson4);
        lesson5=findViewById(R.id.lesson5);
        lesson6=findViewById(R.id.lesson6);
        lock1=findViewById(R.id.lock1);
        lock2=findViewById(R.id.lock2);
        lock3=findViewById(R.id.lock3);
        lock4=findViewById(R.id.lock4);
        lock5=findViewById(R.id.lock5);
        lock6=findViewById(R.id.lock6);
        test1Stars=findViewById(R.id.test1Stars);
        test2Stars=findViewById(R.id.test2Stars);
        test3Stars=findViewById(R.id.test3Stars);
        lesson1Stars=findViewById(R.id.lesson1Stars);
        lesson2Stars=findViewById(R.id.lesson2Stars);
        lesson3Stars=findViewById(R.id.lesson3Stars);
        lesson4Stars=findViewById(R.id.lesson4Stars);
        lesson5Stars=findViewById(R.id.lesson5Stars);
        lesson6Stars=findViewById(R.id.lesson6Stars);
        myDrawerLayout.addView(contentView, 0);
        blessons.add(lesson1);
        blessons.add(lesson2);
        blessons.add(lesson3);
        blessons.add(lesson4);
        blessons.add(lesson5);
        blessons.add(lesson6);
        btests.add(test1);
        btests.add(test2);
        btests.add(test3);
        fillUnitContent(blessons,btests,lessons);
        fillTest();


    }
    private void fillUnitContent(final ArrayList <Button> blessons, final ArrayList <Button> btests, final ArrayList<String> lessons){
        unitConnicetion.ref.child("Units").child(unitID).child("unit letters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot s: dataSnapshot.getChildren()){
                    int i=0;
                    lessons.add(s.getValue().toString());
                    Log.i("LesonsLetter","Hi"+lessons.get(i));
                    i++;

                }
                Log.i("LesonsLetter",""+lessons.size());
                if(lessons.size()!=0){
                    for (int i=0;i<6;i++){
                        blessons.get(i).setText(lessons.get(i));
                    }
                    for (int i=0;i<3;i++){
                        int j=i;
                        btests.get(i).setText(lessons.get(j)+","+lessons.get(j+1));
                        j+=2;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setScore(){

        unitConnicetion.ref.child("child_takes_lesson").child(childID);
    }

    private void fillTest() {
        int  random;
        int i=0;
        int []  random2=new int[4];
        random2[0]=randomTestNo.nextInt(4);
        boolean end=true;
      while(end){
          random=randomTestNo.nextInt(4);
          if (random==random2[i]){
              random=randomTestNo.nextInt(4);
              System.out.print(random+" if rand repeted ");
          }else{
              i++;
              random2[i]=random;
              System.out.print(random+" if rand not repeted ");

          }
          if (random2.length==4){
              end=false;
          }
      }
        for(int j=0;j<testIntent.size();j++){

            rTest.add(testIntent.get(random2[j]));
            TestStringForTesting2.add(TestStringForTesting.get(random2[j]));
            Log.i("testRandom",TestStringForTesting2.get(j));

        }
    }
}
