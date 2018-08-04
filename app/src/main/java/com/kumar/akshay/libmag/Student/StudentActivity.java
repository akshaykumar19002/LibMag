package com.kumar.akshay.libmag.Student;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.librarian.Fine.FineByStudentFragment;
import com.kumar.akshay.libmag.librarian.IssuedBooks.*;
import com.kumar.akshay.libmag.login.MainActivity;

public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment1, fragment2;
    public static String email;
    LibMagDBHelper libMagDB;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        libMagDB = new LibMagDBHelper(this);
        setContentView(R.layout.activity_student);
        relativeLayout = (RelativeLayout) findViewById(R.id.studentRelativeLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        email = getIntent().getExtras().getString("mUserEmail");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragment1 = new IssuedBooksFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("librarian", false);
        bundle.putString("email", email);
        fragment1.setArguments(bundle);
        switchContentWithBackStack(fragment1, fragment1);

        getSupportActionBar().setTitle("Student : " + libMagDB.getAUserName(email));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_issued_books) {
            fragment2 = new IssuedBooksFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("librarian", false);
            bundle.putString("email", email);
            fragment2.setArguments(bundle);
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        } else if (id == R.id.nav_current_fine) {
            fragment2 = new FineFragment();
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            fragment2.setArguments(bundle);
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        } else if (id == R.id.nav_availability_book) {
            fragment2 = new BookAvailability();
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        } else if (id == R.id.nav_recently_issued_book) {
            fragment2 = new RecentlyIssuedBooksFragment();
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            fragment2.setArguments(bundle);
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        } else if (id == R.id.nav_about) {
            fragment2 = new AboutStudentFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("studFrag", true);
            fragment2.setArguments(bundle);
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        }else if (id == R.id.nav_signout){
            FirebaseAuth.getInstance().signOut();
            MainScreen.updatingPreferences(-1, "", "");
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchContentWithBackStack(Fragment fragment1, Fragment fragment2) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment1);
        ft.commit();
        ft.add(R.id.studentRelativeLayout, fragment2);
    }

}
