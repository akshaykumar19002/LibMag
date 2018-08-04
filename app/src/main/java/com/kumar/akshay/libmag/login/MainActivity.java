package com.kumar.akshay.libmag.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kumar.akshay.libmag.R;

public class MainActivity extends FragmentActivity implements LoginFragment.LoginFormListener, RegisterFragment.RegisterFormListener, FragmentChangeListener {

    String TAG = getClass().getSimpleName();
    private LoginAuthTask mLoginAuthTask = null;
    private Fragment fragment1 = null;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ft = getSupportFragmentManager().beginTransaction();
        fragment1 = new LoginFragment();
        setContentView(R.layout.activity_main);
        changeFragment(fragment1, fragment1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loginButtonHadler(int type, String email, String pass) {
        //Handles login button clicks for Login Fragment
        //showProgress(MainActivity.this, true);
        attemptLogin(type, email, pass);
    }

    @Override
    public void changeTheFragment(Fragment fragment) {
        //It changes the fragment which register button is clicked from the login fragment
        changeFragment(fragment1, fragment);
    }

    private void attemptLogin(int currentItem, String email, String pass) {
        mLoginAuthTask = new LoginAuthTask(MainActivity.this, currentItem, email, pass);
        mLoginAuthTask.execute((Void) null);
    }

//    public void registerButtonHandler(View view) {
//        //Handles register button click for Login Fragment
//        mFragmentChangeListener.changeFragment(new RegisterFragment());
//    }

    @Override
    public void registerButton(View view, int type, String name, String email, String pass, String rollno, String course, String branch) {
        //handles register button clicks for Register fragment
        //showProgress(MainActivity.this, true);
        new RegisterTask(MainActivity.this, view, type, name, email, pass, rollno, course, branch).execute((Void) null);
    }

    public void loginButtonHandler(View view) {
        //Handles login button click for Register Fragment
        changeFragment(fragment1, new LoginFragment());
    }

    @Override
    public void changeFragment(Fragment fragment, Fragment fragment2) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
        ft.add(R.id.main_content, fragment2);
        fragment1 = fragment2;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}