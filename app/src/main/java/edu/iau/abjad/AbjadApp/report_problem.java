package edu.iau.abjad.AbjadApp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class report_problem extends menu_educator {
    public static final int PIC_GALLERY_REQUIST = 20;
    menu_variables m = new menu_variables();
    private Button reportBtn, help_btn;
    private StorageReference mStorge;
    private TextView ImgNameTextView, problem_type_label;
    private firebase_connection report;
    private String daownloadURL;
    private RadioGroup  rg;
    private RadioButton problemType,selectedRadioButton;
    private String ProbType;
    private String dis;
    private EditText moreDetails;
    private HashMap<String,String> dataMap;
    private Uri imgURI;
    private boolean uploded=false;
    private int storagePermCode=1;
    private ProgressDialog mProgressDialog;
    private boolean onCliked=false;
    RadioButton problem_type1, problem_type2, problem_type3, problem_type4, problem_type5, problem_type6, problem_type7, problem_type_more;
    ArrayList <RadioButton> array_of_problems;
    FirebaseAuth Uath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        m.title = findViewById(R.id.interface_title);

        m.title.setText("إبلاغ عن مشكلة");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_report_problem, null, false);
        mDrawerLayout.addView(contentView, 0);
        //initialization
        reportBtn= findViewById(R.id.reportButn );
        ImgNameTextView= findViewById(R.id.imgname);
        rg= findViewById(R.id.radioButtonsGroup);
        help_btn = findViewById(R.id.help_btn);
        problem_type_label = findViewById(R.id.problem_type_label);
        problem_type1 = findViewById(R.id.Problem_type_1);
        problem_type2 = findViewById(R.id.Problem_type_2);
        problem_type3 = findViewById(R.id.Problem_type_3);
        problem_type4 = findViewById(R.id.Problem_type_4);
        problem_type5 = findViewById(R.id.Problem_type_5);
        problem_type6 = findViewById(R.id.Problem_type_6);
        problem_type7 = findViewById(R.id.Problem_type_7);
        problem_type_more = findViewById(R.id.more);
        array_of_problems = new ArrayList<RadioButton>();
        Uath= FirebaseAuth.getInstance();

        // add radio button to Array list
        array_of_problems.addAll(Arrays.asList(problem_type1, problem_type2, problem_type3,
                problem_type4, problem_type5, problem_type6, problem_type7, problem_type_more));



        dataMap=new HashMap<String, String>();
        dis="-";
        moreDetails=findViewById(R.id.moredetails);
        mProgressDialog=new ProgressDialog(this);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                ImgNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                ImgNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.upload_icon_2x, 0);
                help_btn.setBackgroundResource(R.drawable.help_icon_2x);
                problem_type_label.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.problem_icon, 0);
                problem_type_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                setRadioButtons_Sizes(30, 1, 102);
                m.setButton_text_XLarge(reportBtn);
                m.setTitle_XLarge();
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                ImgNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,27);
                ImgNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.upload_icon_15x, 0);
                help_btn.setBackgroundResource(R.drawable.help_icon_2x);
                problem_type_label.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.problem_icon_2x, 0);
                problem_type_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,35);
                setRadioButtons_Sizes(30, 1, 102);
                m.setButton_text_Large(reportBtn);
                m.setTitle_Large();
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                ImgNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                ImgNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.upload_icon_1x, 0);
                problem_type_label.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.problem_icon_15x, 0);
                problem_type_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                help_btn.setBackgroundResource(R.drawable.help_icon_15x);
                setRadioButtons_Sizes(25, 0.7f, 0);
                m.setButton_text_Normal(reportBtn);
                m.setTitle_Normal();
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                ImgNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                ImgNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.upload_icon_1x, 0);
                help_btn.setBackgroundResource(R.drawable.help_icon_15x);
                problem_type_label.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.problem_icon_15x, 0);
                problem_type_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                setRadioButtons_Sizes(25, 0.5f, 0);
                m.setButton_text_Small(reportBtn);
                m.setTitle_Small();
                break;
            default:
                ImgNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                ImgNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.upload_icon_1x, 0);
                help_btn.setBackgroundResource(R.drawable.help_icon_2x);
                problem_type_label.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.problem_icon_15x, 0);
                problem_type_label.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                setRadioButtons_Sizes(25, 0.7f, 0);
                m.setButton_text_Default(reportBtn);
                m.setTitle_Default();
        }

        //firbase storge refrance
        mStorge = FirebaseStorage.getInstance().getReference();
        report=new firebase_connection();
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImgNameTextView.getText().equals("اضغط هنا لرفع صورة لقطة الشاشة")== false){onCliked=true;}
                if(onCliked){
                if (rg.getCheckedRadioButtonId()!=-1){
                   selectedRadioButton = (RadioButton)findViewById(rg.getCheckedRadioButtonId());
                   if(( moreDetails.getText().toString().equals("")|| moreDetails.getText().toString().equals(" ")||moreDetails.getText().toString().equals("الوصف"))&& selectedRadioButton.getText().toString().equals("أخرى")){
                    Toast.makeText(report_problem.this,"الرجاء إدخال وصف البلاغ", Toast.LENGTH_SHORT).show();
                            return;}

                final StorageReference filePath = mStorge.child("ReportProblemScreenshots").child(imgURI.getLastPathSegment());
                    StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = filePath.putFile(imgURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.setMessage("Uploading...");
                            mProgressDialog.show();
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri = uri;
                                    daownloadURL = downloadUri.toString();
                                    uploded = true;
                                    if (rg.getCheckedRadioButtonId() != -1 && uploded) {
                                        String semail= Uath.getCurrentUser().getEmail();
                                        mProgressDialog.dismiss();
                                        dataMap.put("screenShots", daownloadURL);
                                        dataMap.put("report_type", ProbType);
                                        dataMap.put("report_description", dis);
                                        dataMap.put("reporter_email",semail);
                                        report.ref.child("Reports").push().setValue(dataMap);
                                        Toast.makeText(report_problem.this, "شكرا، تم تلقي البلاغ!", Toast.LENGTH_LONG).show();
                                        rg.clearCheck();
                                        ImgNameTextView.setText("اضغط هنا لرفع صورة لقطة الشاشة");
                                        moreDetails.setText("");
                                        daownloadURL = "";
                                    }
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(report_problem.this, "خطأ في رفع الصورة", Toast.LENGTH_LONG).show();
                            System.out.println(e);
                        }
                    });}
                    else if(rg.getCheckedRadioButtonId()==-1){
                    Toast.makeText(report_problem.this,"الرجاء تحديد نوع المشكلة",Toast.LENGTH_LONG).show();
                }
                }else{
                    Toast.makeText(report_problem.this,"الرجاء إرفاق صورة لقطة الشاشة",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onImgGalleryCliked(View view) {
        if (ContextCompat.checkSelfPermission(report_problem.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Intent picChooserIntent = new Intent(Intent.ACTION_PICK);
            File picDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String picDirPath = picDirectory.getPath();
            Uri data = Uri.parse(picDirPath);
            picChooserIntent.setDataAndType(data, "image/*");
            startActivityForResult(picChooserIntent, PIC_GALLERY_REQUIST);
        }
        else{
            requestStoragePermission();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PIC_GALLERY_REQUIST) {
                imgURI = data.getData();
                String imgNAmeS = "",schema;
                schema=imgURI.getScheme();
                Log.i("Schema",schema+" ");
                if(schema.equals("file")){imgNAmeS=imgURI.getLastPathSegment();}
                else if(schema.equals("content")){String [] i={MediaStore.Images.Media.TITLE};
                Cursor c= getContentResolver().query(imgURI,i,null,null,null);
                if(c!=null && c.getCount()!=0){ int colindx=c.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                c.moveToFirst();
                imgNAmeS=c.getString(colindx);
                ImgNameTextView.setText(imgNAmeS);}
                if(c!=null){
                    c.close();
                }
                }
                Log.i("sesee",imgNAmeS+" hi");
                if (imgNAmeS.length()!=0){
                   // onCliked=true;
                }
            }
            else{
                Toast.makeText(report_problem.this,"No img selected",Toast.LENGTH_LONG ).show();
            }

        }


    }

    public void isChosed(View view ){
        int radioId=rg.getCheckedRadioButtonId();
        problemType=findViewById(radioId);
        ProbType=(String) problemType.getText();
        moreDetails.setHint("الوصف");
        if(problem_type_more.isChecked()){
            moreDetails.setEnabled(true);
            moreDetails.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    dis=moreDetails.getText().toString();
                    return false;
                }
            });
        } else { moreDetails.setEnabled(false);}


    }

private  void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this).setTitle("بحاجة للوصول").setMessage("بحاجة للوصول الى الصور").setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ActivityCompat.requestPermissions(report_problem.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, storagePermCode);

                }
            }).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, storagePermCode);
        }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      if(requestCode== storagePermCode){
          if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
              Toast.makeText(this,"تم الوصول", Toast.LENGTH_SHORT).show();
          }else{
              Toast.makeText(this,"لم يتم الوصول الى الصور",Toast.LENGTH_SHORT);
          }
      }
    }

    // this function to set the size of radio buttons accroading to the screen size.
public void setRadioButtons_Sizes(int text_size, float scale_size, int padding_right){

        for(RadioButton r : array_of_problems){
            r.setPadding(0,0,padding_right,0);
            r.setTextSize(TypedValue.COMPLEX_UNIT_SP,text_size);
            r.setScaleX(scale_size);
            r.setScaleY(scale_size);
        }
}// end setRadioButtons_Sizes() function

}
