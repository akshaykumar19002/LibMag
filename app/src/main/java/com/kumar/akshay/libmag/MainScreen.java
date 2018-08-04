package com.kumar.akshay.libmag;

import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.Student.StudentActivity;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;
import com.kumar.akshay.libmag.login.MainActivity;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

public class MainScreen extends AppCompatActivity {

    public static SharedPreferences emailSharedPreference, passwordSharedPreference, typeSharedPreference;
    String email = null, pass = null;
    int type = -1;
    TextView textViewMainScreen;
    ProgressBar progressBar;

    public MainScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        textViewMainScreen = (TextView) findViewById(R.id.textViewMainScreen);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMainScreen);
        creatingPreferences();
        requestPer();
        if (!email.equals("") && !pass.equals("") && type != -1) {
            AuthCredential authCredential = EmailAuthProvider.getCredential(email, pass);
            FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull final Task<AuthResult> task) {
                    if (task.isComplete() && throwingException(task, MainScreen.this)) {
                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                        databaseReference.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                textViewMainScreen.setText("Fetching Users");
                                Log.v("MainScreen", "Got a user object");
                                UsersObject usersObject = dataSnapshot.getValue(UsersObject.class);
                                type = usersObject.getType();
                                textViewMainScreen.setText("Verifying Email and Password Combination");
                                if (usersObject.getEmail().equals(email)) {
                                    textViewMainScreen.setText("Logging In");
                                    FirebaseUser firebaseUser = task.getResult().getUser();
                                    if (firebaseUser != null) {
                                        Log.v("MainScreen", "Got a firebaseuser object");
                                        Log.v("MainScreen", "type : " + type);
                                        switch (type) {
                                            case 0:
                                                Log.v("MainScreen", "Opening librarian activity");
                                                Intent intent = new Intent(MainScreen.this, LibrarianActivity.class);
                                                intent.putExtra("mUserEmail", firebaseUser.getEmail());
                                                intent.putExtra("mUid", firebaseUser.getUid());
                                                startActivity(intent);
                                                Log.v("MainScreen", "Login Successfull");
                                                return;
                                            case 1:
                                                Log.v("MainScreen", "Opening student activity");
                                                Intent intent1 = new Intent(MainScreen.this, StudentActivity.class);
                                                intent1.putExtra("mUserEmail", firebaseUser.getEmail());
                                                intent1.putExtra("mUid", firebaseUser.getUid());
                                                startActivity(intent1);
                                                Log.v("MainScreen", "Login Successfull");
                                                break;
                                        }
                                    } else
                                        Log.v("MainScreen", "Login failed");
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

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
            });
        } else {
            finish();
            startActivity(new Intent(MainScreen.this, MainActivity.class));
        }
    }

    private void requestPer() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        4);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        5);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    public boolean throwingException(Task<AuthResult> task, Context context) {
        boolean gotException = false;
        String message = "";
        try {
            throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            message = "Weak Password Entered";
        } catch (FirebaseAuthInvalidCredentialsException e) {
            message = "Invalid Login Credentials";
        } catch (FirebaseAuthUserCollisionException e) {
            message = "User collision error";
        } catch (FirebaseAuthInvalidUserException e) {
            message = "This user doesnot exist";
        } catch (Exception e) {
            message = "";
        } finally {
            if (!message.equals("")) {
                gotException = true;
                updatingPreferences(-1, "", "");
                finish();
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context, MainActivity.class));
            }
        }
        return !gotException;
    }

    public void creatingPreferences() {
        emailSharedPreference = getPreferences(Context.MODE_PRIVATE);
        passwordSharedPreference = getPreferences(Context.MODE_PRIVATE);
        typeSharedPreference = getPreferences(Context.MODE_PRIVATE);
        email = emailSharedPreference.getString("siginEmailId", "");
        pass = passwordSharedPreference.getString("siginPass", "");
        type = typeSharedPreference.getInt("siginType", -1);
    }

    public static void updatingPreferences(int type, String emailId, String password) {
        SharedPreferences.Editor editor = MainScreen.emailSharedPreference.edit();
        editor.putString("siginEmailId", emailId);
        editor.commit();
        editor = MainScreen.passwordSharedPreference.edit();
        editor.putString("siginPass", password);
        editor.commit();
        editor = MainScreen.typeSharedPreference.edit();
        editor.putInt("siginType", type);
        editor.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 4: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    requestPer();
                }
                return;
            }
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    requestPer();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
