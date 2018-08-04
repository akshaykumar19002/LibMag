package com.kumar.akshay.libmag.librarian;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;

import java.util.ArrayList;

public class IssueBookDialogFragment extends DialogFragment {

    public interface IssueBookDialogListener {
        void onIssueBookClick(String rollno);
    }

    IssueBookDialogListener issueBookDialogListener;
    AutoCompleteTextView autoCompleteTextView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_issue_book_dialog, null);
        autoCompleteTextView = view.findViewById(R.id.issueBookAutoCompleteTextView);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                issueBookDialogListener.onIssueBookClick(autoCompleteTextView.getText().toString());
                getActivity().onBackPressed();
            }
        });
        ArrayList<String> arrayList = new LibMagDBHelper(getContext()).getAllRollno();
        if (!arrayList.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, arrayList);
            autoCompleteTextView.setAdapter(arrayAdapter);
        } else {
            Toast.makeText(getContext(), "No rollno in the database", Toast.LENGTH_LONG).show();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Issue Book");
        builder.setPositiveButton("Issue Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!autoCompleteTextView.getText().toString().isEmpty())
                    issueBookDialogListener.onIssueBookClick(autoCompleteTextView.getText().toString());
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        issueBookDialogListener = (IssueBookDialogListener) activity;
    }
}
