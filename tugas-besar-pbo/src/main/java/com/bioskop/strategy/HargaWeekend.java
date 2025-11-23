package com.bioskop.strategy;

public class HargaWeekend implements PricingStrategy {
    @Override
    public double hitungHargaAkhir(double hargaAwal) {
        return hargaAwal + 10000; // Surcharge 10rb saat libur
    }
}