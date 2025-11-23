package com.bioskop.strategy;

public class HargaNormal implements PricingStrategy {
    @Override
    public double hitungHargaAkhir(double hargaAwal) {
        return hargaAwal; // Tidak ada perubahan
    }
}