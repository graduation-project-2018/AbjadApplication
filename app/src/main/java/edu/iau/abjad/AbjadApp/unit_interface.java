package edu.iau.abjad.AbjadApp;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.security.spec.ECField;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
public class unit_interface extends child_menu {
    menu_variables m = new menu_variables();
    private Button test1,test2,test3,lesson1,lesson2,lesson3,lesson4,lesson5,lesson6;
    private ImageView lock2,lock3,lock4,lock5,lock6,test1Stars,test2Stars,test3Stars,
            lesson1Stars,lesson2Stars,lesson3Stars,lesson4Stars,lesson5Stars,lesson6Stars,bal1,bal2,bal3;
    private static firebase_connection unitConnicetion,getscore,TestId,
            childScoreConnection,childLockConnection,getChildScoreConnection,innerScore,testScoreq,testIDq,testIDq2,
            testgetSq1,testgetSq2;
    private Intent chilHomeIntent,lessonIntent;
    private Random randomTestNo;
    private ArrayList <Intent> testIntent;
    private Intent matchingTest_Intent,readingTest_Intent,trueFalseTest_Intent,heardWordTest_Intent;
    private ArrayList<String> TestStringForTesting;
    private ArrayList<String> TestStringForTesting2;
    private ArrayList<String> childLessons,childTests;
    private ArrayList<String> openLessons;
    private ArrayList<String> lessons;
    private ArrayList<String> lessonsScore;
    private audio_URLs audio;
    public static ArrayList<Intent>  Rand;
    private ArrayList<childUnitInfo> lessonsInfo,testInfo;
    private MediaPlayer instructions;
    String un;
    public  static String unitID;
    boolean flag = true;
    private  String childID;
    private int random,random2,rand;
    static boolean endtest=false;
    static int finalScore;
    static long startTime,EndTime;
    static String test_letter;
    static String  childTime;
    static int currentScore ;
    static firebase_connection r = new firebase_connection();
    static String actual_time;
    private String unitName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = findViewById(R.id.interface_title);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_unit_interface, null, false);
        myDrawerLayout.addView(contentView, 0);
        //initilization
        childID = Signin.id_child;
        lessons=new ArrayList<String>();
        childTests=new ArrayList<String>();
        innerScore=new firebase_connection();
        testIDq=new firebase_connection();
        audio=new audio_URLs();
        TestId=new firebase_connection();
        setLessons(lessons);
        getChildScoreConnection=new firebase_connection();
        testIDq2=new firebase_connection();
        lessonsScore=new ArrayList<String>();
        unitConnicetion= new firebase_connection();
        getscore=new firebase_connection();
        childScoreConnection=new firebase_connection();
        childLockConnection=new firebase_connection();
        testScoreq=new firebase_connection();
        testgetSq1=new firebase_connection();
        testgetSq2=new firebase_connection();
        openLessons=new ArrayList<String>();
        childLessons=new ArrayList<String>();
        Rand=new ArrayList<Intent>();
        TestStringForTesting=new ArrayList<String>();
        TestStringForTesting2=new ArrayList<String>();
        randomTestNo=new Random();
        lessonIntent=new Intent(this,Lesson.class);
        testIntent=new ArrayList<Intent>();
        lessonsInfo=new ArrayList<childUnitInfo>();
        bal1=findViewById(R.id.ballon1);
        bal2=findViewById(R.id.ballon2);
        bal3=findViewById(R.id.ballon3);

        Bundle child=getIntent().getExtras();
        m.title.setText(child.getString("Unitname"));

        testInfo=new ArrayList<childUnitInfo>();
        matchingTest_Intent=  new Intent(this, MatchingTest.class );
        readingTest_Intent=   new Intent(this, ReadingTest.class );
        trueFalseTest_Intent= new  Intent(this, TrueFalseTest.class );
        heardWordTest_Intent= new Intent(this, HeardWordTest.class );
        testIntent.add(matchingTest_Intent);
        TestStringForTesting.add("MatchingTest");
        testIntent.add(readingTest_Intent);
        TestStringForTesting.add("ReadingTest");
        testIntent.add(trueFalseTest_Intent);
        TestStringForTesting.add("TrueFalseTest");
        testIntent.add(heardWordTest_Intent);
        TestStringForTesting.add("HeardWordTest");
        test1= findViewById(R.id.test1);
        test2= findViewById(R.id.test2);
        test3= findViewById(R.id.test3);
        lesson1= findViewById(R.id.lesson1);
        lesson2= findViewById(R.id.lesson2);
        lesson3= findViewById(R.id.lesson3);
        lesson4= findViewById(R.id.lesson4);
        lesson5= findViewById(R.id.lesson5);
        lesson6= findViewById(R.id.lesson6);
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
        instructions=new MediaPlayer();
       // Log.i("dsdjcgjsd",unitID);
       Bundle intent=getIntent().getExtras();
       if(intent.getString("preIntent").equals("Lesson")){
           this.unitID=intent.getString("unitID");
       }
       else if(intent.getString("preIntent").equals("heardTest")){
           this.unitID=intent.getString("unitID");

       }
       else if(intent.getString("preIntent").equals("trueFalse")){
           this.unitID=intent.getString("unitID");

       }
       else if(intent.getString("preIntent").equals("matchingTest")){
           this.unitID=intent.getString("unitID");

       }
       else if(intent.getString("preIntent").equals("readingTest")){
           this.unitID=intent.getString("unitID");
       }else if(intent.getString("preIntent").equals("childHome")){
           Log.i("ifStm","Iam here");
           this.unitID=intent.getString("id");
           Log.i("Unitid",this.unitID);
       }else{
           Log.i("ifStm","noone");
       }
       if(unitID.equals("unit1") && intent.getString("preIntent").equals("childHome")){
            playAudio(audio.unit_Tip_One);
            instructions.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //this flag to prevent calling this method multiple times.
                    if(flag == false){
                        return;
                    }
                    flag = false;
                    playAudio(audio.unit_Tip_Two);

                }
            });

        }
        final MediaPlayer aduio_CannotStartLesson=new MediaPlayer();
        try {
            aduio_CannotStartLesson.setDataSource(audio.unit_Tip_three);
            aduio_CannotStartLesson.prepare();
            Log.i("iComeHere","lol");

        } catch (IOException e) {
            e.printStackTrace();
        }
        View.OnTouchListener clike=new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    if (view.isClickable()){}
                    else{
                        aduio_CannotStartLesson.start();
                    }
                }
                return false;
            }
        };
        lesson1.setOnTouchListener(clike);
        lesson2.setOnTouchListener(clike);
        lesson3.setOnTouchListener(clike);
        lesson4.setOnTouchListener(clike);
        lesson5.setOnTouchListener(clike);
        lesson6.setOnTouchListener(clike);
        Log.i("Lesson1",lesson1.isClickable()+" ");

        childUnitInfo lesson1obj=new childUnitInfo(0,null,lesson1Stars,lesson1,null);
        lesson1obj.setNextLesson(lesson2);
        childUnitInfo lesson2obj=new childUnitInfo(0,lock2,lesson2Stars, lesson2 ,null);
        lesson2obj.setNextLesson(lesson3);
        childUnitInfo lesson3obj=new childUnitInfo(0,lock3,lesson3Stars,lesson3,null);
        lesson3obj.setNextLesson(lesson4);
        childUnitInfo lesson4obj=new childUnitInfo(0,lock4,lesson4Stars,lesson4,null);
        lesson4obj.setNextLesson(lesson5);
        childUnitInfo lesson5obj=new childUnitInfo(0,lock5,lesson5Stars,lesson5,null);
        lesson5obj.setNextLesson(lesson6);
        childUnitInfo lesson6obj=new childUnitInfo(0,lock6,lesson6Stars,lesson6,null);
        lesson6obj.setNextLesson(lesson1);
        childUnitInfo test1obj=new childUnitInfo(0,null,test1Stars,test1,null);
        test1obj.setNextLesson(lesson1);
        test1obj.setNext2lesson(lesson2);
        childUnitInfo test2obj=new childUnitInfo(0,null,test2Stars,test2,null);
        test2obj.setNextLesson(lesson3);
        test2obj.setNext2lesson(lesson4);
        childUnitInfo test3obj=new childUnitInfo(0,null,test3Stars,test3,null);
        test3obj.setNextLesson(lesson5);
        test3obj.setNext2lesson(lesson6);
        Log.i("Lesson1",lesson1.isClickable()+" ");
        lesson1.setClickable(true);
        lessonsInfo.add(lesson1obj);
        lessonsInfo.add(lesson2obj);
        lessonsInfo.add(lesson3obj);
        lessonsInfo.add(lesson4obj);
        lessonsInfo.add(lesson5obj);
        lessonsInfo.add(lesson6obj);
        testInfo.add(test1obj);
        testInfo.add(test2obj);
        testInfo.add(test3obj);
         final View.OnClickListener lessonCliked=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.lesson1) {
                    Log.i("putExtra",lesson1.getText().toString());
                    lessonIntent.putExtra("Lessonltr",lessonsInfo.get(0).getLesson().getText().toString());
                    Log.i("Lesson1",lesson1.isClickable()+" ");
                }
                else if (view.getId() == R.id.lesson2) {
                    lessonIntent.putExtra("Lessonltr",lessonsInfo.get(1).getLesson().getText().toString());
                    Log.i("Lesson2",lesson2.isClickable()+" ");

                } else if (view.getId() == R.id.lesson3){
                    lessonIntent.putExtra("Lessonltr",lessonsInfo.get(2).getLesson().getText().toString());
                }else if (view.getId() == R.id.lesson4) {
                    lessonIntent.putExtra("Lessonltr",lessonsInfo.get(3).getLesson().getText().toString());

                } else if (view.getId() == R.id.lesson5) {
                    lessonIntent.putExtra("Lessonltr",lessonsInfo.get(4).getLesson().getText().toString());

                } else if (view.getId() == R.id.lesson6) {
                    Log.i("putExtra",lessonsInfo.get(5).getLesson().getText().toString());
                    lessonIntent.putExtra("Lessonltr",lessonsInfo.get(5).getLesson().getText().toString());
                }
                startActivity(lessonIntent);
            }
        };
        View.OnClickListener clickedTest =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.test1|| view.getId()==R.id.ballon1) {
                     random=randomTestNo.nextInt(4);
                     random2=randomTestNo.nextInt(4);
                     rand=randomTestNo.nextInt(2);
                     setRand(fillTest(random,random2,rand));
                     Intent fIntent=Rand.get(0);
                     Rand.remove(0);
                     test_letter=testInfo.get(0).getLetters();
                     startTime= Calendar.getInstance().getTimeInMillis();
                     startActivity(fIntent);

                } else if (view.getId() == R.id.test2|| view.getId()==R.id.ballon2) {
                     random=randomTestNo.nextInt(4);
                     random2=randomTestNo.nextInt(4);
                     rand=randomTestNo.nextInt(2);
                    setRand(fillTest(random,random2,rand));
                    Intent fIntent=Rand.get(0);
                    Rand.remove(0);
                    test_letter=testInfo.get(1).getLetters();
                    startTime= Calendar.getInstance().getTimeInMillis();
                    startActivity(fIntent);
                } else if (view.getId() == R.id.test3 || view.getId()==R.id.ballon3){
                     random=randomTestNo.nextInt(4);
                     random2=randomTestNo.nextInt(4);
                     rand=randomTestNo.nextInt(2);
                    setRand(fillTest(random,random2,rand));
                    Intent fIntent=Rand.get(0);
                    Rand.remove(0);
                    test_letter=testInfo.get(2).getLetters();
                    startTime= Calendar.getInstance().getTimeInMillis();
                    startActivity(fIntent);
                }
            }
        };
        bal1.setOnClickListener(clickedTest);
        bal2.setOnClickListener(clickedTest);
        bal3.setOnClickListener(clickedTest);
        test1.setOnClickListener(clickedTest);
        test2.setOnClickListener(clickedTest);
        test3.setOnClickListener(clickedTest);
        lesson1.setOnClickListener(lessonCliked);
        Log.i("Hi",lesson6.isInTouchMode()+" Jojo");
        Log.i("Lesson1",lesson1.isClickable()+" ");
        unedatble();
        Log.i("Lesson1",lesson1.isClickable()+" ");

        unitConnicetion.ref.child("Units").child(unitID).child("unit letters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                for(DataSnapshot s: dataSnapshot.getChildren()){
                    lessons.add(s.getValue().toString());
                }
                if(lessons.size()!=0){
                    setLessons(lessons);
                    //set lessons button
                    for (final childUnitInfo childUnitInfo:lessonsInfo){
                        childUnitInfo.Lesson.setText(lessons.get(lessonsInfo.indexOf(childUnitInfo)));
                        childUnitInfo.setLetters(lessons.get(lessonsInfo.indexOf(childUnitInfo)));
                        Log.i("Score", lessonsInfo.get(0).getLetters()+" stars");
                    }
                    int j=0;
                    //set test buttons
                    for (int i=0;i<3;i++) {
                        testInfo.get(i).Lesson.setText(lessons.get(j) + "," + lessons.get(j + 1));
                        testInfo.get(i).setLetters(lessons.get(j) + "_" + lessons.get(j + 1));
                        j += 2;
                    }

                    childScoreConnection.ref.child("child_takes_lesson").child(childID).child(unitID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot s : dataSnapshot.getChildren()) {
                                childLessons.add(s.getKey());
                            }
                            setChildLessons(childLessons);

                            if (childLessons.size() != 0) {
                                getscore.ref.child("Lessons").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (final DataSnapshot KeySnapshot : dataSnapshot.getChildren()) {
                                            final String lKey = KeySnapshot.getKey();
                                            DatabaseReference k = childLockConnection.ref.child(lKey).child("lesson_letter");
                                            ValueEventListener vla = new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (String childLesson : childLessons) {
                                                        if (lKey.equals(childLesson)) {
                                                            String lval = KeySnapshot.child("lesson_letter").getValue(String.class);
                                                            for (String letr : lessons) {
                                                                if (letr.equals(lval)) {
                                                                    lessonsInfo.get(lessons.indexOf(lval)).setLessonId(lKey);
                                                                }
                                                            }
                                                        }
                                                    }
                                                    getChildScoreConnection.ref.child("child_takes_lesson").child(childID).child(unitID).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (final DataSnapshot score : dataSnapshot.getChildren()) {
                                                                final String Lkey = score.getKey();
                                                                DatabaseReference scorRef = innerScore.ref.child(Lkey).child("score");
                                                                ValueEventListener scoreValEventLesiner = new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        for (childUnitInfo obj : lessonsInfo) {
                                                                            if (obj.getLessonId() != null) {
                                                                                if (obj.getLessonId().equals(Lkey)) {
                                                                                    Log.i("Score", score.child("score").getValue(Integer.class) + " ");
                                                                                    obj.setScore(score.child("score").getValue(Integer.class));
                                                                                }
                                                                            }
                                                                        }
                                                                        for (childUnitInfo childUnitInfo_lessonID : lessonsInfo) {
                                                                            if (childUnitInfo_lessonID.getLessonId() != null) {
                                                                                if (childUnitInfo_lessonID.getScore() < 4 && childUnitInfo_lessonID.getScore() > 0) {
                                                                                    childUnitInfo_lessonID.getStars().setImageResource(R.drawable.one_gold_stars_group);
                                                                                    childUnitInfo_lessonID.getNextLesson().setClickable(true);
                                                                                    childUnitInfo_lessonID.getNextLesson().setOnClickListener(lessonCliked);
                                                                                    childUnitInfo_lessonID.getNextLesson().bringToFront();
                                                                                } else if (childUnitInfo_lessonID.getScore() > 3 && childUnitInfo_lessonID.getScore() < 6) {
                                                                                    childUnitInfo_lessonID.getStars().setImageResource(R.drawable.two_gold_stars_group);
                                                                                    childUnitInfo_lessonID.getNextLesson().setClickable(true);
                                                                                    childUnitInfo_lessonID.getNextLesson().setOnClickListener(lessonCliked);
                                                                                    childUnitInfo_lessonID.getNextLesson().bringToFront();
                                                                                } else if (childUnitInfo_lessonID.getScore() > 5 && childUnitInfo_lessonID.getScore() < 8) {
                                                                                    childUnitInfo_lessonID.getStars().setImageResource(R.drawable.gold_three_stars);
                                                                                    childUnitInfo_lessonID.getNextLesson().setClickable(true);
                                                                                    childUnitInfo_lessonID.getNextLesson().setOnClickListener(lessonCliked);
                                                                                    childUnitInfo_lessonID.getNextLesson().bringToFront();
                                                                                } else if (childUnitInfo_lessonID.getScore() == 0) {
                                                                                    childUnitInfo_lessonID.getStars().setImageResource(R.drawable.gray_three_stars);
                                                                                    childUnitInfo_lessonID.getNextLesson().setClickable(true);
                                                                                    childUnitInfo_lessonID.getNextLesson().setOnClickListener(lessonCliked);
                                                                                    childUnitInfo_lessonID.getNextLesson().bringToFront();
                                                                                }
                                                                            }
                                                                            if (childUnitInfo_lessonID.getLock() != null && childUnitInfo_lessonID.getLessonId() != null && childUnitInfo_lessonID.getNextLesson() != null) {
                                                                                childUnitInfo_lessonID.getLock().setVisibility(View.GONE);
                                                                                childUnitInfo_lessonID.getLock().getVisibility();
                                                                                childUnitInfo_lessonID.getLesson().setClickable(true);
                                                                                childUnitInfo_lessonID.getLesson().bringToFront();
                                                                            }
                                                                            if (childUnitInfo_lessonID.getLesson().isClickable() && childUnitInfo_lessonID.getLock() != null) {
                                                                                childUnitInfo_lessonID.getLock().setVisibility(View.GONE);
                                                                                childUnitInfo_lessonID.getLock().getVisibility();
                                                                            }

                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                };
                                                                scorRef.addValueEventListener(scoreValEventLesiner);


                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            };
                                            k.addValueEventListener(vla);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

                testScoreq.ref.child("child_takes_test").child(childID).child(unitID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("Lesson1",lesson1.isClickable()+" ");

                        for (final DataSnapshot data2:dataSnapshot.getChildren()) {
                            final String childTests = data2.getKey();
                            testIDq.ref.child("Tests").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (final DataSnapshot test : dataSnapshot.getChildren()) {
                                        final String testkey = test.getKey();
                                        DatabaseReference getId = testIDq2.ref.child("Tests").child(testkey);
                                        ValueEventListener testLis = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (testkey.equals(childTests)) {
                                                        String testletter = test.child("test_letters").getValue(String.class);
                                                        for (childUnitInfo test : testInfo) {
                                                            String letter = test.getLetters();
                                                            Log.i("let", letter);
                                                            Log.i("try", letter.contains(testletter) + " " + letter + " " + testletter);
                                                            if (letter.equals(testletter)) {
                                                                test.setLessonId(testkey);
                                                                Log.i("TestID", test.getLessonId() + " ");
                                                            }

                                                        }
                                                        DatabaseReference TestScore=testgetSq1.ref.child("child_takes_test").child(childID).child(unitID).child(testkey);
                                                        ValueEventListener TestScoreEvent=new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (childUnitInfo test : testInfo) {
                                                                    String ID=test.getLessonId();
                                                                    if(ID!=null){
                                                                    if (ID.equals(testkey)) {
                                                                        test.setScore(data2.child("score").getValue(Integer.class));

                                                                        Log.i("TestID", test.getScore() + " "+test.getLetters()+" "+test.getLessonId());
                                                                    }

                                                                }}
                                                                for (childUnitInfo childUnitInfo_lessonID:testInfo) {
                                                                    if(childUnitInfo_lessonID.getLessonId()!=null){
                                                                        Log.i("allinfogaingh",childUnitInfo_lessonID.getScore()+" "+childUnitInfo_lessonID.getLessonId()+" "+childUnitInfo_lessonID.getLetters());
                                                                        if(childUnitInfo_lessonID.getScore()<4&& childUnitInfo_lessonID.getScore()>0){
                                                                            childUnitInfo_lessonID.getStars().setImageResource(R.drawable.one_gold_stars_group);
                                                                        }else if(childUnitInfo_lessonID.getScore()>3&& childUnitInfo_lessonID.getScore()<8){
                                                                            childUnitInfo_lessonID.getStars().setImageResource(R.drawable.two_gold_stars_group);
                                                                        }else if(childUnitInfo_lessonID.getScore()>7&& childUnitInfo_lessonID.getScore()<11){
                                                                            childUnitInfo_lessonID.getStars().setImageResource(R.drawable.gold_three_stars);
                                                                            childUnitInfo_lessonID.getNextLesson().setClickable(true);
                                                                            childUnitInfo_lessonID.getNext2lesson().setClickable(true);
                                                                        }else if(childUnitInfo_lessonID.getScore()==0){
                                                                            childUnitInfo_lessonID.getStars().setImageResource(R.drawable.gray_three_stars);
                                                                        }
                                                                    }
                                                                }
                                                                   for (childUnitInfo l:lessonsInfo){
                                                                       if (l.getLesson().isClickable() &&l.getLock()!=null ){
                                                                           l.getLesson().setOnClickListener(lessonCliked);
                                                                           l.getLock().setVisibility(View.GONE);
                                                                           l.getLock().getVisibility();
                                                                       }
                                                                   }


                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        };
                                                        TestScore.addValueEventListener(TestScoreEvent);
                                                    }
                                                }


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        };
                                        getId.addValueEventListener(testLis);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                }
                catch(Exception e){System.out.println(e);}
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }
    public void playAudio(String url){
        try {

            instructions.reset();
            instructions.setAudioStreamType(AudioManager.STREAM_MUSIC);
            instructions.setDataSource(url);
            instructions.prepare();
            instructions.start();

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
    private void unedatble(){
        for (childUnitInfo j:lessonsInfo){
            if(j.getLessonId()==null){
            j.getLesson().setClickable(false);
            j.getLesson().bringToFront();
            }
        }
        lessonsInfo.get(0).getLesson().setClickable(true);
        lessonsInfo.get(0).getLesson().bringToFront();

    }
    @Override
    protected void onStop() {
        super.onStop();
        try{
            instructions.stop();
        }catch (Exception e){
            System.err.println("Unable to stop activity");
        }

    }

    private ArrayList<Intent> fillTest(int random,int random2,int rand){
        ArrayList<Intent> rTest=new ArrayList<Intent>();
        Intent [] arr=new Intent[4];
        Intent swap;
        for(int j=0;j<testIntent.size();j++){
            arr[j]=testIntent.get(j);
        }
        swap=arr[random];
        arr[random]=arr[random2];
        arr[random2]=swap;
        switch (rand){
            case 0:
          for(int j=0;j<arr.length;j++){
            rTest.add(arr[j]);
         }
         break;
            case 1:
                int i=0;
                for(int j=arr.length-1;j>=0;j--) {
                    rTest.add(arr[j]);
                    i++;
                }
         break;

        }
        if(rTest.size()!=0){
        return rTest;
        }
        return null;
    }

    public void setLessons(ArrayList<String> lessons) {
        this.lessons = lessons;
    }

    public void setTestIntent(ArrayList<Intent> testIntent) {
        this.testIntent = testIntent;
    }

    public void setTestStringForTesting(ArrayList<String> testStringForTesting) {
        TestStringForTesting = testStringForTesting;
    }
    public void setTestStringForTesting2(ArrayList<String> testStringForTesting2) {
        TestStringForTesting2 = testStringForTesting2;
    }
    public void setChildLessons(ArrayList<String> childLessons) {
        this.childLessons = childLessons;
    }
    public void setOpenLessons(ArrayList<String> openLessons) {
        this.openLessons = openLessons;
    }

    public ArrayList<Intent> getRand() {
        return Rand;
    }

    public void setRand(ArrayList<Intent> rand) {
        Rand = rand;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        if (resultCode == RESULT_OK) {

            this.unitID=b.getString("unitID");
        }
    }
    public static void  test_score(final String test_id){

        if(endtest==true){
            System.out.println("End time: "+ EndTime);
            double time = EndTime - startTime;
            time = (time/1000)/60;
            actual_time =new DecimalFormat("##.##").format(time);

            finalScore= ReadingTest.reading_child_score+
                    HeardWordTest.final_heard_child_score+
                    TrueFalseTest.true_false_test_score + MatchingTest.score;
            finalScore=finalScore/4;

            Query query =  r.ref.child("child_takes_test").child(Signin.id_child).child(unit_interface.unitID).orderByKey().equalTo(test_id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        System.out.println("Eixist!!!!!!!!");
                        try{
                            DatabaseReference read_score =  r.ref.child("child_takes_test").child(Signin.id_child).child(unit_interface.unitID).child(test_id);
                            read_score.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    System.out.println("Inside read");

                                    for (final DataSnapshot info: dataSnapshot.getChildren()){
                                        currentScore = Integer.valueOf(dataSnapshot.child("score").getValue().toString());
                                        childTime = dataSnapshot.child("time").getValue().toString();
                                    }
                                    if(currentScore<finalScore){
                                        r.ref.child("child_takes_test").child(Signin.id_child)
                                                .child(unit_interface.unitID).child(test_id).child("score").setValue(finalScore);
                                        r.ref.child("child_takes_test").child(Signin.id_child)
                                                .child(unit_interface.unitID).child(test_id).child("time").setValue(actual_time);
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

                        r.ref.child("child_takes_test").child(Signin.id_child).child(unit_interface.unitID).child(test_id).child("score").setValue(finalScore);
                        r.ref.child("child_takes_test").child(Signin.id_child).child(unit_interface.unitID).child(test_id).child("time").setValue(actual_time);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        }

    }

}