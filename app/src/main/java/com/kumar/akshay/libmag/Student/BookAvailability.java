package com.kumar.akshay.libmag.Student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.Barcode.ScanBarcodeActivity;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BookAvailability extends Fragment {

    TextView tvbookAvailability;
    EditText etBookId = null, etBookName;
    Button btnCheckAvail, btnScan, btnReserveBook;
    public static String id = null;
    String name;
    LibMagDBHelper libMagDB;
    DatabaseReference databaseReference;

    public BookAvailability() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_availability, container, false);
        libMagDB = new LibMagDBHelper(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
        tvbookAvailability = view.findViewById(R.id.textViewBookAvailability);
        etBookId = view.findViewById(R.id.editTextBookId);
        etBookName = view.findViewById(R.id.editTextBookName);
        btnCheckAvail = view.findViewById(R.id.buttonBookAvail);
        btnScan = view.findViewById(R.id.buttonScanner);
        btnReserveBook = view.findViewById(R.id.buttonReserveBook);
        btnCheckAvail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = etBookId.getText().toString();
                name = etBookName.getText().toString();
                if (validate(id, name)) {
                    checkAvailability(id);
                }
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanBarcodeActivity.class);
                intent.putExtra("textViewText", "Scan book id to continue");
                intent.putExtra("req_code", 4);
                startActivity(intent);
            }
        });
        btnReserveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = etBookId.getText().toString();
                name = etBookName.getText().toString();
                if (validate(id, name)) {
                    BookMessage bookMessage = libMagDB.getABook(id);
                    UsersObject usersObject = libMagDB.getAStudentUsinEmail(StudentActivity.email);
                    if (btnReserveBook.getText().toString().toUpperCase().equals("RESERVE BOOK")) {
                        bookMessage.setBookIssuedTo("Reserved:" + usersObject.getRollno());
                        Calendar calendar = GregorianCalendar.getInstance();
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        bookMessage.setBookIssueDate(format.format(calendar.getTime()));
                        databaseReference.child(libMagDB.getKeyFromBooksTable(Integer.toString(bookMessage.getBookId()))).removeValue();
                        databaseReference.push().setValue(bookMessage);
                        btnReserveBook.setText("CANCEL RESERVATION");
                    } else if (btnReserveBook.getText().toString().toUpperCase().equals("CANCEL RESERVATION")) {
                        bookMessage.setBookIssuedTo("NIL");
                        bookMessage.setBookIssueDate("NIL");
                        databaseReference.child(libMagDB.getKeyFromBooksTable(Integer.toString(bookMessage.getBookId()))).removeValue();
                        databaseReference.push().setValue(bookMessage);
                        btnReserveBook.setText("RESERVE BOOK");
                    }
                }
            }
        });
        return view;
    }

    private void checkAvailability(String bookId) {
        tvbookAvailability.setVisibility(View.VISIBLE);
        BookMessage bookMessage = libMagDB.getABook(bookId);
        String[] issued_to = bookMessage.getBookIssuedTo().split(":");
        if (bookMessage.getBookIssuedTo().equals("NIL")) {
            tvbookAvailability.setText("Book not yet Issued");
            btnReserveBook.setVisibility(View.VISIBLE);
        } else if (issued_to[0].equals("Reserved")) {
            UsersObject usersObject = libMagDB.getAStudent(libMagDB.getKeyFromStudentsTable(issued_to[1]));
            tvbookAvailability.setText("Reserved to : " + "\n" +
                    "Rollno : " + usersObject.getRollno() + "\n" +
                    "Name : " + usersObject.getName() + "\n" +
                    "Course : " + usersObject.getCourse() + "\n" +
                    "Branch : " + usersObject.getBranch());
            if (libMagDB.getAStudentUsinEmail(StudentActivity.email).getRollno().equals(issued_to[1])){
                btnReserveBook.setVisibility(View.VISIBLE);
                btnReserveBook.setText("CANCEL RESERVATION");
            }else {
                btnReserveBook.setVisibility(View.GONE);
                btnReserveBook.setText("Reserve Book");
            }
        } else {
            UsersObject usersObject = libMagDB.getAStudent(libMagDB.getKeyFromStudentsTable(issued_to[0]));
            tvbookAvailability.setText("Issued to : " + "\n" +
                    "Rollno : " + usersObject.getRollno() + "\n" +
                    "Name : " + usersObject.getName() + "\n" +
                    "Course : " + usersObject.getCourse() + "\n" +
                    "Branch : " + usersObject.getBranch());
            btnReserveBook.setVisibility(View.GONE);
        }
    }

    private boolean validate(String bookId, String bookName) {
        boolean valid = true;
        if (bookName.isEmpty()) {
            etBookName.setError("Invalid Book Name");
            etBookName.requestFocus();
            valid = false;
        }
        if (bookId.isEmpty()) {
            etBookId.setError("Invalid Book Id");
            etBookId.requestFocus();
            valid = false;
        }
        return valid;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (etBookId != null && id != null) {
            etBookId.setText(id);
            BookMessage book = libMagDB.getABook(id);
            etBookName.setText(book.getBookName());
        }
    }
}
