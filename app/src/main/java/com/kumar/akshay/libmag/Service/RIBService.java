package com.kumar.akshay.libmag.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.ObjectClasses.RIBObject;

public class RIBService extends IntentService {

    DatabaseReference databaseReference;
    LibMagDBHelper libMagDB;

    public RIBService() {
        super("ribService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        libMagDB = new LibMagDBHelper(RIBService.this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("rib");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RIBObject ribObject = dataSnapshot.getValue(RIBObject.class);
                if (ribObject != null){
                    libMagDB.insertIntoRIBTable(dataSnapshot.getKey(), ribObject.getRollno(), ribObject.getBook_id(), ribObject.getBook_name(), ribObject.getBook_issue_date(), ribObject.getBook_return_date());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                libMagDB.removeARIB(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
