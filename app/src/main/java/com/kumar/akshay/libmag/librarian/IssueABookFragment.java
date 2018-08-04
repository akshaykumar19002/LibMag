package com.kumar.akshay.libmag.librarian;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.Barcode.ScanBarcodeActivity;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.ObjectClasses.RIBObject;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class IssueABookFragment extends DialogFragment {

    TextView mainTextView;
    AutoCompleteTextView bookIdET = null, rollNOET = null;
    public static String bookId = "", rollNo = "";
    boolean isIssueFragment = true;
    DatabaseReference ref1, ref2;
    View view;
    Button scanBarCodeButton;

    public IssueABookFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_issue_a_book, null);
        scanBarCodeButton = view.findViewById(R.id.scanBarCodeButton);
        LibrarianActivity.isBasicFragment = false;
        final LibMagDBHelper libMagDB = new LibMagDBHelper(getContext());
        ref1 = FirebaseDatabase.getInstance().getReference().child("books");
        ref2 = FirebaseDatabase.getInstance().getReference().child("users");
        Bundle bundle = getArguments();
        if (bundle != null) {
            isIssueFragment = bundle.getBoolean("issueFrag");
        }
        mainTextView = view.findViewById(R.id.mainTextView);
        bookIdET = view.findViewById(R.id.bookIdET);
        rollNOET = view.findViewById(R.id.rollnoET);
        bookIdET.setText("");
        rollNOET.setText("");
        ArrayAdapter<String> bookIds = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, libMagDB.getAllBookIds());
        bookIdET.setAdapter(bookIds);
        ArrayAdapter<String> rollNos = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, libMagDB.getAllRollno());
        rollNOET.setAdapter(rollNos);
        builder.setView(view);
        if (!isIssueFragment) {
            mainTextView.setText("Return Book");
            rollNOET.setVisibility(View.GONE);
            builder.setPositiveButton("Return Book", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //barCode.printBarCode(barCodeString);
                    //Issues a book specified by the user by first checking if the user have entered the correct id or not and
                    //then updating the firebase real time database
                    bookId = bookIdET.getText().toString();
                    rollNo = rollNOET.getText().toString();
                    //Returning a book
                    int result = returnBook(getContext(), libMagDB, ref1, ref2, bookId, rollNo, view);
                    if (result == 0) {
                        getActivity().onBackPressed();
                    } else {
                        Snackbar.make(view, "", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            mainTextView.setText("Issue Book");
            rollNOET.setVisibility(View.VISIBLE);
            builder.setPositiveButton("Issue Book", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    bookId = bookIdET.getText().toString();
                    rollNo = rollNOET.getText().toString();
                    if (validateData(bookId, rollNo)) {
                        if (libMagDB.verifyBookId(bookId) && libMagDB.verifyRollno(rollNo)) {
                            if (IssueABookFragment.issueBook(libMagDB, ref1, ref2, bookId, rollNo, view)) {
                                getActivity().onBackPressed();
                            }
                        } else {
                            bookIdET.setError("Invalid bookId");
                            bookIdET.requestFocus();
                        }
                    }
                }
            });
        }
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                IssueABookFragment.this.getDialog().cancel();
            }
        });
        scanBarCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanBarcodeActivity.class);
                if (isIssueFragment) {
                    intent.putExtra("textViewText", "Firstly scan book id then scan roll no");
                    intent.putExtra("req_code", 0);
                } else {
                    intent.putExtra("textViewText", "Scan book id");
                    intent.putExtra("req_code", 1);
                }
                startActivity(intent);
            }
        });
        return builder.create();
    }

    private boolean validateData(String bookId, String rollNo) {
        boolean valid = true;
        if (rollNo.equals("")) {
            rollNOET.setError("Invalid rollno");
            rollNOET.requestFocus();
            valid = false;
        }
        if (TextUtils.isEmpty(bookId)) {
            bookIdET.setError("Invalid Id");
            bookIdET.requestFocus();
            valid = false;
        }
        return valid;
    }

    public static boolean issueBook(LibMagDBHelper libMagDB, DatabaseReference ref1, DatabaseReference ref2, String bookId, String rollNo, View view) {
        BookMessage bookMessage = libMagDB.getABook(bookId);
        UsersObject usersObject = libMagDB.getAStudent(libMagDB.getKeyFromStudentsTable(rollNo));
        DatabaseReference ribRef = FirebaseDatabase.getInstance().getReference().child("rib");
        String [] input = bookMessage.getBookIssuedTo().split(":");
        Calendar calendar = GregorianCalendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        if (bookMessage.getBookIssueDate().equals("NIL")) {
            bookMessage.setBookIssueDate(format.format(calendar.getTime()));
            bookMessage.setBookIssuedTo(usersObject.getRollno() + ":14");
            String issued_books;
            if (usersObject.getIssuedBooks().equals("NIL")) {
                issued_books = Integer.toString(bookMessage.getBookId());
            } else
                issued_books = usersObject.getIssuedBooks() + "," + Integer.toString(bookMessage.getBookId());
            usersObject.setIssuedBooks(issued_books);

            RIBObject ribObject = new RIBObject(rollNo, Integer.toString(bookMessage.getBookId()), bookMessage.getBookName(), bookMessage.getBookIssueDate(), "NIL");
            ref1.child(libMagDB.getKeyFromBooksTable(bookId)).removeValue();
            ref2.child(libMagDB.getKeyFromStudentsTable(rollNo)).removeValue();
            ref1.push().setValue(bookMessage);
            ref2.push().setValue(usersObject);
            ribRef.push().setValue(ribObject);

            return true;
        } else if (input[0].equals("Reserved") && rollNo.equals(input[1])) {
            bookMessage.setBookIssueDate(format.format(calendar.getTime()));
            bookMessage.setBookIssuedTo(usersObject.getRollno() + ":14");
            String issued_books;
            if (usersObject.getIssuedBooks().equals("NIL")) {
                issued_books = Integer.toString(bookMessage.getBookId());
            } else
                issued_books = usersObject.getIssuedBooks() + "," + Integer.toString(bookMessage.getBookId());
            usersObject.setIssuedBooks(issued_books);

            RIBObject ribObject = new RIBObject(rollNo, Integer.toString(bookMessage.getBookId()), bookMessage.getBookName(), bookMessage.getBookIssueDate(), "NIL");
            ref1.child(libMagDB.getKeyFromBooksTable(bookId)).removeValue();
            ref2.child(libMagDB.getKeyFromStudentsTable(rollNo)).removeValue();
            ref1.push().setValue(bookMessage);
            ref2.push().setValue(usersObject);
            ribRef.push().setValue(ribObject);

            return true;
        } else {
            Snackbar.make(view, "Book already issued", Snackbar.LENGTH_LONG).show();
            return false;
        }
    }

    public int returnBook(final Context context, final LibMagDBHelper libMagDB, final DatabaseReference databaseReference, final DatabaseReference databaseReference2, final String bookId, final String rollno, View view) {
        final DatabaseReference ribRef = FirebaseDatabase.getInstance().getReference().child("rib");
        final String book_key = libMagDB.getKeyFromBooksTable(bookId);
        final BookMessage bookMessage = libMagDB.getABook(bookId);
        Calendar calendar = GregorianCalendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        if (bookMessage.getBookIssuedTo() != "NIL") {
            String some[] = bookMessage.getBookIssuedTo().split(":");
            final String stud_rollno = some[0];
            final String student_key = libMagDB.getKeyFromStudentsTable(stud_rollno);
            final UsersObject usersObject = libMagDB.getAStudent(student_key);
            String issued_books[] = usersObject.getIssuedBooks().split(",");
            if (issued_books.length == 1) {
                usersObject.setIssuedBooks("NIL");
            } else {
                String issued = "";
                for (int j = 0; j < issued_books.length; j++) {
                    if (issued_books[j].equals(Integer.toString(bookMessage.getBookId()))) {
                        issued_books[j] = null;
                    } else {
                        if (issued.equals("")) {
                            issued += issued_books[j];
                        } else
                            issued += "," + issued_books[j];
                    }
                }
                usersObject.setIssuedBooks(issued);
            }
            bookMessage.setBookIssueDate("NIL");
            bookMessage.setBookIssuedTo("NIL");
            final RIBObject ribObject = libMagDB.getARIB(stud_rollno, bookId);
            ribObject.setBook_return_date(format.format(calendar.getTime()));
            int fine = Integer.parseInt(libMagDB.getFineForBook(bookId));
            if (fine <= 0) {
                databaseReference2.child(student_key).removeValue();
                databaseReference.child(book_key).removeValue();
                ribRef.child(libMagDB.getKeyFromRIBTable(stud_rollno, bookId)).removeValue();
                databaseReference2.push().setValue(usersObject);
                databaseReference.push().setValue(bookMessage);
                ribRef.push().setValue(ribObject);
                return 0;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Please pay the book fine first")
                        .setPositiveButton("Pay Fine", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ribRef.child(libMagDB.getKeyFromRIBTable(stud_rollno, bookId)).removeValue();
                                databaseReference2.child(student_key).removeValue();
                                databaseReference.child(book_key).removeValue();
                                databaseReference2.push().setValue(usersObject);
                                databaseReference.push().setValue(bookMessage);
                                context.startActivity(new Intent(context, MainScreen.class));
                                ribRef.push().setValue(ribObject);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.show();
            }
        } else {
            return -1;
        }
        return -1;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bookIdET != null) {
            bookIdET.setText("");
            rollNOET.setText("");
            if (isIssueFragment) {
                bookIdET.setText(bookId);
                rollNOET.setText(rollNo);
            } else {
                bookIdET.setText(bookId);
            }
        }
    }
}
