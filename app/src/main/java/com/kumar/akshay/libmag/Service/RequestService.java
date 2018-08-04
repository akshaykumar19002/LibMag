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
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

public class RequestService extends IntentService {

    public static String TAG = "RequestService";
    DatabaseReference databaseReference;
    LibMagDBHelper libMagDB;

    public RequestService() {
        super("requestService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        libMagDB = new LibMagDBHelper(RequestService.this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_req");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UsersObject usersObject = dataSnapshot.getValue(UsersObject.class);
                if (usersObject != null) {
                    if (usersObject.getType() == 1) {
                        //libMagDB.insertIntoStudentsTable(dataSnapshot.getKey(), usersObject.getName(), usersObject.getEmail(), usersObject.getPass(), usersObject.getRollno(), usersObject.getCourse(), usersObject.getBranch(), usersObject.getIssuedBooks());
                        libMagDB.insertUserRequestIntoRequestTable(dataSnapshot.getKey(), usersObject.getName(), usersObject.getEmail(), usersObject.getPass(), usersObject.getRollno(), usersObject.getCourse(), usersObject.getBranch());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UsersObject usersObject = dataSnapshot.getValue(UsersObject.class);
                if (usersObject != null) {
                    libMagDB.removeARequest(dataSnapshot.getKey(), usersObject.getRollno());
                }
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
