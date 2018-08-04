package com.kumar.akshay.libmag.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kumar.akshay.libmag.Barcode.BarCode;
import com.kumar.akshay.libmag.R;

public class LoginFragment extends Fragment {

    Context context;
    LoginFormListener mLoginFormListener;
    EditText emailET, passwordET;
    String email, password;
    String[] users = {"Librarian", "Student"};
    Spinner spinner;
    int type;
    ArrayAdapter<String> adapter;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailET = view.findViewById(R.id.emailEditText);
        passwordET = view.findViewById(R.id.passwordEditText);
        spinner = view.findViewById(R.id.spinnerType);
        emailET.setText("");
        passwordET.setText("");
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        type = 0;
        Button loginBtn = view.findViewById(R.id.loginBtn);
        final Button registerBtn = view.findViewById(R.id.registerButton);
        registerBtn.setVisibility(View.GONE);
        passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                passwordET.requestFocus();
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                if (validateData()) {
                    mLoginFormListener.loginButtonHadler(type, email, password);
                    return true;
                } else
                    return false;
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                if (validateData())
                    mLoginFormListener.loginButtonHadler(type, email, password);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("register Button Click", "Register Button Clicked");

                mLoginFormListener.changeTheFragment(new RegisterFragment());
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = i;
                if (type == 1)
                    registerBtn.setVisibility(View.VISIBLE);
                else
                    registerBtn.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner.setSelected(false);
            }
        });
        return view;
    }

    private boolean validateData() {
        boolean valid = true;
        if (!email.contains("@") && !email.contains(".")) {
            emailET.setError("Invalid email");
            emailET.requestFocus();
            valid = false;
        }
        if (password.length() < 5) {
            passwordET.setError("Too short");
            passwordET.requestFocus();
            valid = false;
        }
        return valid;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mLoginFormListener = (LoginFormListener) activity;
    }

    public interface LoginFormListener {
        void loginButtonHadler(int type, String email, String pass);
        void changeTheFragment(Fragment fragment);
    }
}
