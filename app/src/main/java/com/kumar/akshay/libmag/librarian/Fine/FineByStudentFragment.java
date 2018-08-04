package com.kumar.akshay.libmag.librarian.Fine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kumar.akshay.libmag.Barcode.ScanBarcodeActivity;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

public class FineByStudentFragment extends Fragment {

    TextView mainTextView, loadingFineTextView;
    AutoCompleteTextView rollnoET = null, nameET = null;
    ProgressBar fineProgressBar;
    Button checkFineButton, scanButton;
    boolean isStudFrag = true;
    public static String rollno = null;
    String name;
    LibMagDBHelper libMagDB;
    BookMessage bookMessage;
    UsersObject usersObject;

    public FineByStudentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fine_status, container, false);
        libMagDB = new LibMagDBHelper(getContext());
        LibrarianActivity.isBasicFragment = false;
        isStudFrag = getArguments().getBoolean("fineStud");
        mainTextView = view.findViewById(R.id.textViewFine);
        loadingFineTextView = view.findViewById(R.id.textViewLoadingFine);
        rollnoET = view.findViewById(R.id.editTextRollno);
        nameET = view.findViewById(R.id.editTextName);
        rollnoET.setText("");
        nameET.setText("");
        fineProgressBar = view.findViewById(R.id.progressBarFine);
        checkFineButton = view.findViewById(R.id.buttonCheckFine);
        scanButton = view.findViewById(R.id.buttonScan);
        if (!isStudFrag) {
            ArrayAdapter<String> bookIds = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, libMagDB.getAllBookIds());
            rollnoET.setAdapter(bookIds);
            mainTextView.setText("Fine Status - Book");
            rollnoET.setHint("Book Id");
            nameET.setHint("Book Name");
        } else {
            ArrayAdapter<String> rollnos = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, libMagDB.getAllRollno());
            rollnoET.setAdapter(rollnos);
            mainTextView.setText("Fine Status - Student");
            rollnoET.setHint("Roll no");
            nameET.setHint("Student Name");
        }
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanBarcodeActivity.class);
                if (isStudFrag){
                    intent.putExtra("textViewText", "Scan roll no to continue");
                    intent.putExtra("req_code", 2);
                }else {
                    intent.putExtra("textViewText", "Scan book id to continue");
                    intent.putExtra("req_code", 3);
                }
                startActivity(intent);
            }
        });
        checkFineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollno = rollnoET.getText().toString();
                name = nameET.getText().toString();
                fineProgressBar.setVisibility(View.VISIBLE);
                loadingFineTextView.setVisibility(View.VISIBLE);
                if (validate(rollno, name)) {
                    if (isStudFrag) {
                        if (libMagDB.verifyRollno(rollno)) {
                            //checks fine for the specific student
                            usersObject = libMagDB.getAStudent(libMagDB.getKeyFromStudentsTable(rollno));
                            int fine = 0;
                            String[] books = usersObject.getIssuedBooks().split(",");
                            for (String book_id : books) {
                                fine += Integer.parseInt(libMagDB.getFineForBook(book_id));
                            }
                            if (fine <= 0) {
                                loadingFineTextView.setText("No fine");
                                fineProgressBar.setVisibility(View.GONE);
                            } else {
                                loadingFineTextView.setText("Fine : " + fine);
                                fineProgressBar.setVisibility(View.GONE);
                            }
                        } else {
                            rollnoET.setError("This rollno does not exist");
                            rollnoET.requestFocus();
                        }
                    } else {
                        if (libMagDB.verifyBookId(rollno)) {
                            //checks fine for specified book
                            bookMessage = libMagDB.getABook(rollno);
                            fineProgressBar.setVisibility(View.GONE);
                            loadingFineTextView.setText("Fine : " + libMagDB.getFineForBook(rollno));
                        } else {
                            rollnoET.setError("This book id does not exist");
                            rollnoET.requestFocus();
                        }
                    }
                } else {
                    fineProgressBar.setVisibility(View.GONE);
                    loadingFineTextView.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private boolean validate(String rollno, String name) {
        boolean valid = true;
        if (name.equals("")) {
            nameET.setError("Invalid name");
            nameET.requestFocus();
            valid = false;
        }
        if (rollno.equals("")) {
            rollnoET.setError("Invalid Rollno");
            rollnoET.requestFocus();
            valid = false;
        }
        return valid;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rollnoET != null && rollno != null && nameET != null) {
            rollnoET.setText("");
            nameET.setText("");
            if (isStudFrag) {
                rollnoET.setText("" + rollno);
                if (libMagDB.verifyRollno(rollno)) {
                    UsersObject student = libMagDB.getAStudent(libMagDB.getKeyFromStudentsTable(rollno));
                    nameET.setText(student.getName());
                }else{
//                    new AlertDialog.Builder(getContext())
//                            .setTitle("Invalid Input")
//                            .setMessage("Invalid Rollno")
//                            .setIcon(R.drawable.error_icon)
//                            .setNeutralButton("", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.cancel();
//                                    dialogInterface.dismiss();
//                                }
//                            }).show();
                }
            }else {
                rollnoET.setText("" + rollno);
                if (libMagDB.verifyBookId(rollno)) {
                    BookMessage book = libMagDB.getABook(rollno);
                    nameET.setText(book.getBookName());
                }else {

                }
            }
        }
    }
}
