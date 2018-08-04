package com.kumar.akshay.libmag.librarian;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kumar.akshay.libmag.LibMagDatabase.LibMagDBHelper;
import com.kumar.akshay.libmag.MainScreen;
import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.Student.AboutStudentFragment;
import com.kumar.akshay.libmag.librarian.AddBook.AddBookFragment;
import com.kumar.akshay.libmag.librarian.Fine.FineByStudentFragment;
import com.kumar.akshay.libmag.librarian.IssuedBooks.IssuedBooksFragment;
import com.kumar.akshay.libmag.librarian.LoginRequest.LoginRequestAdpater;
import com.kumar.akshay.libmag.librarian.LoginRequest.LoginRequestFragment;
import com.kumar.akshay.libmag.librarian.User.UserFragment;
import com.kumar.akshay.libmag.login.MainActivity;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

public class LibrarianActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BooksFragment.BooksFragmentListener,
        IssueBookDialogFragment.IssueBookDialogListener, LoginRequestAdpater.LoginInterface {

    Fragment fragment1, fragment2;
    static String mUserEmail, mUid;
    ActionMode mActionMode = null;
    android.view.ActionMode actionMode1 = null;
    BookMessage bookMessage = null;
    UsersObject user = null;
    public static boolean isBasicFragment = true;
    static int count = 0;
    LibMagDBHelper libMagDB;
    ActionMode actionMode = null;
    UsersObject user1 = null;
    Context context;
    View view;
    DatabaseReference databaseReference1, databaseReference2;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);`    q
        context = this;
        libMagDB = new LibMagDBHelper(LibrarianActivity.this);
        setContentView(R.layout.activity_librarian);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mUserEmail = getIntent().getExtras().getString("mUserEmail");
        mUid = getIntent().getExtras().getString("mUid");
        view = findViewById(R.id.librarianRelativeLayout);
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("books");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Opening the required fragment with specific arguments
        fragment1 = new BooksFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("searchFrag", false);
        bundle.putString("mUserEmail", mUserEmail);
        bundle.putString("mUid", mUid);
        fragment1.setArguments(bundle);
        switchContentWithBackStack(fragment1, fragment1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!isBasicFragment) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                super.onBackPressed();
            } else {
                if (count == 0){
                    Toast.makeText(this, "Press back again to exit", Toast.LENGTH_LONG).show();
                    count ++;
                } else if (count == 1) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    isBasicFragment = true;
                    count = 0;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.librarian, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        isBasicFragment = false;
        switch (id) {
            case R.id.action_add_book:
                fragment2 = new AddBookFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mUserEmail", mUserEmail);
                bundle.putString("mUid", mUid);
                fragment2.setArguments(bundle);
                switchContentWithBackStack(fragment1, fragment2);
                fragment1 = fragment2;
                return true;
            case R.id.action_search_book:
                fragment2 = new SearchFragment();
                switchContentWithBackStack(fragment1, fragment2);
                fragment1 = fragment2;
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        isBasicFragment = true;

        if (id == R.id.nav_books) {
            //Shows all the books in the library
            fragment2 = new BooksFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("searchFrag", false);
            bundle.putString("mUserEmail", mUserEmail);
            bundle.putString("mUid", mUid);
            fragment2.setArguments(bundle);
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        } else if (id == R.id.nav_issue_books) {
            //Show all issued books
            fragment2 = new IssuedBooksFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("librarian", true);
            fragment2.setArguments(bundle);
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        } else if (id == R.id.nav_issue_a_book) {
            //Issuing a book
            DialogFragment df = new IssueABookFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("issueFrag", true);
            df.setArguments(bundle);
            df.show(getSupportFragmentManager(), "issueABookDialogFragment");
            //switchContentWithBackStack(fragment1, fragment2);
            //fragment1 = fragment2;
        } else if (id == R.id.nav_return_a_book) {
            //Returning a book
            DialogFragment df = new IssueABookFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("issueFrag", false);
            df.setArguments(bundle);
            df.show(getSupportFragmentManager(), "issueABookDialogFragment");
        } else if (id == R.id.nav_fine_book) {
            //Checks fine by book
            fragment2 = new FineByStudentFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("fineStud", false);
            fragment2.setArguments(bundle);
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        } else if (id == R.id.nav_fine_student) {
            //Checks fine by student
            fragment2 = new FineByStudentFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("fineStud", true);
            fragment2.setArguments(bundle);
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        } else if (id == R.id.nav_about_lib) {
            //Display info about the librarian
            fragment2 = new AboutStudentFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("studFrag", false);
            fragment2.setArguments(bundle);
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        } else if (id == R.id.nav_signout) {
            //Signout user and opens login screen
            FirebaseAuth.getInstance().signOut();
            MainScreen.updatingPreferences(-1, "", "");
            finish();
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(LibrarianActivity.this, "Signned out", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_student_login_requests) {
            //Creates a new user account
            fragment2 = new LoginRequestFragment();
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        } else if (id == R.id.nav_show_all_users) {
            //Creates a new user account
            fragment2 = new UserFragment();
            switchContentWithBackStack(fragment1, fragment2);
            fragment1 = fragment2;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchContentWithBackStack(Fragment fragment1, Fragment fragment2) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment1);
        ft.commit();
        ft.addToBackStack(null);
        ft.add(R.id.fragmentLibrarian, fragment2);
        //ft.commit();
    }

    ActionMode.Callback mActionModeCallbackBooks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.books_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.editBookMenu:
                    DialogFragment editBookDetailsFragment = new EditBookDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("bookId", Integer.toString(bookMessage.getBookId()));
                    editBookDetailsFragment.setArguments(bundle);
                    editBookDetailsFragment.show(getSupportFragmentManager(), "editBookDetailsDialog");
                    Intent i = new Intent(LibrarianActivity.this, LibrarianActivity.class);
                    i.putExtra("mUserEmail", mUserEmail);
                    i.putExtra("mUid", mUid);
                    startActivity(i);
                    break;
                case R.id.removeBookMenu:
                    String key = libMagDB.getKeyFromBooksTable(Integer.toString(bookMessage.getBookId()));
                    BookMessage bookMessgae = libMagDB.getABook(Integer.toString(bookMessage.getBookId()));
                    if (bookMessgae.getBookIssueDate().equals("NIL")) {
                        databaseReference1.child(key).removeValue();
                        Intent i1 = new Intent(LibrarianActivity.this, LibrarianActivity.class);
                        i1.putExtra("mUserEmail", mUserEmail);
                        i1.putExtra("mUid", mUid);
                        startActivity(i1);
                    } else {
                        Snackbar.make(view, "Book alredy issued so it can be removed", Snackbar.LENGTH_LONG).show();
                    }

            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            getSupportActionBar().show();
        }
    };

//    ActionMode.Callback mSearchMenuCallBack = new ActionMode.Callback() {
//
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            LayoutInflater inflater = LayoutInflater.from(LibrarianActivity.this);
//            View view = inflater.inflate(R.layout.search_menu_layout, null);
//            autoCompleteTextView = view.findViewById(R.id.searchAutoCompleteTextView);
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(LibrarianActivity.this, android.R.layout.simple_list_item_1, libMagDB.getAllBookIds());
//            autoCompleteTextView.setAdapter(arrayAdapter);
//            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    String bookId = autoCompleteTextView.getText().toString();
//                    fragment2 = new BooksFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("searchFrag", true);
//                    bundle.putString("bookId", bookId);
//                    fragment2.setArguments(bundle);
//                    switchContentWithBackStack(fragment1, fragment2);
//                    fragment1 = fragment2;
//                }
//            });
//            mode.setCustomView(view);
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            return false;
//        }
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            mActionMode = null;
//            getSupportActionBar().show();
//        }
//    };

    @Override
    public boolean showBooksMenu(BookMessage bookMessage) {
        if (mActionMode != null) {
            return false;
        }
        getSupportActionBar().hide();
        this.bookMessage = bookMessage;
        mActionMode = startSupportActionMode(mActionModeCallbackBooks);
        return true;
    }

    @Override
    public void onIssueBookClick(String rollno) {
        IssueABookFragment.issueBook(new LibMagDBHelper(this), databaseReference1, databaseReference2, Integer.toString(ShowASpecificBookFragment.bookId), rollno, view);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void addUserMenu(UsersObject user, View view1) {
        Log.e("LibrarianActivity", "User menu reached");
        user1 = user;
        registerForContextMenu(view1);
//        if (actionMode1 == null)
//            actionMode1 = startActionMode(mActionModeCallbackUsers); /*startSupportActionMode(mActionModeCallbackUsers);*/

    }

}