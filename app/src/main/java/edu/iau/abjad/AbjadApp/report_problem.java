package edu.iau.abjad.AbjadApp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.util.HashMap;

public class report_problem extends child_menu {
    public static final int PIC_GALLERY_REQUIST = 20;
    menu_variables m = new menu_variables();
    private Button uplodImgbtn;
    private Button reportBtn;
    private StorageReference mStorge;
    private TextView ImgNameTextView;
    private firebase_connection report;
    private String daownloadURL;
    private RadioGroup  rg;
    private RadioButton problemType;
    private String ProbType;
    private String dis;
    private EditText moreDetails;
    private HashMap<String,String> dataMap;
    private Uri imgURI;
    private boolean uploded=false;
    private int storagePermCode=1;
    private ProgressDialog mProgressDialog;
    private boolean onCliked=false;
    private Bundle email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m.title = findViewById(R.id.interface_title);

        m.title.setText("إبلاغ عن مشكلة");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_report_problem, null, false);
        myDrawerLayout.addView(contentView, 0);
        //initialization
        uplodImgbtn = findViewById(R.id.uplodBtn);
        reportBtn= findViewById(R.id.reportButn );
        ImgNameTextView= findViewById(R.id.imgname);
        rg= findViewById(R.id.radioButtonsGroup);
        dataMap=new HashMap<String, String>();
        dis="-";
        moreDetails=findViewById(R.id.moredetails);
        mProgressDialog=new ProgressDialog(this);
        //firbase storge refrance
        mStorge = FirebaseStorage.getInstance().getReference();
        report=new firebase_connection();
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImgNameTextView.getText().toString().length()!=0){onCliked=true;}
                if(onCliked){
                if (rg.getCheckedRadioButtonId()!=-1){
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
                                        email=getIntent().getExtras();
                                        String semail=email.getString("email");
                                        mProgressDialog.dismiss();
                                        dataMap.put("screenShots", daownloadURL);
                                        dataMap.put("report_type", ProbType);
                                        dataMap.put("report_description", dis);
                                        dataMap.put("reporter_email",semail);
                                        report.ref.child("Reports").push().setValue(dataMap);
                                        Toast.makeText(report_problem.this, "شكرا، تم تلقي البلاغ!", Toast.LENGTH_LONG).show();
                                        rg.clearCheck();
                                        ImgNameTextView.setText("");
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
                    Toast.makeText(report_problem.this,"الرجاء ارفاق التقاطة الشاشة للمشكلة",Toast.LENGTH_LONG).show();

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
        RadioButton more=findViewById(R.id.more);
        moreDetails.setHint("الوصف");
        if(more.isChecked()){
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
            }).setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
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



}
