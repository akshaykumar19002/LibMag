package com.kumar.akshay.libmag.Student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;

import java.util.ArrayList;

public class RecentlyIssuedBooksFragment extends Fragment {

    ListView recentlyIssuedListView;
    ArrayList<String> al;
    LibMagDBHelper libMagDB;
    String email;
    RecentlyIssuedBooksAdapter recentlyIssuedBooksAdapter;

    public RecentlyIssuedBooksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recently_issued_books, container, false);
        libMagDB = new LibMagDBHelper(getContext());
        LibrarianActivity.isBasicFragment = false;
        email = getArguments().getString("email");
        al = libMagDB.getIssuedBooks(libMagDB.getARollno(email));
        recentlyIssuedListView = view.findViewById(R.id.recentlyIssuedBooksListView);
        recentlyIssuedBooksAdapter = new RecentlyIssuedBooksAdapter(getContext(), R.layout.fragment_issued_books_list, al);
        recentlyIssuedListView.setAdapter(recentlyIssuedBooksAdapter);

        return view;
    }
}
