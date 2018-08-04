package com.kumar.akshay.libmag.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.TokenWatcher;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.Service.BooksService;
import com.kumar.akshay.libmag.Service.NotificationService;
import com.kumar.akshay.libmag.Service.RIBService;
import com.kumar.akshay.libmag.Service.RequestService;
import com.kumar.akshay.libmag.Service.StudentsService;
import com.kumar.akshay.libmag.SplashScreen;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;

public class LoginAuthTask extends AsyncTask<Void, Void, Boolean> {

    Context context;
    String emailId, password;
    int userType;

    LoginAuthTask(Context context, int userType, String email, String password) {
        this.context = context;
        this.userType = userType;
        emailId = email;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            AuthCredential authCredential = EmailAuthProvider.getCredential(emailId, password);
            FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful() && new MainScreen().throwingException(task, context)) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        if (firebaseUser != null) {
                            MainScreen.updatingPreferences(userType, emailId, password);
                            new LibMagDBHelper(context).recreateTables();
                            context.startService(new Intent(context, NotificationService.class));
                            context.startService(new Intent(context, BooksService.class));
                            context.startService(new Intent(context, StudentsService.class));
                            context.startService(new Intent(context, RIBService.class));
                            context.startService(new Intent(context, RequestService.class));
                            context.startActivity(new Intent(context, MainScreen.class));
                            Log.v("MainActivity", "Login Successfull");
                        } else
                            Log.v("MainActivity", "Login failed");
                    }else {
                        new MainScreen().throwingException(task, context);
                    }
                }
            });
            return null;
        }catch (RuntimeExecutionException e){
            Toast.makeText(context, "Error occured", Toast.LENGTH_LONG);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

    }
}