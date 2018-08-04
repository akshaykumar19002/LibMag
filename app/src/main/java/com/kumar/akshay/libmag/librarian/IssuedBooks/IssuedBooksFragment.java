package com.kumar.akshay.libmag.librarian.IssuedBooks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

import java.util.ArrayList;

public class IssuedBooksFragment extends Fragment {

    DatabaseReference databaseReference, databaseReference2;
    IssuedBooksAdapter issuedBooksAdapter;
    LinearLayoutManager layoutManager;
    RecyclerView issuedBooksRecyclerView;
    BookMessage dummyData = new BookMessage(-1, "No books Issued yet", null, null, null, null, null, null, null);
    UsersObject usersObject;
    LibMagDBHelper libMagDB;
    String email = "";
    boolean librarian = true;
    String[] issued_books;
    ArrayList<BookMessage> list;
    SwipeRefreshLayout swipeRefreshLayout;

    public IssuedBooksFragment() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issued_books, container, false);
        LibrarianActivity.isBasicFragment = false;
        swipeRefreshLayout = view.findViewById(R.id.books_swipe_container);
        libMagDB = new LibMagDBHelper(getContext());
        if (getArguments() != null) {
            librarian = getArguments().getBoolean("librarian", false);
            if (!librarian) {
                email = getArguments().getString("email", "Nothing");
            }
        }
        issuedBooksRecyclerView = view.findViewById(R.id.issuedBooksRecyclerView);
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.add(dummyData);
        issuedBooksRecyclerView.setLayoutManager(layoutManager);
        issuedBooksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        issuedBooksAdapter = new IssuedBooksAdapter(getContext(), list);
        issuedBooksRecyclerView.setAdapter(issuedBooksAdapter);
        issuedBooksAdapter.notifyData(list);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!librarian) {
            usersObject = libMagDB.getAStudentUsinEmail(email);
            if(usersObject.getIssuedBooks().equals("NIL")){
                return;
            }
            issued_books = usersObject.getIssuedBooks().split(",");
        }
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BookMessage bookMessage = dataSnapshot.getValue(BookMessage.class);
                if (bookMessage != null)
                    if (bookMessage.bookName != null) {
                        if (!bookMessage.getBookIssueDate().equals("NIL")) {
                            if (!librarian) {
                                for (String book_id : issued_books) {
                                    if (book_id.equals(Integer.toString(bookMessage.getBookId()))) {
                                        list.remove(dummyData);
                                        list.add(bookMessage);
                                        break;
                                    }
                                }
                            } else {
                                list.remove(dummyData);
                                list.add(bookMessage);
                            }
                        }
                    }
                issuedBooksAdapter.notifyData(list);
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

}
