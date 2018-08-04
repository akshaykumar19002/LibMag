package com.kumar.akshay.libmag.librarian;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    Spinner searchSpinner;
    Button searchButton;
    EditText editTextSearch;
    RecyclerView searchRecyclerView;
    ArrayAdapter<String> searchAdapter;
    String[] searchList;
    Context context;
    LinearLayoutManager layoutManager;
    String searchItem, searchString;
    boolean gotData = false;
    BooksAdapter booksAdapter;
    List<BookMessage> books;
    Query query;
    BooksFragment.BooksFragmentListener mBooksFragmentListener;
    BookMessage dummyBook = new BookMessage(0, "No Book Found", null, null, null, null, null, null, null);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        LibrarianActivity.isBasicFragment = false;

        searchButton = view.findViewById(R.id.searchButton);
        searchSpinner = view.findViewById(R.id.searchSpinner);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        searchRecyclerView = view.findViewById(R.id.searchBookRecyclerView);
        searchList = new String[]{"Book Id", "Book Name", "Book Publisher", "Book Author", "Issue Status", "Return Date"};
        searchAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, searchList);
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(searchAdapter);
        books = new ArrayList<>();
        booksAdapter = new BooksAdapter(context, books, getFragmentManager(), mBooksFragmentListener);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchRecyclerView.setAdapter(booksAdapter);
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = searchAdapter.getItem(position);
                if (position == 5) {
                    editTextSearch.setHint("dd-mm-yy");
                } else
                    editTextSearch.setHint(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                books.clear();
                booksAdapter.notifyData(books);
                searchString = editTextSearch.getText().toString();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
                switch (searchSpinner.getSelectedItemPosition()) {
                    case 0:
                        searchItem = "bookId";
                        query = databaseReference.orderByChild(searchItem).equalTo(Integer.parseInt(searchString));
                        break;
                    case 1:
                        searchItem = "bookName";
                        query = databaseReference.orderByChild(searchItem).equalTo(searchString);
                        break;
                    case 2:
                        searchItem = "bookPublisher";
                        query = databaseReference.orderByChild(searchItem).equalTo(searchString);
                        break;
                    case 3:
                        searchItem = "bookAuthor";
                        query = databaseReference.orderByChild(searchItem).equalTo(searchString);
                        break;
                    case 4:
                        searchItem = "bookIssuedTo";
                        query = databaseReference.orderByChild(searchItem).startAt(searchString);
                        break;
                    case 5:
                        searchItem = "bookIssueDate";
                        query = databaseReference.orderByChild(searchItem).equalTo(searchString);
                        break;
                }
                query.addChildEventListener(searchChildEventListener);
                query.addListenerForSingleValueEvent(searchValueEventListener);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    ChildEventListener searchChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            BookMessage bookMessage = dataSnapshot.getValue(BookMessage.class);
            if (bookMessage != null) {
                gotData = true;
                books.remove(dummyBook);
                books.add(bookMessage);
                booksAdapter.notifyData(books);
            }
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
    };

    ValueEventListener searchValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!gotData) {
                books.add(dummyBook);
                booksAdapter.notifyData(books);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
