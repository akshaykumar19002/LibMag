package com.kumar.akshay.libmag.librarian.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

public class ShowASpecificUserRequestFragment extends Fragment {

    String rollno, name, email, pass, course, branch;
    EditText rollnoEditText, nameEditText, emailEditText, passEditText, courseEditText, branchEditText;
    boolean getData;
    Button acceptRequestBtn, rejectRequestBtn;
    DatabaseReference databaseReference1, databaseReference2;
    LibMagDBHelper libMagDb;
    View view;
    String key;

    public ShowASpecificUserRequestFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_a_specific_user, null);
        LibrarianActivity.isBasicFragment = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            rollno = bundle.getString("rollno");
            name = bundle.getString("name");
            email = bundle.getString("email");
            pass = bundle.getString("pass");
            course = bundle.getString("course");
            branch = bundle.getString("branch");
            getData = true;
        } else
            getData = false;

        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("user_req");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users");
        libMagDb = new LibMagDBHelper(getContext());
        rollnoEditText = view.findViewById(R.id.editTextRollno);
        nameEditText = view.findViewById(R.id.editTextName);
        emailEditText = view.findViewById(R.id.editTextEmail);
        passEditText = view.findViewById(R.id.editTextPass);
        courseEditText = view.findViewById(R.id.editTextCourse);
        branchEditText = view.findViewById(R.id.editTextBranch);
        acceptRequestBtn = view.findViewById(R.id.buttonAcceptRequest);
        rejectRequestBtn = view.findViewById(R.id.buttonRejectRequest);
        key = libMagDb.getAKeyFromUserRequest(rollno);
        if (getData){
            rollnoEditText.setText(rollno);
            nameEditText.setText(name);
            emailEditText.setText(email);
            passEditText.setText(pass);
            courseEditText.setText(course);
            branchEditText.setText(branch);
        }

        acceptRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final UsersObject student = new UsersObject(1, name, email, pass, rollno, course, branch, "NIL");
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (key != null) {
                            databaseReference1.child(key).removeValue();
                            databaseReference2.push().setValue(student);
                            Toast.makeText(getContext(), "User request accepted", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getContext(), MainScreen.class));
                        }else
                            Toast.makeText(getContext(), "Unknwon error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        rejectRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (key != null) {
                    databaseReference1.child(key).removeValue();
                    startActivity(new Intent(getContext(), MainScreen.class));
                    Toast.makeText(getContext(), "User request rejected", Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(getContext(), "Unknown error", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
