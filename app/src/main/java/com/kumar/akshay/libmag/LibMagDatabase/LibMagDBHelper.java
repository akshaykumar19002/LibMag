package com.kumar.akshay.libmag.LibMagDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kumar.akshay.libmag.ObjectClasses.BookMessage;
import com.kumar.akshay.libmag.ObjectClasses.RIBObject;
import com.kumar.akshay.libmag.ObjectClasses.UsersObject;

import java.util.ArrayList;

import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOKS_TABLE_CREATE_STMT;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOKS_TABLE_NAME;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOK_AUTHOR_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOK_DESCRIPTION_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOK_EDITION_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOK_FINE_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOK_ID_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOK_ISSUED_TO_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOK_ISSUE_DATE_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOK_KEY_VALUE_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOK_NAME_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.BOOK_PUBLISHER_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.DATABASE_NAME;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.DATABASE_VERSION;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.RECENTLY_ISSUED_BOOK_ID;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.RECENTLY_ISSUED_BOOK_ISSUE_DATE;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.RECENTLY_ISSUED_BOOK_KEY;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.RECENTLY_ISSUED_BOOK_NAME;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.RECENTLY_ISSUED_BOOK_RETURN_DATE;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.RECENTLY_ISSUED_CREATE_TABLE_STMT;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.RECENTLY_ISSUED_STUDENT_ROLLNO;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.RECENTLY_ISSUED_TABLE_NAME;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.REQUEST_USER_BRANCH_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.REQUEST_USER_COURSE_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.REQUEST_USER_EMAIL_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.REQUEST_USER_KEY;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.REQUEST_USER_NAME_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.REQUEST_USER_PASS_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.REQUEST_USER_ROLLNO_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.REQUEST_USER_TABLE_NAME;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.REQUEST_USER_TABLE_STMT;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.STUDENTS_TABLE_NAME;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.STUDENT_BRANCH_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.STUDENT_COURSE_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.STUDENT_EMAIL_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.STUDENT_ISSUED_BOOKS_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.STUDENT_KEY_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.STUDENT_NAME_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.STUDENT_PASS_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.STUDENT_ROLLNO_COLUMN;
import static com.kumar.akshay.libmag.LibMagDatabase.LibMagContract.STUDENT_TABLE_CREATE_STMT;

public class LibMagDBHelper extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;

    public LibMagDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = getWritableDatabase();
    }

    public void recreateTables() {
        onUpgrade(sqLiteDatabase, 0, 0);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BOOKS_TABLE_CREATE_STMT);
        db.execSQL(STUDENT_TABLE_CREATE_STMT);
        db.execSQL(RECENTLY_ISSUED_CREATE_TABLE_STMT);
        db.execSQL(REQUEST_USER_TABLE_STMT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + BOOKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RECENTLY_ISSUED_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REQUEST_USER_TABLE_NAME);
        onCreate(db);
    }

    //Books table
    public boolean insertIntoBooksTable(String book_key, int book_id, String book_name, String book_issue_date, String book_fine,
                                        String author_name, String publisher, String edition, String description, String issuedTo) {
        ContentValues cv = new ContentValues();
        cv.put(BOOK_KEY_VALUE_COLUMN, book_key);
        cv.put(BOOK_ID_COLUMN, book_id);
        cv.put(BOOK_NAME_COLUMN, book_name);
        cv.put(BOOK_AUTHOR_COLUMN, author_name);
        cv.put(BOOK_PUBLISHER_COLUMN, publisher);
        cv.put(BOOK_EDITION_COLUMN, edition);
        cv.put(BOOK_DESCRIPTION_COLUMN, description);
        cv.put(BOOK_ISSUE_DATE_COLUMN, book_issue_date);
        cv.put(BOOK_FINE_COLUMN, book_fine);
        cv.put(BOOK_ISSUED_TO_COLUMN, issuedTo);
        long result = sqLiteDatabase.insert(BOOKS_TABLE_NAME, null, cv);
        if (result <= 0) {
            return false;
        } else
            return true;
    }

    public String getKeyFromBooksTable(String book_id) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + BOOK_KEY_VALUE_COLUMN + " FROM " + BOOKS_TABLE_NAME +
                        " WHERE " + BOOK_ID_COLUMN + " =?",
                new String[]{book_id});
        if (cursor != null) {
            if (cursor.moveToNext()) {
                return cursor.getString(0);
            }
        } else
            return "Incorrect Data Sent";
        return null;
    }

    public boolean removeABook(String key, String book_Id) {
        int result = sqLiteDatabase.delete(BOOKS_TABLE_NAME, BOOK_KEY_VALUE_COLUMN + " = ? AND " + BOOK_ID_COLUMN + " = ?",
                new String[]{key, book_Id});
        if (result > 0) {
            return true;
        } else return false;
    }

    public boolean verifyBookId(String book_id) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + BOOK_ID_COLUMN + " FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOK_ID_COLUMN + " = ?", new String[]{book_id});
        if (cursor.moveToNext()) {
            if (book_id.equals(Integer.toString(cursor.getInt(0))))
                return true;
        }
        return false;
    }

    public BookMessage getABook(String book_id) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME +
                " WHERE " + BOOK_ID_COLUMN + " = ?", new String[]{book_id});
        if (cursor.moveToNext()) {
            return new BookMessage(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(7), cursor.getString(5), cursor.getString(6), cursor.getString(9), cursor.getString(10));
        }
        return null;
    }

    public String getFineForBook(String book_id) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + BOOK_FINE_COLUMN + " FROM " + BOOKS_TABLE_NAME
                + " WHERE " + BOOK_ID_COLUMN + " = ?", new String[]{book_id});
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        } else
            return "0";
    }

    public ArrayList<String> getAllBookIds() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + BOOK_ID_COLUMN + " FROM " + BOOKS_TABLE_NAME, null);
        ArrayList<String> bookIds = new ArrayList<>();
        while (cursor.moveToNext()){
            bookIds.add(cursor.getString(0));
        }
        return bookIds;
    }


    //Students table
    public boolean insertIntoStudentsTable(String key, String name, String email, String pass, String rollno, String course, String branch, String issue_books) {
        ContentValues cv = new ContentValues();
        cv.put(STUDENT_KEY_COLUMN, key);
        cv.put(STUDENT_NAME_COLUMN, name);
        cv.put(STUDENT_EMAIL_COLUMN, email);
        cv.put(STUDENT_PASS_COLUMN, pass);
        cv.put(STUDENT_ROLLNO_COLUMN, rollno);
        cv.put(STUDENT_COURSE_COLUMN, course);
        cv.put(STUDENT_BRANCH_COLUMN, branch);
        cv.put(STUDENT_ISSUED_BOOKS_COLUMN, issue_books);
        long result = sqLiteDatabase.insert(STUDENTS_TABLE_NAME, null, cv);
        if (result >= 0) {
            return true;
        } else
            return false;
    }

    public String getKeyFromStudentsTable(String rollno) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + STUDENT_KEY_COLUMN + " FROM " + STUDENTS_TABLE_NAME
                + " WHERE " + STUDENT_ROLLNO_COLUMN + " = ?", new String[]{rollno});
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        } else
            return null;
    }

    public boolean verifyRollno(String rollno) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + STUDENT_ROLLNO_COLUMN + " FROM " + STUDENTS_TABLE_NAME +
                " WHERE " + STUDENT_ROLLNO_COLUMN + " = ? ", new String[]{rollno});
        if (cursor.moveToNext()) {
            String rr = cursor.getString(0);
            if (cursor.getString(0).equals(rollno)) {
                return true;
            } else
                return false;
        }
        return false;
    }

    public UsersObject getAStudent(String key) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + STUDENTS_TABLE_NAME +
                " WHERE " + STUDENT_KEY_COLUMN + " = ? ", new String[]{key});
        if (cursor.moveToNext()) {
            return new UsersObject(2, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        } else
            return null;
    }

    public UsersObject getAStudentUsinEmail(String email) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + STUDENTS_TABLE_NAME +
                " WHERE " + STUDENT_EMAIL_COLUMN + " = ? ", new String[]{email});
        if (cursor.moveToNext()) {
            return new UsersObject(2, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        } else
            return null;
    }

    public boolean removeAStudent(String key, String rollno) {
        int result = sqLiteDatabase.delete(STUDENTS_TABLE_NAME, STUDENT_KEY_COLUMN + " = ? AND " + STUDENT_ROLLNO_COLUMN + " = ?", new String[]{key, rollno});
        if (result > 0)
            return true;
        else
            return false;
    }

    public ArrayList<String> getAllRollno() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + STUDENT_ROLLNO_COLUMN + " FROM " + STUDENTS_TABLE_NAME, null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(0));
        }
        return arrayList;
    }

    public String getAUserName(String email) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + STUDENT_NAME_COLUMN + " FROM " + STUDENTS_TABLE_NAME + " WHERE "
                + STUDENT_EMAIL_COLUMN + " =?", new String[]{email});
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return null;
    }

    public String getARollno(String email) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + STUDENT_ROLLNO_COLUMN + " FROM " + STUDENTS_TABLE_NAME + " WHERE "
                + STUDENT_EMAIL_COLUMN + " = ?", new String[]{email});
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return "0";
    }


    //Recently issued books
    public boolean insertIntoRIBTable(String key, String rollno, String issued_book_id, String book_name, String issue_date, String return_date) {
        ContentValues cv = new ContentValues();
        cv.put(RECENTLY_ISSUED_BOOK_KEY, key);
        cv.put(RECENTLY_ISSUED_STUDENT_ROLLNO, rollno);
        cv.put(RECENTLY_ISSUED_BOOK_ID, issued_book_id);
        cv.put(RECENTLY_ISSUED_BOOK_NAME, book_name);
        cv.put(RECENTLY_ISSUED_BOOK_ISSUE_DATE, issue_date);
        cv.put(RECENTLY_ISSUED_BOOK_RETURN_DATE, return_date);
        long result = sqLiteDatabase.insert(RECENTLY_ISSUED_TABLE_NAME, null, cv);
        if (result >= 0) {
            return true;
        } else
            return false;
    }

    public ArrayList<String> getIssuedBooks(String rollno) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + RECENTLY_ISSUED_BOOK_ID + "," + RECENTLY_ISSUED_BOOK_NAME
                + "," + RECENTLY_ISSUED_BOOK_ISSUE_DATE + "," + RECENTLY_ISSUED_BOOK_RETURN_DATE + " FROM " + RECENTLY_ISSUED_TABLE_NAME
                + " WHERE " + RECENTLY_ISSUED_STUDENT_ROLLNO + " = ?", new String[]{rollno});
        ArrayList<String> al = new ArrayList<>();
        while (cursor.moveToNext()) {
            String details = cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3);
            al.add(details);
        }
        return al;
    }

    public boolean removeARIB(String key) {
        int result = sqLiteDatabase.delete(RECENTLY_ISSUED_TABLE_NAME, RECENTLY_ISSUED_BOOK_KEY + " = ?", new String[]{key});
        if (result > 0) {
            return true;
        } else
            return false;
    }

    public RIBObject getARIB(String rollno, String bookId){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + RECENTLY_ISSUED_TABLE_NAME + " WHERE "
                + RECENTLY_ISSUED_STUDENT_ROLLNO + " = ? AND " + RECENTLY_ISSUED_BOOK_ID + " = ?", new String[]{rollno, bookId});
        if (cursor.moveToNext()){
            return new RIBObject(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        }
        return  null;
    }

    public String getKeyFromRIBTable(String rollno, String bookId){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + RECENTLY_ISSUED_BOOK_KEY + " FROM " + RECENTLY_ISSUED_TABLE_NAME + " WHERE "
                + RECENTLY_ISSUED_STUDENT_ROLLNO + " = ? AND " + RECENTLY_ISSUED_BOOK_ID + " = ?", new String[]{rollno, bookId});
        if (cursor.moveToNext()){
            return cursor.getString(0);
        }
        return  null;
    }

    //Request users table
    public boolean insertUserRequestIntoRequestTable(String key, String name, String email, String pass, String rollno, String course, String branch){
        ContentValues cv = new ContentValues();
        cv.put(REQUEST_USER_KEY, key);
        cv.put(REQUEST_USER_NAME_COLUMN, name);
        cv.put(REQUEST_USER_EMAIL_COLUMN, email);
        cv.put(REQUEST_USER_PASS_COLUMN, pass);
        cv.put(REQUEST_USER_ROLLNO_COLUMN, rollno);
        cv.put(REQUEST_USER_COURSE_COLUMN, course);
        cv.put(REQUEST_USER_BRANCH_COLUMN, branch);
        long result = sqLiteDatabase.insert(REQUEST_USER_TABLE_NAME, null, cv);
        if (result >= 0) {
            return true;
        } else
            return false;
    }

    public ArrayList<String> getAllUserRequests(String rollno) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + REQUEST_USER_NAME_COLUMN + "," + REQUEST_USER_EMAIL_COLUMN
                + "," + REQUEST_USER_PASS_COLUMN + "," + REQUEST_USER_ROLLNO_COLUMN + "," + REQUEST_USER_COURSE_COLUMN
                + "," + REQUEST_USER_BRANCH_COLUMN + " FROM " + REQUEST_USER_TABLE_NAME, null);
        ArrayList<String> al = new ArrayList<>();
        while (cursor.moveToNext()) {
            String details = cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3)
                    + "," + cursor.getString(4) + "," + cursor.getString(5);
            al.add(details);
        }
        return al;
    }

    public boolean removeARequest(String key, String rollno) {
        int result = sqLiteDatabase.delete(REQUEST_USER_TABLE_NAME, REQUEST_USER_KEY + " = ? AND " + REQUEST_USER_ROLLNO_COLUMN + " = ?", new String[]{key, rollno});
        if (result > 0)
            return true;
        else
            return false;
    }

    public String getAKeyFromUserRequest(String rollno){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + REQUEST_USER_KEY + " FROM " + REQUEST_USER_TABLE_NAME
                + " WHERE " + REQUEST_USER_ROLLNO_COLUMN + " = ?", new String[]{rollno});
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        } else
            return null;
    }
}
