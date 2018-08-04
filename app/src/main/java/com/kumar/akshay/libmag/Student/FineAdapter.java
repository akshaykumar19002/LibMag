package com.kumar.akshay.libmag.Student;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.librarian.BooksAdapter;

import static com.kumar.akshay.libmag.librarian.IssuedBooks.IssuedBooksAdapter.getFine;

public class FineAdapter extends ArrayAdapter<BookMessage> {

    TextView textViewBookId, textViewBookName, textViewBookIssueDate, textViewBookReturnDate, textViewBookFine;

    public FineAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_issued_books_list, parent, false);
        }
        BookMessage bookMessage = getItem(position);
        textViewBookId = view.findViewById(R.id.textViewBookId);
        textViewBookName = view.findViewById(R.id.textViewBookName);
        textViewBookIssueDate = view.findViewById(R.id.textViewIssueDate);
        textViewBookReturnDate = view.findViewById(R.id.textViewReturnDate);
        textViewBookFine = view.findViewById(R.id.textViewFine);

        if (bookMessage != null)
            if (bookMessage.getBookName().equals("No books Issued yet")) {
                textViewBookName.setText(bookMessage.getBookName());
                textViewBookId.setVisibility(View.GONE);
                textViewBookFine.setVisibility(View.GONE);
                textViewBookIssueDate.setVisibility(View.GONE);
                textViewBookReturnDate.setVisibility(View.GONE);
            } else {
                textViewBookId.setVisibility(View.VISIBLE);
                textViewBookFine.setVisibility(View.VISIBLE);
                textViewBookIssueDate.setVisibility(View.VISIBLE);
                textViewBookReturnDate.setVisibility(View.VISIBLE);
                textViewBookId.setText(Integer.toString(bookMessage.getBookId()));
                textViewBookName.setText(bookMessage.getBookName());
                textViewBookIssueDate.setText(bookMessage.getBookIssueDate());

                //Add 14 to date and display that as return date
                textViewBookReturnDate.setText(BooksAdapter.getReturnDate(bookMessage.getBookIssueDate(), bookMessage.getBookIssuedTo()));

                //If return date has been crossed then display a fine of Rs. 2 per day.
                int fine = getFine(BooksAdapter.getReturnDate(bookMessage.getBookIssueDate(), bookMessage.getBookIssuedTo()));
                if (fine <= -1) {
                    Toast.makeText(getContext(), "Unknown error", Toast.LENGTH_LONG).show();
                } else {
                    textViewBookFine.setText("" + fine);
                }
            }


        return view;
    }
}
