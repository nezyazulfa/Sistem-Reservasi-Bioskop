package com.bioskop.strategy;

public class DiskonMember implements PricingStrategy {
    @Override
    public double hitungHargaAkhir(double hargaAwal) {
        return hargaAwal * 0.90; // Diskon 10%
    }
}