package edu.iau.abjad.AbjadApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Locale;

import static java.security.AccessController.getContext;

public class ChildProgress extends menu_educator {
    menu_variables m = new menu_variables();
    private ImageView viewChildProfile;
    private ImageView deleteChild;
    private ImageView changePass;
    private TextView nUnlokedLesson;
    private TextView nDoneLesson;
    private TextView nTimer;
    private TextView LessonNameTimer;
    private TextView highestScoreLesson;
    private TextView lessonNameScore;
    private TextView nDoneTest;
    private TextView highestScoreTest;
    private TextView testName;
    private firebase_connection lesson_unloked,lesson,lesson_comp,letterLesson;
    private firebase_connection test,nTest;
    private firebase_connection child,deleteChild_Children,deleteChild_edu,deleteChild_lesson,deleteChild_test;
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
         LessonNameTimer=findViewById(R.id.LessonNameTimer);
         highestScoreLesson=findViewById(R.id.highestScoreLesson);
         lessonNameScore=findViewById(R.id.LessonNameScore);
         nDoneTest=findViewById(R.id.nDoneTest);
         highestScoreTest=findViewById(R.id.highestScoreTest);
         testName=findViewById(R.id.testName);
        lesson_unloked=new firebase_connection();
        lesson=new firebase_connection();
        lesson_comp=new firebase_connection();
        letterLesson=new firebase_connection();
         test=new firebase_connection();
         child = new firebase_connection();
         Bundle b=getIntent().getExtras();
         childID=b.getString("child_ID");
         deleteChild_Children=new firebase_connection();
         deleteChild_edu=new firebase_connection();
         deleteChild_lesson=new firebase_connection();
         deleteChild_test=new firebase_connection();
        nTest=new firebase_connection();
        dleastTime=100000.00;
        sLeastTime="";
        sHighstScoreLesson="";
        sHighstScoreTest="";
        lett="";
       lessonScores=new ArrayList<childUnitInfo>();
       testScore=new ArrayList<childUnitInfo>();
        timeCom=new ArrayList<childUnitInfo>();

        lastName="";

        final String lessonh="";
        final Intent educatorHome=new Intent(this,educator_home.class);
        final Intent changePassword =new Intent(this, change_password.class );


        // screen size
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        TextView imgname=findViewById(R.id.imgname);
        TextView hUnlokedLesson=findViewById(R.id.hUnlokedLesson);
        TextView hDoneLesson=findViewById(R.id.hDoneLesson);
        TextView hTimer=findViewById(R.id.hTimer);
        TextView hLessonNameTimer=findViewById(R.id.hLessonNameTimer);
        TextView hHighestScoreLesson=findViewById(R.id.hHighestScoreLesson);
        TextView hDoneTest=findViewById(R.id.hDoneTest);
        TextView hHighestScoreTest=findViewById(R.id.hHighestScoreTest);
        TextView lesson_99=findViewById(R.id.lesson_99);
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                imgname.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                hUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                LessonNameTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                nUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                nDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                hDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                nTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                hTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                hLessonNameTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                hHighestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                hDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                hHighestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                lesson_99.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                lessonNameScore .setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                nDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                highestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                testName .setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                m.setTitle_XLarge();
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                imgname.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                hUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                LessonNameTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                nUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                nDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                hDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                nTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                hTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                hLessonNameTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                hHighestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                hDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                hHighestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                lesson_99.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                lessonNameScore .setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                nDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                highestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                testName .setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                m.setTitle_Large();
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                imgname.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                hUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                LessonNameTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                nUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                nDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                hDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                nTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                hTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                hLessonNameTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                hHighestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                hDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                hHighestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                lesson_99.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                lessonNameScore .setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                nDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                highestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                testName .setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                m.setTitle_Normal();
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                imgname.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                hUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                LessonNameTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                nUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                nDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                hDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                nTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                hTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                hLessonNameTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                hHighestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                hDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                hHighestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                lesson_99.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                lessonNameScore .setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                nDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                highestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                testName .setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                m.setTitle_Small();
                break;
            default:
                imgname.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                LessonNameTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nUnlokedLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hDoneLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hLessonNameTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hHighestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                hHighestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                highestScoreLesson.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                lesson_99.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                lessonNameScore.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nDoneTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                highestScoreTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                testName .setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                m.setTitle_Default();
        }

        final Intent c=new Intent(this,userTypeSelection.class);
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
                    Log.i("unitId",unitId);
                    if (unitId!=null){
                        DatabaseReference nLeson=lesson_unloked.ref.child(childID).child(unitId);
                        ValueEventListener unlokedLessonNo_Event=new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                unlookedLesson+=dataSnapshot.child(unitId).getChildrenCount();
                                nUnlokedLesson.setText(unlookedLesson+" ");
                                for(final DataSnapshot s:snapshot.getChildren()){
                                    final String lessonKey=s.getKey();
                                    Log.i("lessonKey",s.getKey()+" ");
                                    DatabaseReference complete=lesson_comp.ref.child(childID).child(unitId).child(lessonKey);
                                    ValueEventListener completeEvent=new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            DatabaseReference getLetter=letterLesson.ref.child("Lessons");
                                            ValueEventListener evntLetr=new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final String status=s.child("status").getValue(String.class);
                                                    final int ilessonScore=s.child("score").getValue(Integer.class);
                                                    sTime=s.child("time").getValue().toString();
                                                    dTime=Double.parseDouble(sTime);

                                                   lett=dataSnapshot.child(lessonKey)
                                                            .child("lesson_letter").getValue().toString();
                                                    Log.i("time",dTime+" m");
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
                                                    lessonNameScore.setText(sHighstScoreLesson+"");
                                                    LessonNameTimer.setText(sLeastTime+" ");
                                                    Log.i("status",status);
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
                    Log.i("unitId",unitId);
                    if (unitId!=null){
                        DatabaseReference nLeson=lesson_unloked.ref.child(childID).child(unitId);
                        ValueEventListener unlokedLessonNo_Event=new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                testNo+=dataSnapshot.child(unitId).getChildrenCount();
                                nDoneTest.setText(testNo+" ");
                                for(final DataSnapshot s:snapshot.getChildren()){
                                    final String TestId=s.getKey();
                                    Log.i("lessonKey",s.getKey()+" ");
                                    DatabaseReference complete=lesson_comp.ref.child(childID).child(unitId).child(TestId);
                                    ValueEventListener completeEvent=new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            DatabaseReference getLetter=letterLesson.ref.child("Tests");
                                            ValueEventListener evntLetr=new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot1) {
                                                    final int iTestScore=s.child("score").getValue(Integer.class);
                                                    Log.i("TestID",TestId);
                                                    Log.i("TestID",dataSnapshot1.child(TestId).child("test_letters").getValue(String.class)+" ");

                                                    lettTest=dataSnapshot1.child(TestId).child("test_letters").getValue().toString();
                                                    Log.i("dsdasd",lettTest+"" );
                                                    if(iTestScore>=ihighestScore){
                                                        Log.i("score",iTestScore+ " ");
                                                        ihighestScore=iTestScore;

                                                       // sHighstScoreTest=lettTest+" ";
                                                        testScore.add(new childUnitInfo(iTestScore,lettTest,dTime));

                                                    }

                                                    Log.i("score3",ihighestScore+ " ");
                                                    for(childUnitInfo child: testScore){
                                                        if(ihighestScore==child.score&& (!sHighstScoreTest.contains(child.letters))){
                                                            sHighstScoreTest+=child.letters+" / ";
                                                        }
                                                    }
                                                    if(sHighstScoreTest.length()!=0) {
                                                        //sHighstScoreTest.replace("_","،");
                                                      // sHighstScoreTest=
                                                        Log.i("sHighstScore",sHighstScoreTest.substring(0,sHighstScoreTest.length()-1));
                                                    }
                                                    Log.i("fffff",sHighstScoreTest+" ");
                                                    highestScoreTest.setText(ihighestScore+" /10");
                                                    testName.setText(sHighstScoreTest.substring(0,sHighstScoreTest.length()-2).replace("_","،"));
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

       /* test.ref.child("child_takes_test").child(childID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for(final DataSnapshot snshot:dataSnapshot.getChildren()){
                    final String unitId_test=snshot.getKey();
                    Log.i("unitIDTest",unitId_test);
                    if (unitId_test!=null){
                        DatabaseReference test_done=nTest.ref.child(childID).child(unitId_test);
                        ValueEventListener unlokedLessonNo_Event=new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                testNo+=snshot.child(unitId_test).getChildrenCount();
                                Log.i("TesNo",testNo+" "+snshot.child(unitId_test).getChildrenCount());
                                nDoneTest.setText(testNo+" ");
                                Log.i("unitIDTest",unitId_test);
                                Log.i("Tests",snshot.child(unitId_test).getChildrenCount()+" ");
                                for(final DataSnapshot s:snshot.getChildren()) {
                                    final String testKey = s.getKey();
                                    Log.i("lessonKey", s.getKey() + " ");
                                    final DatabaseReference highestScore = lesson_comp.ref.child(childID).child(unitId_test).child(testKey);
                                    ValueEventListener TestEvent = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int iscore = s.child("score").getValue(Integer.class);
                                            if (iscore > ihighestScore) {
                                                ihighestScore = iscore;

                                            }

                                            highestScoreTest.setText(ihighestScore + " ");
                                            Log.i("score", iscore + " /10");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    };
                                    highestScore.addValueEventListener(TestEvent);
                                } }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };test_done.addValueEventListener(unlokedLessonNo_Event);
                    }}}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }


}
