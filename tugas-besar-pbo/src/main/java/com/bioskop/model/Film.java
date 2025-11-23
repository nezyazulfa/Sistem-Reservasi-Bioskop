package com.bioskop.model;

public class Film {
    private String title;
    private String genre;
    private double price;

    // Constructor
    public Film(String title, String genre, double price) {
        this.title = title;
        this.genre = genre;
        this.price = price;
    }

    // Getters (Clean Code: Immutable fields where possible)
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return title + " (" + genre + ") - Rp" + price;
    }
}