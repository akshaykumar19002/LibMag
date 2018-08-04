package com.kumar.akshay.libmag.ObjectClasses;

public class RIBObject {

    String rollno, book_id, book_name, book_issue_date, book_return_date;

    public RIBObject() {
    }

    public RIBObject(String rollno, String book_id, String book_name, String book_issue_date, String book_return_date) {
        this.rollno = rollno;
        this.book_id = book_id;
        this.book_name = book_name;
        this.book_issue_date = book_issue_date;
        this.book_return_date = book_return_date;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_issue_date() {
        return book_issue_date;
    }

    public void setBook_issue_date(String book_issue_date) {
        this.book_issue_date = book_issue_date;
    }

    public String getBook_return_date() {
        return book_return_date;
    }

    public void setBook_return_date(String book_return_date) {
        this.book_return_date = book_return_date;
    }
}
