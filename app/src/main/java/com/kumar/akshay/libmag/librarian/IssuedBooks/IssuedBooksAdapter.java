package com.kumar.akshay.libmag.librarian.IssuedBooks;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.librarian.BooksAdapter;
import com.kumar.akshay.libmag.librarian.IssueABookFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class IssuedBooksAdapter extends RecyclerView.Adapter<IssuedBooksAdapter.IssuedBooksViewHolder> {

    List<BookMessage> list;
    Context context;
    DatabaseReference databaseReference, databaseReference2;
    LibMagDBHelper libMagDB;

    TextView textViewBookId, textViewBookName, textViewBookIssueDate, textViewBookReturnDate, textViewBookFine;

    public IssuedBooksAdapter(@NonNull Context context, List<BookMessage> list) {
        this.list = list;
        this.context = context;
        libMagDB = new LibMagDBHelper(context);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @Override
    public IssuedBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_issued_books_list, parent, false);
        return new IssuedBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IssuedBooksViewHolder holder, final int position) {
        BookMessage bookMessage = list.get(position);

        if (bookMessage != null)
            if (bookMessage.getBookName().equals("No books Issued yet")) {
                textViewBookId.setText(bookMessage.getBookName());
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

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final BookMessage selectedBookMessage = list.get(position);
                        if (selectedBookMessage.getBookName().equals("No books Issued yet")) {
                            return;
                        }
                        AlertDialog builder = new AlertDialog.Builder(context)
                                .setTitle("Return Book")
                                .setMessage("Do you want to return this book ?")
                                .setPositiveButton("Return Book", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int result = new IssueABookFragment().returnBook(context, libMagDB, databaseReference, databaseReference2, Integer.toString(selectedBookMessage.getBookId()), "", v);
                                        if (result == 0) {
                                            Snackbar.make(v, "Unable to return the book", Snackbar.LENGTH_LONG).show();
                                            //context.startActivity(new Intent(context, MainScreen.class));
                                        } else {
                                            Snackbar.make(v, "Book Returned", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .create();
                        builder.show();
                    }
                });

                //Add 14 to date and display that as return date
                textViewBookReturnDate.setText(BooksAdapter.getReturnDate(bookMessage.getBookIssueDate(), bookMessage.getBookIssuedTo()));

                //If return date has been crossed then display a fine of Rs. 2 per day.
                int fine = getFine(BooksAdapter.getReturnDate(bookMessage.getBookIssueDate(), bookMessage.getBookIssuedTo()));
                if (fine <= -1) {
                    Toast.makeText(context, "Unknown error", Toast.LENGTH_LONG).show();
                } else {
                    textViewBookFine.setText("Fine : " + fine);
                }
            }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void notifyData(List<BookMessage> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class IssuedBooksViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;

        public IssuedBooksViewHolder(View itemView){
            super(itemView);
            textViewBookId = itemView.findViewById(R.id.textViewBookId);
            textViewBookName = itemView.findViewById(R.id.textViewBookName);
            textViewBookIssueDate = itemView.findViewById(R.id.textViewIssueDate);
            textViewBookReturnDate = itemView.findViewById(R.id.textViewReturnDate);
            textViewBookFine = itemView.findViewById(R.id.textViewFine);
            linearLayout = itemView.findViewById(R.id.issuedBooksItemLinearLayout);
        }
    }

    public static int getFine(String returnDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format.parse(returnDate);
            Date currentTime = GregorianCalendar.getInstance().getTime();
            long diff = currentTime.getTime() - date.getTime();
            int diffInDays = (int) (diff / (1000 * 60 * 60 * 24));
            int fine = 0;
            if (diffInDays >= 0){
                fine = diffInDays * 2;
            }
            return fine;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
