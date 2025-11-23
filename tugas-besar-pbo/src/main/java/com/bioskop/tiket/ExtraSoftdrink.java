package com.bioskop.tiket;

public class ExtraSoftdrink extends TiketDecorator {
    
    public ExtraSoftdrink(Tiket tiket) {
        super(tiket);
    }

    @Override
    public String getDeskripsi() {
        return super.getDeskripsi() + " + Softdrink";
    }

    @Override
    public double getHarga() {
        return super.getHarga() + 15000; // Harga Softdrink 15.000
    }
}