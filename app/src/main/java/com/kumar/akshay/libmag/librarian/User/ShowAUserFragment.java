package com.kumar.akshay.libmag.librarian.User;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

public class ShowAUserFragment extends Fragment {

    String rollno, name, email, pass, course, branch, issuedBooks;
    EditText rollnoEditText, nameEditText, emailEditText, passEditText, courseEditText, branchEditText;
    boolean getData;
    Button resetPasswordBtn, deleteAccountBtn, changePwdBtn;
    DatabaseReference databaseReference;
    LibMagDBHelper libMagDb;
    View view;
    String key;
    UsersObject student = null;

    public ShowAUserFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, null);
        LibrarianActivity.isBasicFragment = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            rollno = bundle.getString("rollno");
            name = bundle.getString("name");
            email = bundle.getString("email");
            pass = bundle.getString("pass");
            course = bundle.getString("course");
            branch = bundle.getString("branch");
            issuedBooks = bundle.getString("issuedBooks");
            student = new UsersObject(1, name, email, pass, rollno, course, branch, issuedBooks);
            getData = true;
        } else
            getData = false;

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        libMagDb = new LibMagDBHelper(getContext());
        rollnoEditText = view.findViewById(R.id.editTextRollno);
        nameEditText = view.findViewById(R.id.editTextName);
        emailEditText = view.findViewById(R.id.editTextEmail);
        passEditText = view.findViewById(R.id.editTextPass);
        courseEditText = view.findViewById(R.id.editTextCourse);
        branchEditText = view.findViewById(R.id.editTextBranch);
        changePwdBtn = view.findViewById(R.id.buttonChangePassword);
        resetPasswordBtn = view.findViewById(R.id.buttonResetPassword);
        deleteAccountBtn = view.findViewById(R.id.buttonDeleteAccount);
        key = libMagDb.getAKeyFromUserRequest(rollno);
        if (getData) {
            rollnoEditText.setText(rollno);
            nameEditText.setText(name);
            emailEditText.setText(email);
            passEditText.setText(pass);
            courseEditText.setText(course);
            branchEditText.setText(branch);
        }



        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = student.getEmail();

                auth.sendPasswordResetEmail(emailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(view, "Password Reset Email Sent", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }

}
