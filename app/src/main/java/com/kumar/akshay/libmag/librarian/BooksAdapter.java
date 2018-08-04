package com.kumar.akshay.libmag.librarian;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> {

    Context context;
    List<BookMessage> list;
    FragmentManager fragmentManager;
    BooksFragment.BooksFragmentListener booksFragmentListener;

    public BooksAdapter(@NonNull Context context, List<BookMessage> list, FragmentManager fragmentManager, BooksFragment.BooksFragmentListener booksFragmentListener) {
        this.context = context;
        this.list = list;
        this.fragmentManager = fragmentManager;
        this.booksFragmentListener = booksFragmentListener;
    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book, parent, false);
        return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BooksViewHolder holder, final int position) {
        BookMessage bookMessage = list.get(position);
        if (bookMessage != null)
            if (bookMessage.getBookId() == 0) {
                holder.book_id.setText("No Book Found");
                holder.book_name.setVisibility(View.GONE);
                holder.book_author.setVisibility(View.GONE);
                holder.book_publisher.setVisibility(View.GONE);
                holder.book_issue_status.setVisibility(View.GONE);
                holder.book_return_date.setVisibility(View.GONE);
            } else {
                holder.book_name.setVisibility(View.VISIBLE);
                holder.book_author.setVisibility(View.VISIBLE);
                holder.book_publisher.setVisibility(View.VISIBLE);
                holder.book_issue_status.setVisibility(View.VISIBLE);
                holder.book_return_date.setVisibility(View.VISIBLE);
                holder.book_id.setText(Integer.valueOf(bookMessage.getBookId()).toString());
                holder.book_name.setText(bookMessage.getBookName());
                holder.book_author.setText(bookMessage.getBookAuthor());
                holder.book_publisher.setText(bookMessage.getBookPublisher());
                if (!bookMessage.getBookIssueDate().equals("NIL")) {
                    holder.book_issue_status.setText("Issued");
                    holder.book_return_date.setVisibility(View.VISIBLE);
                    holder.book_return_date.setText(getReturnDate(bookMessage.getBookIssueDate(), bookMessage.getBookIssuedTo()));
                } else {
                    holder.book_issue_status.setText("Not yet issued");
                    holder.book_return_date.setVisibility(View.GONE);
                }
            }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookMessage selectedBookMessage = list.get(position);
                if (selectedBookMessage.getBookId() == 0)
                    return;
                if (selectedBookMessage.bookIssueDate == null) {
                    return;
                }
                Fragment fragment = new ShowASpecificBookFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("bookId", selectedBookMessage.getBookId());
                bundle.putString("bookName", selectedBookMessage.getBookName());
                bundle.putString("bookAuthor", selectedBookMessage.getBookAuthor());
                bundle.putString("bookEdition", selectedBookMessage.getBookEdition());
                bundle.putString("bookPublisher", selectedBookMessage.getBookPublisher());
                bundle.putString("bookDescription", selectedBookMessage.getBookDescription());
                bundle.putString("issueDate", selectedBookMessage.getBookIssueDate());
                bundle.putString("issuedTo", selectedBookMessage.getBookIssuedTo());
                bundle.putString("bookLocation", selectedBookMessage.getBookLocation());
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragmentLibrarian, fragment).addToBackStack(null).commit();
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                BookMessage selectedBookMessage = list.get(position);
                view.setSelected(true);
                if (!booksFragmentListener.showBooksMenu(selectedBookMessage)) {
                    Toast.makeText(context, "Unknown error", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static String getReturnDate(String dateInString, String issuedTo) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String[] some = issuedTo.split(":");
        int days = Integer.parseInt(some[1]);
        try {
            Date date = format.parse(dateInString);
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, days);
            return format.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void notifyData(List<BookMessage> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class BooksViewHolder extends RecyclerView.ViewHolder {

        TextView book_id, book_name, book_author, book_publisher, book_issue_status, book_return_date;
        LinearLayout linearLayout;

        public BooksViewHolder(View view) {
            super(view);
            book_id = view.findViewById(R.id.book_id_tv);
            book_name = view.findViewById(R.id.book_name_tv);
            book_author = view.findViewById(R.id.book_author_tv);
            book_publisher = view.findViewById(R.id.book_publisher_tv);
            book_issue_status = view.findViewById(R.id.book_issue_status_tv);
            book_return_date = view.findViewById(R.id.book_return_date_tv);
            linearLayout = view.findViewById(R.id.booksListLinearLayout);
        }
    }
}
