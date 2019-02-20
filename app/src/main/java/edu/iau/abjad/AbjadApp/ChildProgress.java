package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class ChildProgress extends menu_educator {
    menu_variables m = new menu_variables();
    private TextView nUnlokedLesson;
    private TextView nDoneLesson;
    private TextView nTimer;
    private TextView highestScoreLesson;
    private TextView nDoneTest;
    private TextView highestScoreTest;
    private firebase_connection lesson_unloked,lesson,lesson_comp,letterLesson;
    private firebase_connection test;
    private firebase_connection child;
    private String childID;
    private   String name;
    private long unlookedLesson,testNo;
    int icomplete=0;
    int ihighestScore=0,ihighestLessonScore=0;
    double dleastTime;
    String  sTime, sLeastTime,sHighstScoreLesson,sHighstScoreTest,lett,lettTest,lastName;
    double dTime;
    ArrayList <childUnitInfo> lessonScores;
    ArrayList <childUnitInfo> testScore, timeCom;
    int xLarge, large, normal, small, default_size;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m.title = (TextView) findViewById(R.id.interface_title);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        //inflate your activity layout here!
        final View contentView = inflater.inflate(R.layout.activity_child_progress, null, false);
        sTime=""; sLeastTime="";sHighstScoreLesson="";sHighstScoreTest="";lett="";
        mDrawerLayout.addView(contentView, 0);






        //intilization
        nUnlokedLesson=findViewById(R.id.nUnlokedLesson);
        nDoneLesson=findViewById(R.id.nDoneLesson);
        nTimer=findViewById(R.id.nTimer);
        highestScoreLesson=findViewById(R.id.highestScoreLesson);
        nDoneTest=findViewById(R.id.nDoneTest);
        highestScoreTest=findViewById(R.id.highestScoreTest);
        lesson_unloked=new firebase_connection();
        lesson=new firebase_connection();
        lesson_comp=new firebase_connection();
        letterLesson=new firebase_connection();
        test=new firebase_connection();
        child = new firebase_connection();
        Bundle b=getIntent().getExtras();
        childID=b.getString("child_ID");
        dleastTime=100000.00;
        sLeastTime="";
        sHighstScoreLesson="";
        sHighstScoreTest="";
        lett="";
        lessonScores=new ArrayList<childUnitInfo>();
        testScore=new ArrayList<childUnitInfo>();
        timeCom=new ArrayList<childUnitInfo>();
        xLarge = 20;
        large = 17;
        normal=14;
        small = 10;
        default_size = 16;
        lastName="";

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // screen size
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        TextView imgname=findViewById(R.id.imgname);
        TextView hUnlokedLesson=findViewById(R.id.hUnlokedLesson);
        TextView hDoneLesson=findViewById(R.id.hDoneLesson);
        TextView hTimer=findViewById(R.id.hTimer);
        TextView hHighestScoreLesson=findViewById(R.id.hHighestScoreLesson);
        TextView hDoneTest=findViewById(R.id.hDoneTest);
        TextView hHighestScoreTest=findViewById(R.id.hHighestScoreTest);
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                imgname.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                hUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                nUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                nDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                hDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                nTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                hTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                hHighestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                hDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                hHighestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                nDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                highestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,xLarge);
                m.setTitle_XLarge();
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                imgname.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                hUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                nUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                nDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                hDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                nTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                hTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                hHighestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                hDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                hHighestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                nDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                highestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,large);
                m.setTitle_Large();
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                imgname.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                hUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                nUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                nDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                hDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                nTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                hTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                hHighestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                hDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                hHighestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP, normal);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                nDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                highestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,normal);
                m.setTitle_Normal();
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                imgname.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                hUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                nUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                nDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                hDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                nTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                hTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                hHighestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                hDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                hHighestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                nDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                highestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,small);
                m.setTitle_Small();
                break;
            default:
                imgname.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hHighestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hHighestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                highestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                m.setTitle_Default();
        }

        child.ref.child("Children").child(childID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name=dataSnapshot.child("first_name").getValue(String.class);
                lastName=dataSnapshot.child("last_name").getValue(String.class);
                m.title.setText(name +" "+ lastName);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get data from firebase and set Text view
        lesson.ref.child("child_takes_lesson").child(childID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    final String unitId=snapshot.getKey();
                    if (unitId!=null){
                        DatabaseReference nLeson=lesson_unloked.ref.child(childID).child(unitId);
                        ValueEventListener unlokedLessonNo_Event=new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                unlookedLesson+=dataSnapshot.child(unitId).getChildrenCount();
                                nUnlokedLesson.setText(unlookedLesson+" ");
                                for(final DataSnapshot s:snapshot.getChildren()){
                                    final String lessonKey=s.getKey();
                                    DatabaseReference complete=lesson_comp.ref.child(childID).child(unitId).child(lessonKey);
                                    ValueEventListener completeEvent=new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            DatabaseReference getLetter=letterLesson.ref.child("Lessons");
                                            ValueEventListener evntLetr=new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    try{
                                                        final String status=s.child("status").getValue(String.class);
                                                        final int ilessonScore=s.child("score").getValue(Integer.class);
                                                        sTime=s.child("time").getValue().toString();
                                                        String modifiedsTime = arabicToDecimal(sTime);
                                                        dTime=Double.parseDouble(modifiedsTime);

                                                        lett=dataSnapshot.child(lessonKey)
                                                                .child("lesson_letter").getValue().toString();
                                                        if(status.equals("مكتمل")){
                                                            icomplete++;
                                                        }
                                                        if(ilessonScore>=ihighestLessonScore){
                                                            ihighestLessonScore=ilessonScore;
                                                            lessonScores.add(new childUnitInfo(ilessonScore,lett,dTime));
                                                            //sHighstScoreLesson=lett;
                                                        }
                                                        if (dTime<=dleastTime){
                                                            dleastTime=dTime;
                                                            timeCom.add( new childUnitInfo(0,lett,dleastTime) );
                                                            // sLeastTime=lett;

                                                        }
                                                        for(childUnitInfo child: timeCom){
                                                            if(dleastTime==child.time &&(!sLeastTime.contains(child.letters))){
                                                                sLeastTime=child.letters+" /";
                                                            }
                                                        }
                                                        if(sLeastTime.length()!=0){
                                                            sLeastTime= sLeastTime.substring(0,sLeastTime.length()-1);
                                                        }
                                                        for(childUnitInfo child: lessonScores){
                                                            if(ihighestLessonScore==child.score &&(!sHighstScoreLesson.contains(child.letters))){
                                                                sHighstScoreLesson+=child.letters+" /";
                                                            }
                                                        }

                                                        if(sHighstScoreLesson.length()!=0){
                                                            sHighstScoreLesson=sHighstScoreLesson.substring(0,sHighstScoreLesson.length()-1);
                                                        }
                                                        nTimer.setText(dleastTime+" "+(dleastTime<1?"ثانية":" دقيقة"));
                                                        highestScoreLesson.setText(ihighestLessonScore+" /7");
                                                        nDoneLesson.setText(icomplete+" ");
                                                    }catch (Exception e){

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            };getLetter.addValueEventListener(evntLetr);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    };
                                    complete.addValueEventListener(completeEvent);

                                }
                            }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        nLeson.addValueEventListener(unlokedLessonNo_Event);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        test.ref.child("child_takes_test").child(childID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    final String unitId=snapshot.getKey();
                    if (unitId!=null){
                        DatabaseReference nLeson=lesson_unloked.ref.child(childID).child(unitId);
                        ValueEventListener unlokedLessonNo_Event=new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                testNo+=dataSnapshot.child(unitId).getChildrenCount();
                                nDoneTest.setText(testNo+" ");
                                for(final DataSnapshot s:snapshot.getChildren()){
                                    final String TestId=s.getKey();
                                    DatabaseReference complete=lesson_comp.ref.child(childID).child(unitId).child(TestId);
                                    ValueEventListener completeEvent=new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            DatabaseReference getLetter=letterLesson.ref.child("Tests");
                                            ValueEventListener evntLetr=new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot1) {
                                                    final int iTestScore=s.child("score").getValue(Integer.class);


                                                    lettTest=dataSnapshot1.child(TestId).child("test_letters").getValue().toString();
                                                    if(iTestScore>=ihighestScore){
                                                        ihighestScore=iTestScore;

                                                        // sHighstScoreTest=lettTest+" ";
                                                        testScore.add(new childUnitInfo(iTestScore,lettTest,dTime));

                                                    }
                                                    for(childUnitInfo child: testScore){
                                                        if(ihighestScore==child.score&& (!sHighstScoreTest.contains(child.letters))){
                                                            sHighstScoreTest+=child.letters+" / ";
                                                        }
                                                    }
                                                    if(sHighstScoreTest.length()!=0) {
                                                        //sHighstScoreTest.replace("_","،");
                                                        // sHighstScoreTest=
                                                       // Log.i("sHighstScore",sHighstScoreTest.substring(0,sHighstScoreTest.length()-1));
                                                    }
                                                    highestScoreTest.setText(ihighestScore+" /10");
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            };getLetter.addValueEventListener(evntLetr);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    };
                                    complete.addValueEventListener(completeEvent);

                                }
                            }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        nLeson.addValueEventListener(unlokedLessonNo_Event);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            else if(ch=='٫')
                ch='.';
            chars[i] = ch;
        }
        return new String(chars);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(),educator_home.class));

    }
}
