package com.bioskop.model;

import java.io.Serializable;

// IMPLEMENTS SERIALIZABLE AGAR BISA DISIMPAN KE FILE
public class Film implements Serializable {
    private static final long serialVersionUID = 1L; // Versi Serialization

    private String title;
    private String genre;
    private double price;
    private int stock;

    public Film(String title, String genre, double price, int stock) {
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.stock = stock;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void reduceStock(int amount) {
        if (amount <= stock) {
            this.stock -= amount;
        }
    }
}