package com.bioskop.tiket;

public class ExtraPopcorn extends TiketDecorator {
    
    public ExtraPopcorn(Tiket tiket) {
        super(tiket);
    }

    @Override
    public String getDeskripsi() {
        return super.getDeskripsi() + " + Popcorn Large";
    }

    @Override
    public double getHarga() {
        return super.getHarga() + 25000; // Harga Popcorn
    }
}