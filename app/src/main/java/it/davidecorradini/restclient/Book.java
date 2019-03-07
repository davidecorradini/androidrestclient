package it.davidecorradini.restclient;

import java.util.Date;

public class Book {

    private long id;
    private String title;
    private String author;
    private Date releaseDate;
    private double price;

    public Book(String title, String author, Date releaseDate, double price) {
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.price = price;
    }

    public Book(long id, String title, String author, Date releaseDate, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date release_date) {
        this.releaseDate = release_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}