package com.kumar.akshay.libmag.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;
import com.kumar.akshay.libmag.Service.BooksService;
import com.kumar.akshay.libmag.Service.StudentsService;

public class RegisterTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private int type;
    private String name, email, pass, rollno, course = "", branch = "";
    private UsersObject usersObject;
    View view;
    DatabaseReference databaseReference;

    public RegisterTask(Context context, View view, int type, String name, String email, String pass, String rollno, String course, String branch) {
        this.context = context;
        this.view = view;
        this.type = type;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.rollno = rollno;
        this.course = course;
        this.branch = branch;
        usersObject = new UsersObject(type, name, email, pass, rollno, course, branch, "NIL");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.v("MainActivity", "Name : " + name + " Email : " + email + " Pass : " + pass + " Rollno : " + rollno);
        if (name.equals("") || email.equals("") || pass.equals("") || rollno.equals("")) {
            return false;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_req");
        databaseReference.push().setValue(usersObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Request for new user have been done Contact your librarian for completing the login procedure", Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context, MainScreen.class));
                new LibMagDBHelper(context).recreateTables();
                context.startService(new Intent(context, BooksService.class));
                context.startService(new Intent(context, StudentsService.class));
                //MainScreen.updatingPreferences(type, email, pass);
                Log.v("MainActivity", "Database updated");
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
            }
        });
//        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                FirebaseUser firebaseUser = task.getResult().getUser();
//                if (firebaseUser != null) {
//                    FirebaseDatabase.getInstance().getReference().child("users").push().setValue(usersObject);
        //MainActivity.showProgress(context, false);
//                }
//            }
//        });

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}