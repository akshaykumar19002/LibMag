package com.kumar.akshay.libmag.librarian.AddBook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.Barcode.NewBarCodeDialogFragment;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.librarian.BooksFragment;

import java.util.Random;

public class AddBookFragment extends Fragment {

    public AddBookFragment() {
    }

    EditText bookNameET, bookAuthorET, bookPublisherET, bookEditionET, bookDescriptionET, bookLocationET;
    Button addBookButton, cancelButton;
    String bookName, bookAuthor, bookPublisher, bookEdition, bookDescription, bookLocation;
    public static int bookId;
    DatabaseReference databaseReference;
    static boolean valid = true;
    Random random;
    LibMagDBHelper libMagDB;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);
        random = new Random();
        context = getContext();
        libMagDB = new LibMagDBHelper(context);
        bookNameET = view.findViewById(R.id.bookNameEditText);
        bookAuthorET = view.findViewById(R.id.bookAuthorEditText);
        bookPublisherET = view.findViewById(R.id.bookPublisherEditText);
        bookEditionET = view.findViewById(R.id.bookEditionEditText);
        bookDescriptionET = view.findViewById(R.id.bookDescriptionEditText);
        bookLocationET = view.findViewById(R.id.bookLocationEditText);
        addBookButton = view.findViewById(R.id.addBookBtn);
        cancelButton = view.findViewById(R.id.cancelButton);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookName = bookNameET.getText().toString();
                bookAuthor = bookAuthorET.getText().toString();
                bookPublisher = bookPublisherET.getText().toString();
                bookEdition = bookEditionET.getText().toString();
                bookDescription = bookDescriptionET.getText().toString();
                bookLocation = bookLocationET.getText().toString();
                if (validateData(bookName, bookAuthor, bookPublisher, bookEdition, bookDescription, bookLocation)) {
                    bookId = getBookIddd();
                    BookMessage bookMessage1 = new BookMessage(bookId, bookName, bookAuthor, bookPublisher, "NIL", bookEdition, bookDescription, "NIL", bookLocation);
                    databaseReference.push().setValue(bookMessage1);
                    DialogFragment dialogFragment = new NewBarCodeDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("barCode", bookId);
                    bundle.putString("type", "book");
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), "newbarcodedialogfragment");
                    //startActivity(new Intent(getContext(), MainScreen.class));
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Fragment fragment2 = new BooksFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("searchFrag", false);
                    bundle.putString("mUserEmail", user.getEmail());
                    bundle.putString("mUid", user.getUid());
                    fragment2.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    //ft.remove(fragment2);
                    ft.replace(R.id.fragmentLibrarian, fragment2);
                    ft.commit();
                    //ft.add(R.id.fragmentLibrarian, fragment2);
                }
            }
        });
        return view;
    }

    private int getBookIddd() {
        bookId = random.nextInt(999999);
        while (libMagDB.verifyBookId(Integer.toString(bookId))){
            while (!(bookId >= 100000)){
                bookId = random.nextInt(999999);
            }
        }
        return bookId;
    }

    private boolean validateData(String bookName, String bookAuthor, String bookPublisher, String bookEdition, String bookDescription, String location) {
        valid = true;
        if (bookDescription.equals("")) {
            valid = false;
            bookDescriptionET.setError("Invalid book description");
            bookDescriptionET.requestFocus();
        }
        if (bookEdition.equals("")) {
            valid = false;
            bookEditionET.setError("Invalid book edition");
            bookEditionET.requestFocus();
        }
        if (bookPublisher.equals("")) {
            valid = false;
            bookPublisherET.setError("Invalid book publisher");
            bookPublisherET.requestFocus();
        }
        if (bookAuthor.equals("")) {
            valid = false;
            bookAuthorET.setError("Invalid book author");
            bookAuthorET.requestFocus();
        }
        if (bookName.equals("")) {
            valid = false;
            bookNameET.setError("Invalid book name");
            bookNameET.requestFocus();
        }
        if (location.isEmpty()){
            valid = false;
            bookLocationET.setError("Invalid rack number");
            bookLocationET.requestFocus();
        }
        return valid;
    }
}
