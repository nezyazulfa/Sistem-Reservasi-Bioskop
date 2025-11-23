package com.bioskop.tiket;

public class ExtraVip extends TiketDecorator {
    
    public ExtraVip(Tiket tiket) {
        super(tiket);
    }

    @Override
    public String getDeskripsi() {
        return super.getDeskripsi() + " (UPGRADE VIP SEAT)";
    }

    @Override
    public double getHarga() {
        return super.getHarga() + 50000; // Surcharge VIP
    }
}