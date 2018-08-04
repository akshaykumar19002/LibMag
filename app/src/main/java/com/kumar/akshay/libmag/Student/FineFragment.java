package com.kumar.akshay.libmag.Student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;

public class FineFragment extends Fragment {

    TextView fineStatusTextView;
    Button payFineButton;
    ListView fineBooksListView;
    ProgressBar progressBarFineStatus;
    FineAdapter fineAdapter;
    UsersObject usersObject;
    String[] issued_books;
    LibMagDBHelper libMagDB;

    DatabaseReference databaseReference;
    String email, rollno;

    public FineFragment() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fine, null);
        LibrarianActivity.isBasicFragment = false;
        email = getArguments().getString("email");
        libMagDB = new LibMagDBHelper(getContext());
        rollno = libMagDB.getARollno(email);
        fineStatusTextView = view.findViewById(R.id.textViewFineStatus);
        payFineButton = view.findViewById(R.id.buttonPayFine);
        fineBooksListView = view.findViewById(R.id.fineBooksListView);
        progressBarFineStatus = view.findViewById(R.id.progressBarFineStatus);
        fineAdapter = new FineAdapter(getContext(), R.layout.fragment_issued_books_list);
        fineBooksListView.setAdapter(fineAdapter);
        checkFine();
        payFineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pays fine

            }
        });
        payFineButton.setVisibility(View.GONE);
        return view;
    }

    public void checkFine(){
        if (libMagDB.verifyRollno(rollno)) {
            //checks fine for the specific student
            usersObject = libMagDB.getAStudent(libMagDB.getKeyFromStudentsTable(rollno));
            int fine = 0;
            String[] books = usersObject.getIssuedBooks().split(",");
            if (!books[0].equals(null))
                for (String book_id : books) {
                    int f = Integer.parseInt(libMagDB.getFineForBook(book_id));
                    if (f > 0) {
                        fineAdapter.add(libMagDB.getABook(book_id));
                        fine += f;
                    }
                }
            if (fine <= 0) {
                fineStatusTextView.setText("No fine");
                progressBarFineStatus.setVisibility(View.GONE);
            } else {
                fineStatusTextView.setText("Fine : " + fine);
                progressBarFineStatus.setVisibility(View.GONE);
            }
        } else {
            fineStatusTextView.setText("Unknown error");
        }
    }

}
