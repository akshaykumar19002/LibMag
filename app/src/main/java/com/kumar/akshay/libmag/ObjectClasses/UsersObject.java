package com.kumar.akshay.libmag.ObjectClasses;


public class UsersObject {

    public int type;
    public String name, email, pass, rollno, course, branch, issuedBooks;

    public UsersObject() {
    }

    public UsersObject(int type, String name, String email, String pass, String rollno, String course, String branch, String issuedBooks) {
        this.type = type;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.rollno = rollno;
        this.course = course;
        this.branch = branch;
        this.issuedBooks = issuedBooks;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIssuedBooks() {
        return issuedBooks;
    }

    public void setIssuedBooks(String issuedBooks) {
        this.issuedBooks = issuedBooks;
    }
}
