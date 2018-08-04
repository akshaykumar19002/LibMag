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

import com.kumar.akshay.libmag.R;

import java.util.List;

public class RecentlyIssuedBooksAdapter extends ArrayAdapter<String> {

    TextView textViewBookId, textViewBookName, textViewBookIssueDate, textViewBookReturnDate, textViewBookFine;

    public RecentlyIssuedBooksAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_issued_books_list, parent, false);
        }

        if (getItem(position) != null) {
            String currentString[] = getItem(position).split(",");
            textViewBookId = view.findViewById(R.id.textViewBookId);
            textViewBookName = view.findViewById(R.id.textViewBookName);
            textViewBookIssueDate = view.findViewById(R.id.textViewIssueDate);
            textViewBookReturnDate = view.findViewById(R.id.textViewReturnDate);
            textViewBookFine = view.findViewById(R.id.textViewFine);
            textViewBookFine.setVisibility(View.GONE);

            textViewBookId.setText(currentString[0]);
            textViewBookName.setText(currentString[1]);
            textViewBookIssueDate.setText(currentString[2]);
            textViewBookReturnDate.setText(currentString[3]);
        }
        return view;
    }
}
