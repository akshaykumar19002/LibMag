package com.kumar.akshay.libmag.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.librarian.BooksAdapter;
import com.kumar.akshay.libmag.librarian.IssuedBooks.IssuedBooksAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BooksService extends IntentService {

    public static String TAG = "BooksService";
    DatabaseReference databaseReference;
    LibMagDBHelper libMagDB;

    public BooksService() {
        super("BooksService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        libMagDB = new LibMagDBHelper(BooksService.this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int fine = 0;
                BookMessage bookMessage = dataSnapshot.getValue(BookMessage.class);
                if (bookMessage != null) {
                    if (!bookMessage.bookIssueDate.equals("NIL"))
                        fine = IssuedBooksAdapter.getFine(BooksAdapter.getReturnDate(bookMessage.getBookIssueDate(), bookMessage.getBookIssuedTo()));
                    libMagDB.insertIntoBooksTable(dataSnapshot.getKey(), bookMessage.getBookId(), bookMessage.getBookName(), bookMessage.getBookIssueDate(), Integer.toString(fine), bookMessage.getBookAuthor(), bookMessage.getBookPublisher(), bookMessage.getBookEdition(), bookMessage.getBookDescription(), bookMessage.getBookIssuedTo());
                }
                Calendar calendar = GregorianCalendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String[] issued_to = bookMessage.getBookIssuedTo().split(":");
                try {
                    if (issued_to[0].equals("Reserved") && differenceInDates(format.parse(bookMessage.getBookIssueDate())) > 48 ){
                        bookMessage.setBookIssuedTo("NIL");
                        bookMessage.setBookIssueDate("NIL");
                        databaseReference.child(libMagDB.getKeyFromBooksTable(Integer.toString(bookMessage.getBookId()))).removeValue();
                        databaseReference.push().setValue(bookMessage);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                BookMessage bookMessage = dataSnapshot.getValue(BookMessage.class);
                if (bookMessage != null){
                    libMagDB.removeABook(dataSnapshot.getKey(), String.valueOf(bookMessage.getBookId()));
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public long differenceInDates(Date date1){
        long diff = 0;
        try {
            Calendar calendar = GregorianCalendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date2 = format.parse(format.format(calendar.getTime()));
            diff = (date2.getTime() - date1.getTime())/(60*60*1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

}
