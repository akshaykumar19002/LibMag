package com.kumar.akshay.libmag.librarian.LoginRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.Student.FineFragment;
import com.kumar.akshay.libmag.Student.RecentlyIssuedBooksFragment;
import com.kumar.akshay.libmag.librarian.IssuedBooks.IssuedBooksFragment;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;
import com.kumar.akshay.libmag.librarian.User.ShowASpecificUserRequestFragment;
import com.kumar.akshay.libmag.librarian.User.ShowAUserFragment;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

import java.util.List;

public class LoginRequestAdpater extends RecyclerView.Adapter<LoginRequestAdpater.LoginRequestViewHolder> {

    Context context;
    List<UsersObject> list;
    FragmentManager fragmentManager;
    UsersObject user;
    boolean isReqFrag = true;
    Fragment fragment1, fragment2, fragment;
    LoginInterface mLoginInterface;
    View view;

    public LoginRequestAdpater(@NonNull Context context, List<UsersObject> list, FragmentManager fragmentManager, boolean isReqFrag) {
        this.context = context;
        this.list = list;
        this.fragmentManager = fragmentManager;
        this.isReqFrag = isReqFrag;
    }

    @Override
    public LoginRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_req, parent, false);
        return new LoginRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LoginRequestViewHolder holder, int position) {
        user = list.get(position);
        mLoginInterface = (LoginInterface) context;
        if (isReqFrag){
            holder.imageView.setVisibility(View.GONE);
        }
        if (user != null)
            if (user.getName().equals("No data found")) {
                holder.rollno.setVisibility(View.GONE);
                holder.name.setText(user.getName());
                holder.course.setVisibility(View.GONE);
                holder.branch.setVisibility(View.GONE);
            } else {
                holder.rollno.setText(user.getRollno());
                holder.name.setText(user.getName());
                holder.course.setText(user.getCourse());
                holder.branch.setText(user.getBranch());
                holder.rollno.setVisibility(View.VISIBLE);
                holder.course.setVisibility(View.VISIBLE);
                holder.branch.setVisibility(View.VISIBLE);
            }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("LoginRequestAdapter", "Image View on click listener");
                LibrarianActivity.isBasicFragment = false;
                if (user.getRollno() == null) {
                    return;
                }
                if (!isReqFrag) {
                    showPopupMenuForUsers(user, holder.imageView);
                }
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LibrarianActivity.isBasicFragment = false;
                if (user.getRollno() == null) {
                    return;
                }
                if (isReqFrag)
                    fragment = new ShowASpecificUserRequestFragment();
                else
                    fragment = new ShowAUserFragment();
                Bundle bundle = new Bundle();
                bundle.putString("rollno", user.getRollno());
                bundle.putString("name", user.getName());
                bundle.putString("email", user.getEmail());
                bundle.putString("pass", user.getPass());
                bundle.putString("course", user.getCourse());
                bundle.putString("branch", user.getBranch());
                bundle.putString("issuedBooks", user.getIssuedBooks());
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragmentLibrarian, fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void notifyData(List<UsersObject> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class LoginRequestViewHolder extends RecyclerView.ViewHolder {

        TextView rollno, name, course, branch;
        LinearLayout linearLayout;
        ImageView imageView;

        public LoginRequestViewHolder(View view) {
            super(view);
            rollno = view.findViewById(R.id.rollno_tv);
            name = view.findViewById(R.id.name_tv);
            course = view.findViewById(R.id.course_tv);
            branch = view.findViewById(R.id.branch_tv);
            linearLayout = view.findViewById(R.id.usersListLinearLayout);
            imageView = view.findViewById(R.id.imageMenu);
        }
    }

    public interface LoginInterface {
        void addUserMenu(UsersObject user, View view);
    }

    void showPopupMenuForUsers(final UsersObject user, View image) {
        view = image;
        PopupMenu popup = new PopupMenu(image.getContext(), image);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.users_list, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_issued_books:
                        fragment2 = new IssuedBooksFragment();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("librarian", false);
                        bundle.putString("email", user.getEmail());
                        fragment2.setArguments(bundle);
                        switchContentWithBackStack(fragment2);
                        fragment1 = fragment2;
                        return true;
                    case R.id.action_recently_issued_books:
                        fragment2 = new RecentlyIssuedBooksFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("email", user.getEmail());
                        fragment2.setArguments(bundle1);
                        switchContentWithBackStack(fragment2);
                        fragment1 = fragment2;
                        return true;
                    case R.id.action_check_fine:
                        fragment2 = new FineFragment();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("email", user.getEmail());
                        fragment2.setArguments(bundle2);
                        switchContentWithBackStack(fragment2);
                        fragment1 = fragment2;
                        return true;
                    case R.id.action_delete_user:
                        deleteuser();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    public void deleteuser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        if (!checkFine()) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(view, "Account Deleted", Snackbar.LENGTH_LONG).show();
                                FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email")
                                        .equalTo(email).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        UsersObject student = dataSnapshot.getValue(UsersObject.class);
                                        if (student != null) {
                                            if (student.getRollno() != null) {
                                                FirebaseDatabase.getInstance().getReference().child("users").child(dataSnapshot.getKey()).removeValue();
                                            }
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
                                });
                                context.startActivity(new Intent(context, MainScreen.class));
                            }
                        }
                    });
        } else {
            Snackbar.make(view, "You currently have some issued books", Snackbar.LENGTH_LONG).show();
        }
    }

    boolean checkFine() {
        LibMagDBHelper libMagDB = new LibMagDBHelper(context);
        final UsersObject userObject = user;
        if (userObject != null)
            if (libMagDB.verifyRollno(userObject.getRollno())) {
                //checks fine for the specific student
                UsersObject usersObject = libMagDB.getAStudent(libMagDB.getKeyFromStudentsTable(userObject.getRollno()));
                int fine = 0;
                String[] books = usersObject.getIssuedBooks().split(",");
                if (!books[0].equals(null)) {
                    return true;
                } else
                    return false;
            } else {
                return false;
            }
        return true;
    }

    public void switchContentWithBackStack(Fragment fragment2) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragmentLibrarian, fragment2);
        ft.commit();
    }
}