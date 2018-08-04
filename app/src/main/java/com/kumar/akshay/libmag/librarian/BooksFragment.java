package com.kumar.akshay.libmag.librarian;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.R;

import java.util.ArrayList;
import java.util.List;

public class BooksFragment extends Fragment {

    Bundle bundle;
    BooksFragmentListener mBooksFragmentListener;

    public BooksFragment() {
    }

    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager layoutManager;
    RecyclerView booksRecyclerView;
    BooksAdapter booksAdapter;
    DatabaseReference databaseReference;
    BookMessage dummyBookMessage = new BookMessage(0, "No books present", null, null, null, null, null, null, null);
    boolean isSearchFrag = false;
    String bookId;
    List<BookMessage> list;
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, null);
        LibrarianActivity.isBasicFragment = true;
        bundle = getArguments();
        if (bundle != null) {
            isSearchFrag = bundle.getBoolean("searchFrag");
            if (isSearchFrag) {
                bookId = bundle.getString("bookId");
            }
        }
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list = new ArrayList<>();
        list.add(dummyBookMessage);
        swipeRefreshLayout = view.findViewById(R.id.books_swipe_container);
        booksAdapter = new BooksAdapter(getContext(), list, getActivity().getSupportFragmentManager(), mBooksFragmentListener);
        booksRecyclerView = view.findViewById(R.id.bookRecyclerView);
        booksRecyclerView.setLayoutManager(layoutManager);
        booksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        booksRecyclerView.setAdapter(booksAdapter);
        booksAdapter.notifyData(list);
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
        list.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BookMessage bookMessage = dataSnapshot.getValue(BookMessage.class);
                if (bookMessage != null) {
                    if (isSearchFrag) {
                        if (bookId.equals(Integer.toString(bookMessage.getBookId()))) {
                            list.remove(dummyBookMessage);
                            list.add(bookMessage);
                        }
                    } else {
                        list.remove(dummyBookMessage);
                        list.add(bookMessage);
                    }
                }
                booksAdapter.notifyData(list);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBooksFragmentListener = (BooksFragmentListener) getActivity();
    }

    public interface BooksFragmentListener {
        boolean showBooksMenu(BookMessage bookMessage);
    }
}
