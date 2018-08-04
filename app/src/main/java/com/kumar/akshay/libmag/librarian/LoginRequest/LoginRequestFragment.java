package com.kumar.akshay.libmag.librarian.LoginRequest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;

import java.util.ArrayList;
import java.util.List;

public class LoginRequestFragment extends Fragment {

    Bundle bundle;
    LinearLayoutManager layoutManager;
    RecyclerView usersRecyclerView;
    LoginRequestAdpater loginRequestAdpater;
    DatabaseReference databaseReference;
    UsersObject dummyUser = new UsersObject(1, "No data found", null, null, null, null, null, null);
    boolean isSearchFrag = false;
    String rollno;
    List<UsersObject> list;
    FragmentManager fragmentManager;
    public LoginRequestFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_req_list, null);
        LibrarianActivity.isBasicFragment = false;
        bundle = getArguments();
//        if (bundle != null) {
//            isSearchFrag = bundle.getBoolean("searchFrag");
//            if (isSearchFrag) {
//                bookId = bundle.getString("bookId");
//            }
//        }
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list = new ArrayList<>();
        list.add(dummyUser);
        loginRequestAdpater = new LoginRequestAdpater(getContext(), list, getActivity().getSupportFragmentManager(), true);
        loginRequestAdpater.notifyData(list);
        usersRecyclerView = view.findViewById(R.id.userListRecyclerView);
        usersRecyclerView.setLayoutManager(layoutManager);
        usersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        usersRecyclerView.setAdapter(loginRequestAdpater);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_req");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UsersObject user = dataSnapshot.getValue(UsersObject.class);
                if (user != null) {
                    if (isSearchFrag) {
                        if (rollno.equals(user.getRollno())) {
                            list.remove(dummyUser);
                            list.add(user);
                        }
                    } else {
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
