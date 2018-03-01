package edu.iau.abjad.AbjadApp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class firebase_connection {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

}
