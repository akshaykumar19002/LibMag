package com.kumar.akshay.libmag.ObjectClasses;


public class BookMessage {

    public String bookName, bookAuthor, bookPublisher, bookIssueDate, bookEdition, bookDescription, bookIssuedTo, bookLocation;
    int bookId;

    public BookMessage() {
    }

    public BookMessage(int bookId, String bookName, String bookAuthor, String bookPublisher, String bookIssueDate, String bookEdition, String bookDescription, String bookIssuedTo, String bookLocation) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.bookIssueDate = bookIssueDate;
        this.bookEdition = bookEdition;
        this.bookDescription = bookDescription;
        this.bookIssuedTo = bookIssuedTo;
        this.bookId = bookId;
        this.bookLocation = bookLocation;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookIssueDate() {
        return bookIssueDate;
    }

    public void setBookIssueDate(String bookIssueDate) {
        this.bookIssueDate = bookIssueDate;
    }

    public String getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(String bookEdition) {
        this.bookEdition = bookEdition;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookIssuedTo() {
        return bookIssuedTo;
    }

    public void setBookIssuedTo(String bookIssuedTo) {
        this.bookIssuedTo = bookIssuedTo;
    }

    public String getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(String bookLocation) {
        this.bookLocation = bookLocation;
    }
}
