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

public class StudentsService extends IntentService {

    public static String TAG = "StudentsService";
    DatabaseReference databaseReference;
    LibMagDBHelper libMagDB;

    public StudentsService() {
        super("StudentsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        libMagDB = new LibMagDBHelper(StudentsService.this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UsersObject usersObject = dataSnapshot.getValue(UsersObject.class);
                if (usersObject != null) {
                    if (usersObject.getType() == 1) {
                        libMagDB.insertIntoStudentsTable(dataSnapshot.getKey(), usersObject.getName(), usersObject.getEmail(), usersObject.getPass(), usersObject.getRollno(), usersObject.getCourse(), usersObject.getBranch(), usersObject.getIssuedBooks());
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
                    libMagDB.removeAStudent(dataSnapshot.getKey(), usersObject.getRollno());
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
