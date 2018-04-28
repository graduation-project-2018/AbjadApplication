package edu.iau.abjad.AbjadApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
    String  sTime, sLeastTime,sHighstScoreLesson,sHighstScoreTest,lett,lettTest;
    double dTime;
    ArrayList <childUnitInfo> lessonScores;
    ArrayList <childUnitInfo> testScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m.title = (TextView) findViewById(R.id.interface_title);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        final View contentView = inflater.inflate(R.layout.activity_child_progress, null, false);
        sTime=""; sLeastTime="";sHighstScoreLesson="";sHighstScoreTest="";lett="";
        mDrawerLayout.addView(contentView, 0);
        //intilization
        viewChildProfile= findViewById(R.id.ChildProfile);
        changePass=findViewById(R.id.changePass);
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
        dleastTime=1000000000.00;
        sLeastTime="";
        sHighstScoreLesson="";
        sHighstScoreTest="";
        lett="";
       lessonScores=new ArrayList<childUnitInfo>();
       testScore=new ArrayList<childUnitInfo>();

        final String lessonh="";
        final Intent educatorHome=new Intent(this,educator_home.class);
        final Intent changePassword =new Intent(this, change_password.class );

         //set onClickListener for 3 buttons
         viewChildProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childProfile =new Intent(ChildProgress.this, child_profile.class );
                childProfile.putExtra("childID",childID);
                setResult(RESULT_OK, childProfile);
                startActivity(childProfile);
            }
        });

        final Intent c=new Intent(this,userTypeSelection.class);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(changePassword);

            }
        });
        child.ref.child("Children").child(childID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             name=dataSnapshot.getValue(String.class);
                m.title.setText(name + " ");

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
                                                    Log.i("GGGGGGGG",dataSnapshot.child(lessonKey)
                                                            .child("lesson_letter").getValue().toString()+" juju");
                                                   lett=dataSnapshot.child(lessonKey)
                                                            .child("lesson_letter").getValue().toString();
                                                    Log.i("time",dTime+" m");
                                                    if(status.equals("مكتمل")){
                                                        icomplete++;
                                                    }
                                                    if(ilessonScore>=ihighestLessonScore){
                                                        ihighestLessonScore=ilessonScore;
                                                        lessonScores.add(new childUnitInfo(ilessonScore,lett));
                                                        //sHighstScoreLesson=lett;
                                                    }
                                                    if (dTime<=dleastTime){
                                                        dleastTime=dTime;
                                                        sLeastTime=lett;
                                                    }
                                                    for(childUnitInfo child: lessonScores){
                                                        if(ihighestLessonScore==child.score){
                                                            sHighstScoreLesson+=child.letters+" ";
                                                        }
                                                    }


                                                    nTimer.setText(dleastTime+" "+(dleastTime<1?"/ s":"/ m"));
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
                                                    if(iTestScore>ihighestScore){
                                                        Log.i("score",iTestScore+ " ");
                                                        Log.i("score",ihighestLessonScore+ " ");
                                                        ihighestScore=iTestScore;
                                                       // sHighstScoreTest=lettTest+" ";
                                                     }
                                                    lessonScores.add(new childUnitInfo(iTestScore,lettTest));

                                                    Log.i("score3",ihighestScore+ " ");
                                                    for(childUnitInfo child: testScore){
                                                        if(ihighestScore==child.score){
                                                            sHighstScoreTest+=child.letters+" ";
                                                        }
                                                    }
                                                    highestScoreTest.setText(ihighestScore+" /10");
                                                    testName.setText(sHighstScoreTest.replace("_","،")+" .");

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
