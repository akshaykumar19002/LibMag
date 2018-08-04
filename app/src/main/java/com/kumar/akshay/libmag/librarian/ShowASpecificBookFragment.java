package com.kumar.akshay.libmag.librarian;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.R;

public class ShowASpecificBookFragment extends Fragment {

    public static int bookId;
    String bookName, bookAuthor, bookPublisher, bookEdition, bookDescription, bookIssueDate, bookLocation;
    TextView bookIdTextView, bookNameTextView, bookAuthorTextView, bookPublisherTextView, bookEditionTextView, bookDescriptionTextView, bookLocationTextView;
    boolean getData;
    Button issueBook, removeBook;
    DatabaseReference databaseReference1, databaseReference2;
    LibMagDBHelper libMagDb;
    View view;

    public ShowASpecificBookFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_a_specific_book, null);
        LibrarianActivity.isBasicFragment = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            bookId = bundle.getInt("bookId");
            bookName = bundle.getString("bookName");
            bookAuthor = bundle.getString("bookAuthor");
            bookPublisher = bundle.getString("bookPublisher");
            bookDescription = bundle.getString("bookDescription");
            bookEdition = bundle.getString("bookEdition");
            bookIssueDate = bundle.getString("issueDate");
            bookLocation = bundle.getString("bookLocation");
            getData = true;
        } else
            getData = false;
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("books");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users");
        libMagDb = new LibMagDBHelper(getContext());
        bookIdTextView = view.findViewById(R.id.textViewBookId);
        bookNameTextView = view.findViewById(R.id.textViewBookName);
        bookAuthorTextView = view.findViewById(R.id.textViewAuthorName);
        bookPublisherTextView = view.findViewById(R.id.textViewPublisherName);
        bookEditionTextView = view.findViewById(R.id.textViewEdition);
        bookDescriptionTextView = view.findViewById(R.id.textViewBookDescription);
        bookLocationTextView = view.findViewById(R.id.textViewLocation);
        issueBook = view.findViewById(R.id.buttonIssueBook);
        removeBook = view.findViewById(R.id.buttonRemoveBook);

        if (getData) {
            bookIdTextView.setText("Book Id : " + Integer.toString(bookId));
            bookNameTextView.setText("Book Name : " + bookName);
            bookAuthorTextView.setText("Author Name : " + bookAuthor);
            bookPublisherTextView.setText("Publisher : " + bookPublisher);
            bookEditionTextView.setText("Edition : " + bookEdition);
            bookDescriptionTextView.setText(bookDescription);
            bookLocationTextView.setText("Rack Number : "  + bookLocation);
            if (!bookIssueDate.equals("NIL")) {
                issueBook.setText("Return Book");
            }
        } else {
            bookIdTextView.setText("No data found");
            bookNameTextView.setText("No data found");
            bookAuthorTextView.setText("No data found");
            bookPublisherTextView.setText("No data found");
            bookEditionTextView.setText("No data found");
            bookDescriptionTextView.setText("No data found");
        }


        issueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookIssueDate.equals("NIL")) {
                    //Issuing a book by opening an alert dialog and getting the rollno to which the book is to be issued
                    IssueABookFragment issueBookDialogFragment = new IssueABookFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("issueFrag", true);
                    issueBookDialogFragment.setArguments(bundle);
                    issueBookDialogFragment.show(getFragmentManager(), "issuebook");
                } else {
                    //Retutn this book
                    int result = new IssueABookFragment().returnBook(getContext(), libMagDb, databaseReference1, databaseReference2, Integer.toString(bookId), "", view);
                    if (result == 0) {
                        getActivity().onBackPressed();
                    } else {
                        Snackbar.make(view, "", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
        removeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = libMagDb.getKeyFromBooksTable(Integer.toString(bookId));
                BookMessage bookMessgae = libMagDb.getABook(Integer.toString(bookId));
                if (bookMessgae.getBookIssueDate().equals("NIL")) {
                    databaseReference1.child(key).removeValue();
                    getActivity().onBackPressed();
                } else {
                    Snackbar.make(view, "Book alredy issued so it can be removed", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
}