package com.kumar.akshay.libmag.Student;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

public class AboutStudentFragment extends Fragment {

    TextView aboutStudentTextView;
    FirebaseUser firebaseUser;
    LibMagDBHelper libMagDb;
    boolean isStudFrag = true;
    ProgressBar mProgressBar;
    DatabaseReference databaseReference;
    public static UsersObject usersObject = null;
    public static String user_key = null;
    Button changePwdBtn, deleteAccountBtn;
    Context context;
    UsersObject currentUser = null;
    RelativeLayout aboutStudentView;

    public AboutStudentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        libMagDb = new LibMagDBHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view1 = inflater.inflate(R.layout.fragment_about_student, container, false);
        context = getContext();
        isStudFrag = getArguments().getBoolean("studFrag");
        changePwdBtn = view1.findViewById(R.id.buttonChangePassword);
        aboutStudentView = view1.findViewById(R.id.aboutStudentRelativeLayout);
        deleteAccountBtn = view1.findViewById(R.id.buttonDeleteAccount);
        mProgressBar = view1.findViewById(R.id.aboutProgressBar);
        aboutStudentTextView = view1.findViewById(R.id.textViewAboutStudent);
        if (firebaseUser != null) {
            if (isStudFrag) {
                usersObject = libMagDb.getAStudent(libMagDb.getKeyFromStudentsTable(libMagDb.getARollno(firebaseUser.getEmail())));
                aboutStudentTextView.setText("Rollno : " + usersObject.getRollno() + "\n" +
                        "Name : " + usersObject.getName() + "\n" +
                        "Course : " + usersObject.getCourse() + "\n" +
                        "Branch : " + usersObject.getBranch() + "\n" +
                        "Email : " + usersObject.getEmail());
                changePwdBtn.setVisibility(View.VISIBLE);
                deleteAccountBtn.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            } else {
                getLibrarianDetails(firebaseUser.getEmail());
                changePwdBtn.setVisibility(View.VISIBLE);
                deleteAccountBtn.setVisibility(View.GONE);
            }
        } else {
            changePwdBtn.setVisibility(View.GONE);
            deleteAccountBtn.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            Snackbar.make(view1, "Unknown error", Snackbar.LENGTH_LONG).show();
        }

        changePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view3) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View view2 = inflater.inflate(R.layout.change_password_dialog, null);
                final EditText newPassET, cnfNewPassET;
                newPassET = view2.findViewById(R.id.newPasswordET);
                cnfNewPassET = view2.findViewById(R.id.confirmNewPasswordET);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view2);
                builder.setTitle("Change Password");
                builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.updatePassword(newPassET.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (cnfNewPassET.getText().toString().equals(newPassET.getText().toString())) {
                                            databaseReference.child(libMagDb.getKeyFromStudentsTable(usersObject.getRollno())).removeValue();
                                            usersObject.setPass(newPassET.getText().toString());
                                            databaseReference.push().setValue(usersObject);
                                            Snackbar.make(view1, "Password Changed", Snackbar.LENGTH_LONG).show();
                                            startActivity(new Intent(getContext(), MainScreen.class));
                                        } else
                                            cnfNewPassET.setError("Password does not match");
                                    }
                                });
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                builder.create();
            }
        });

        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view4) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                if (!checkFine()) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar.make(view1, "Account Deleted", Snackbar.LENGTH_LONG).show();
                                        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email")
                                                .equalTo(email).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                UsersObject student = dataSnapshot.getValue(UsersObject.class);
                                                if (student != null) {
                                                    if (student.getRollno() != null) {
                                                        FirebaseDatabase.getInstance().getReference().child("users").child(dataSnapshot.getKey()).removeValue();
                                                    }
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
                                        startActivity(new Intent(getContext(), MainScreen.class));
                                    }
                                }
                            });
                } else {
                    Snackbar.make(aboutStudentView, "You currently have some issued books", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        return view1;
    }

    private void getLibrarianDetails(final String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UsersObject usersObjects = dataSnapshot.getValue(UsersObject.class);
                if (usersObjects.getType() == 0) {
                    if (usersObjects.email != null) {
                        if (usersObjects.getEmail().equals(email)) {
                            user_key = dataSnapshot.getKey();
                            usersObject = usersObjects;
                            aboutStudentTextView.setText("Staff Id : " + usersObject.getRollno() + "\n" +
                                    "Name : " + usersObject.getName() + "\n" +
                                    "Email : " + usersObject.getEmail());
                            mProgressBar.setVisibility(View.GONE);
                        } else {
                            usersObject = null;
                        }
                    } else usersObject = null;
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

    boolean checkFine() {
        LibMagDBHelper libMagDB = new LibMagDBHelper(context);
        final UsersObject userObject = usersObject;
        if (userObject != null)
            if (libMagDB.verifyRollno(usersObject.getRollno())) {
                //checks fine for the specific student
                UsersObject usersObject = libMagDB.getAStudent(libMagDB.getKeyFromStudentsTable(userObject.getRollno()));
                int fine = 0;
                String[] books = usersObject.getIssuedBooks().split(",");
                if (!books[0].equals(null)) {
                    return true;
                } else
                    return false;
            } else {
                return false;
            }
        return true;
    }
}
