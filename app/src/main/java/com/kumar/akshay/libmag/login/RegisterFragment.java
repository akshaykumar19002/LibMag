package com.kumar.akshay.libmag.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.kumar.akshay.libmag.Barcode.NewBarCodeDialogFragment;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.librarian.LibrarianActivity;

public class RegisterFragment extends Fragment {

    Context context;

    public RegisterFragment() {
    }

    private RegisterFormListener mRegisterFormListener;

    String name, email, pass, rollno, course, branch;
    int type;
    EditText nameET, emailET, passET, rollnoET;
    private Spinner spinnerCourse, spinnerBranch, spinnerType;
    String courses[] = new String[]{"B. Tech.", "M. Tech.", "BBA", "MBA"};
    String branches[] = new String[]{"CSE", "Mech", "ECE", "EEE", "Civil"};
    String type_list[] = new String[]{"Librarian", "Student"};
    ArrayAdapter<String> coursesAdapter, branchAdapter, typeAdapter;
    View view1;
    Button registerButton, generateBarCodeButton;
    LibMagDBHelper libMagDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        view1 = view.findViewById(R.id.registerScrollView);
        libMagDB = new LibMagDBHelper(context);
        LibrarianActivity.isBasicFragment = false;
        nameET = view.findViewById(R.id.nameEditText);
        emailET = view.findViewById(R.id.EmailIdeditText);
        passET = view.findViewById(R.id.passEditText);
        rollnoET = view.findViewById(R.id.rollnoEditText);
        spinnerCourse = view.findViewById(R.id.spinnerRegisterCourse);
        spinnerBranch = view.findViewById(R.id.spinnerRegisterBranch);
        spinnerType = view.findViewById(R.id.spinnerTypeRegister);
        nameET.setText("");
        emailET.setText("");
        passET.setText("");
        rollnoET.setText("");
        coursesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, courses);
        coursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourse.setAdapter(coursesAdapter);
        typeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, type_list);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        spinnerType.setVisibility(View.GONE);
        type = 1;
        course = spinnerCourse.getSelectedItem().toString();
        updateUI();
        registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                pass = passET.getText().toString();
                rollno = rollnoET.getText().toString();
                if (validateData(name, email, pass, rollno)) {
                    if (type == 0)
                        mRegisterFormListener.registerButton(null, type, name, email, pass, rollno, "", "");
                    else if (type == 2)
                        mRegisterFormListener.registerButton(null, type, name, email, pass, rollno, branch, "");
                    else if (type == 1)
                        mRegisterFormListener.registerButton(view1, type, name, email, pass, rollno, course, branch);
                }else
                    Log.v("MainActivity", "Invalid data");
            }
        });

        generateBarCodeButton = view.findViewById(R.id.generateBarcodeButton);
        generateBarCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new NewBarCodeDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("barCode", Integer.parseInt(rollnoET.getText().toString()));
                bundle.putString("type", "rollno");
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "newbarcodedialogfragment");
            }
        });

        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course = spinnerCourse.getSelectedItem().toString();
                if (course.equals("BBA") || course.equals("MBA")) {
                    spinnerBranch.setVisibility(View.GONE);
                } else
                    spinnerBranch.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerCourse.setSelected(false);
            }
        });
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = i;
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                branch = spinnerBranch.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                branch = "";
            }
        });
        return view;
    }

    private void updateUI(){
        if (type == 0) {
            rollnoET.setHint("Staff id");
            spinnerCourse.setVisibility(View.GONE);
            spinnerBranch.setVisibility(View.GONE);
        } else if (type == 1) {
            spinnerCourse.setVisibility(View.VISIBLE);
            spinnerBranch.setVisibility(View.VISIBLE);
            rollnoET.setHint("Roll no");
            branches = new String[]{"CSE", "Mech", "ECE", "EEE", "Civil","BBA", "MBA", "None"};
            branchAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, branches);
            branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBranch.setAdapter(branchAdapter);
            branch = spinnerBranch.getSelectedItem().toString();
        } else {
            rollnoET.setHint("Staff id");
            branchAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, branches);
            branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBranch.setAdapter(branchAdapter);
            spinnerBranch.setVisibility(View.GONE);
            //branch = spinnerBranch.getSelectedItem().toString();
        }
    }

    private boolean validateData(String name, String email, String pass, String rollno) {
        boolean valid = true;
        if (TextUtils.isEmpty(name)) {
            nameET.setError("Invalid Name");
            nameET.requestFocus();
            valid = false;
        }
        if (TextUtils.isEmpty(email) && !email.contains("@") && !email.contains(".")) {
            emailET.setError("Invalid Email ID");
            emailET.requestFocus();
            valid = false;
        }
        if (pass.length() < 5) {
            passET.setError("Too short");
            passET.requestFocus();
            valid = false;
        }
        if (TextUtils.isEmpty(pass)) {
            passET.setError("Invalid password");
            passET.requestFocus();
            valid = false;
        }
        if (rollno.length() > 3 && rollno.length() < 10 && TextUtils.isEmpty(rollno)) {
            rollnoET.setError("Invalid input");
            rollnoET.requestFocus();
            valid = false;
        }
        if (type == 1 && libMagDB.verifyRollno(rollno)){
            rollnoET.setError("Already have an account for this rollno");
            rollnoET.requestFocus();
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
        mRegisterFormListener = (RegisterFormListener) activity;
    }

    public interface RegisterFormListener {
        void registerButton(View view, int type, String name, String email, String pass, String rollno, String course, String branch);
    }

}