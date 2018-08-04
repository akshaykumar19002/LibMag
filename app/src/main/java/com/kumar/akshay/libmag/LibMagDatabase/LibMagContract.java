package com.kumar.akshay.libmag.LibMagDatabase;

public class LibMagContract {

    public static final String DATABASE_NAME = "LibMagDB";
    public static final int DATABASE_VERSION = 1;

    //Tables
    public static final String BOOKS_TABLE_NAME = "BOOKS";
    public static final String STUDENTS_TABLE_NAME = "STUDENTS";
    public static final String RECENTLY_ISSUED_TABLE_NAME = "RECENTLY_ISSUED";
    public static final String REQUEST_USER_TABLE_NAME = "REQ_USER";

    //Books table columns
    public static final String BOOK_KEY_VALUE_COLUMN = "BOOK_KEY";
    public static final String BOOK_ID_COLUMN = "BOOK_ID";
    public static final String BOOK_NAME_COLUMN = "BOOK_NAME";
    public static final String BOOK_AUTHOR_COLUMN = "AUTHOR_NAME";
    public static final String BOOK_PUBLISHER_COLUMN = "PUBLISHER_NAME";
    public static final String BOOK_EDITION_COLUMN = "EDITION";
    public static final String BOOK_DESCRIPTION_COLUMN = "BOOK_DESCRIPTION";
    public static final String BOOK_ISSUE_DATE_COLUMN = "BOOK_ISSUE_DATE";
    public static final String BOOK_FINE_COLUMN = "BOOK_FINE";
    public static final String BOOK_ISSUED_TO_COLUMN = "ISSUED_TO";
    public static final String BOOK_LOCATION_COLUMN = "BOOK_LOCATION";

    //Students table columns
    public static final String STUDENT_KEY_COLUMN = "STUDENT_KEY";
    public static final String STUDENT_NAME_COLUMN = "STUDENT_NAME";
    public static final String STUDENT_EMAIL_COLUMN = "STUDENT_EMAIL";
    public static final String STUDENT_PASS_COLUMN = "STUDNET_PASS";
    public static final String STUDENT_ROLLNO_COLUMN = "STUDENT_ROLLNO";
    public static final String STUDENT_COURSE_COLUMN = "STUDENT_COURSE";
    public static final String STUDENT_BRANCH_COLUMN = "STUDENT_BRANCH";
    public static final String STUDENT_ISSUED_BOOKS_COLUMN = "ISSUED_BOOKS";

    //Recently issued books table columns
    public static final String RECENTLY_ISSUED_BOOK_KEY = "RI_KEY";
    public static final String RECENTLY_ISSUED_STUDENT_ROLLNO = "RI_STUD_ROLLNO";
    public static final String RECENTLY_ISSUED_BOOK_ID = "ISSUED_BOOK_ID";
    public static final String RECENTLY_ISSUED_BOOK_NAME = "ISSUED_BOOK_NAME";
    public static final String RECENTLY_ISSUED_BOOK_ISSUE_DATE = "ISSUED_BOOK_ISSUE_DATE";
    public static final String RECENTLY_ISSUED_BOOK_RETURN_DATE = "ISSUED_BOOK_RETURN_DATE";

    //Request User table columns
    public static final String REQUEST_USER_KEY = "REQ_USER_KEY";
    public static final String REQUEST_USER_NAME_COLUMN = "REQ_USER_NAME";
    public static final String REQUEST_USER_EMAIL_COLUMN = "REQ_USER_EMAIL";
    public static final String REQUEST_USER_PASS_COLUMN = "REQ_USER_PASS";
    public static final String REQUEST_USER_ROLLNO_COLUMN = "REQ_USER_ROLLNO";
    public static final String REQUEST_USER_COURSE_COLUMN = "REQ_USER_COURSE";
    public static final String REQUEST_USER_BRANCH_COLUMN = "REQ_USER_BRANCH";

    //Create table statement for books table
    public static final String BOOKS_TABLE_CREATE_STMT = "CREATE TABLE " + BOOKS_TABLE_NAME + " ( " +
            BOOK_KEY_VALUE_COLUMN + " TEXT, " + BOOK_ID_COLUMN + " INTEGER, " + BOOK_NAME_COLUMN + " TEXT, "
            + BOOK_AUTHOR_COLUMN + " TEXT, " + BOOK_PUBLISHER_COLUMN +  " TEXT, " + BOOK_EDITION_COLUMN + " TEXT, "
            + BOOK_DESCRIPTION_COLUMN + " TEXT, " + BOOK_ISSUE_DATE_COLUMN  + "  TEXT, " + BOOK_FINE_COLUMN +
            "  TEXT, " + BOOK_ISSUED_TO_COLUMN + " TEXT, " + BOOK_LOCATION_COLUMN + " TEXT )";

    //Create table statement for students table
    public static final String STUDENT_TABLE_CREATE_STMT = "CREATE TABLE " + STUDENTS_TABLE_NAME + " ( "
            + STUDENT_KEY_COLUMN + " TEXT, " + STUDENT_NAME_COLUMN + " TEXT, " + STUDENT_EMAIL_COLUMN + " TEXT, "
            + STUDENT_PASS_COLUMN + " TEXT, " + STUDENT_ROLLNO_COLUMN + " TEXT, " + STUDENT_COURSE_COLUMN + " TEXT, "
            + STUDENT_BRANCH_COLUMN + " TEXT, " + STUDENT_ISSUED_BOOKS_COLUMN + " TEXT )";

    //Create table statement for recently issued table
    public static final String RECENTLY_ISSUED_CREATE_TABLE_STMT = "CREATE TABLE " + RECENTLY_ISSUED_TABLE_NAME + " ( "
            + RECENTLY_ISSUED_BOOK_KEY + " TEXT, "+ RECENTLY_ISSUED_STUDENT_ROLLNO + " TEXT, " + RECENTLY_ISSUED_BOOK_ID + " TEXT, "
            + RECENTLY_ISSUED_BOOK_NAME + " TEXT, " + RECENTLY_ISSUED_BOOK_ISSUE_DATE + " TEXT, "
            + RECENTLY_ISSUED_BOOK_RETURN_DATE + " TEXT ) ";

    //Create table statement for request for new users
    public static final String REQUEST_USER_TABLE_STMT = "CREATE TABLE " + REQUEST_USER_TABLE_NAME + " ( "
            + REQUEST_USER_KEY + " TEXT, " + REQUEST_USER_NAME_COLUMN + " TEXT, " + REQUEST_USER_EMAIL_COLUMN + " TEXT, "
            + REQUEST_USER_PASS_COLUMN + " TEXT, " + REQUEST_USER_ROLLNO_COLUMN + " TEXT, " + REQUEST_USER_COURSE_COLUMN + " TEXT, "
            + REQUEST_USER_BRANCH_COLUMN + " TEXT )";

}
