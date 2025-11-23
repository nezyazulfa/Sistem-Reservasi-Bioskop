package com.bioskop.tiket;

import com.bioskop.model.Film;

public class TiketFilm implements Tiket {
    private Film film;

    public TiketFilm(Film film) {
        this.film = film;
    }

    @Override
    public String getDeskripsi() {
        return "Tiket: " + film.getTitle();
    }

    @Override
    public double getHarga() {
        return film.getPrice();
    }
}