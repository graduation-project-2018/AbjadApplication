package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

public class report_problem extends menu_educator {
    public static final int PIC_GALLERY_REQUIST = 20;
    menu_variables m = new menu_variables();
    private Button uplodImgbtn;
    private Button reportBtn;
    private StorageReference mStorge;
    private TextView ImgNameTextView;
    private  DatabaseReference report;
    private  String daownloadURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m.title = (TextView) findViewById(R.id.interface_title);

        m.title.setText("إبلاغ عن مشكلة");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_report_problem, null, false);
        mDrawerLayout.addView(contentView, 0);

        uplodImgbtn = findViewById(R.id.uplodBtn);
        reportBtn= findViewById(R.id.reportButn );
        ImgNameTextView=(TextView) findViewById(R.id.imgname);
        //firbase storge refrance
        mStorge = FirebaseStorage.getInstance().getReference();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        report=ref.child("Reports");
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void onImgGalleryCliked(View view) {
        Intent picChooserIntent = new Intent(Intent.ACTION_PICK);
        File picDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picDirPath = picDirectory.getPath();
        Uri data = Uri.parse(picDirPath);
        picChooserIntent.setDataAndType(data, "image/*");
        startActivityForResult(picChooserIntent, PIC_GALLERY_REQUIST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PIC_GALLERY_REQUIST) {
                final Uri imgURI = data.getData();
                final StorageReference filePath = mStorge.child("ReportProblemScreenshots").child(imgURI.getLastPathSegment());
                StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = filePath.putFile(imgURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(report_problem.this, "تم رفع الصورة", Toast.LENGTH_LONG).show();
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUri=uri;
                                daownloadURL=downloadUri.toString();
                            }
                        });
                        ImgNameTextView.setText(filePath.getName());
                        report.push().child("screenshot_url").setValue(daownloadURL);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(report_problem.this, "خطأ في رفع الصورة", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

}
