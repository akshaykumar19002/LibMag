package com.kumar.akshay.libmag.librarian;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.R;

public class EditBookDetailsFragment extends DialogFragment {

    EditText bookName, bookAuthorName, bookPublisherName, bookEdition, bookDescription;
    LibMagDBHelper libMagDB;
    BookMessage bookMessage;
    DatabaseReference databaseReference;

    public EditBookDetailsFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LibrarianActivity.isBasicFragment = false;
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        libMagDB = new LibMagDBHelper(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
        View view = layoutInflater.inflate(R.layout.fragment_edit_details_dialog, null, false);
        bookName = view.findViewById(R.id.editTextBookName);
        bookAuthorName = view.findViewById(R.id.editTextAuthorName);
        bookPublisherName = view.findViewById(R.id.editTextBookPublisher);
        bookEdition = view.findViewById(R.id.editTextBookEdition);
        bookDescription = view.findViewById(R.id.editTextBookDescription);
        bookMessage = libMagDB.getABook(getArguments().getString("bookId"));
        bookName.setText(bookMessage.getBookName());
        bookAuthorName.setText(bookMessage.getBookAuthor());
        bookPublisherName.setText(bookMessage.getBookPublisher());
        bookEdition.setText(bookMessage.getBookEdition());
        bookDescription.setText(bookMessage.getBookDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Edit Details")
                .setMessage("Fill only those details which needs to be edited and keep others unchanged")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bookMessage.setBookName(bookName.getText().toString());
                        bookMessage.setBookAuthor(bookAuthorName.getText().toString());
                        bookMessage.setBookEdition(bookEdition.getText().toString());
                        bookMessage.setBookPublisher(bookPublisherName.getText().toString());
                        bookMessage.setBookDescription(bookDescription.getText().toString());
                        String book_key = libMagDB.getKeyFromBooksTable(Integer.toString(bookMessage.getBookId()));
                        databaseReference.child(book_key).removeValue();
                        databaseReference.push().setValue(bookMessage);
                        getContext().startActivity(new Intent(getContext(), MainScreen.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        return builder.create();
    }
}
