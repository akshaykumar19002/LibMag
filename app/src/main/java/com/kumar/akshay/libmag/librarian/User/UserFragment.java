package com.kumar.akshay.libmag.librarian.User;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.Student.FineFragment;
import com.kumar.akshay.libmag.Student.RecentlyIssuedBooksFragment;
import com.kumar.akshay.libmag.librarian.IssuedBooks.IssuedBooksFragment;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;
import com.kumar.akshay.libmag.librarian.LoginRequest.LoginRequestAdpater;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    Bundle bundle;
    LinearLayoutManager layoutManager;
    RecyclerView usersRecyclerView;
    LoginRequestAdpater loginRequestAdpater;
    DatabaseReference databaseReference;
    UsersObject dummyUser = new UsersObject(1, "No data found", null, null, null, null, null, null);
    String rollno;
    List<UsersObject> list;
    FragmentManager fragmentManager;
    Fragment fragment1, fragment2;
    UsersObject user1;
    View view;

    public UserFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_req_list, null);
        LibrarianActivity.isBasicFragment = false;
        bundle = getArguments();
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list = new ArrayList<>();
        list.add(dummyUser);
        loginRequestAdpater = new LoginRequestAdpater(getContext(), list, getActivity().getSupportFragmentManager(), false);
        usersRecyclerView = view.findViewById(R.id.userListRecyclerView);
        usersRecyclerView.setLayoutManager(layoutManager);
        usersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        usersRecyclerView.setAdapter(loginRequestAdpater);
        loginRequestAdpater.notifyData(list);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        list.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UsersObject user = dataSnapshot.getValue(UsersObject.class);
                if (user != null) {
                    if (user.getType() == 1) {
                        list.remove(dummyUser);
                        list.add(user);
                    }
                }
                loginRequestAdpater.notifyData(list);
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
}