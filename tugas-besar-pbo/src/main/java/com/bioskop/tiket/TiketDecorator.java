package com.bioskop.tiket;

// Abstract class implement interface Tiket
public abstract class TiketDecorator implements Tiket {
    protected Tiket tiket; // Reference ke objek yang akan dihias

    protected TiketDecorator(Tiket tiket) {
        this.tiket = tiket;
    }

    @Override
    public String getDeskripsi() {
        return tiket.getDeskripsi();
    }

    @Override
    public double getHarga() {
        return tiket.getHarga();
    }
}