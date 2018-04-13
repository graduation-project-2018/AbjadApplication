package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class educator_home extends menu_educator {
    menu_variables m = new menu_variables();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_educator_home);

        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الرئيسية");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_educator_home, null, false);

        mDrawerLayout.addView(contentView, 0);

        String unit_id = "unit1";



        final Button btn = (Button) findViewById(R.id.add_new_child_btn);
        final firebase_connection r = new firebase_connection();
        /*r.ref.child("Units").child("unit2").child("unit letters").child("L1").setValue("ص");
        r.ref.child("Units").child("unit2").child("unit letters").child("L2").setValue("ف");
        r.ref.child("Units").child("unit2").child("unit letters").child("L3").setValue("س");
        r.ref.child("Units").child("unit2").child("unit letters").child("L4").setValue("ق");
        r.ref.child("Units").child("unit2").child("unit letters").child("L5").setValue("ت");
        r.ref.child("Units").child("unit2").child("unit letters").child("L6").setValue("ح");

        r.ref.child("Units").child("unit2").child("unit pic").setValue("-");
        r.ref.child("Units").child("unit2").child("unit title").setValue("مدرستي");*/




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          String id = SigninEducator.id_edu;
          String id_child = Signin.id_child;

            }
        });






    }
}
