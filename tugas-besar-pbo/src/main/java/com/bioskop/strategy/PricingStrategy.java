package com.bioskop.strategy;

public interface PricingStrategy {
    // Method untuk memanipulasi harga akhir
    double hitungHargaAkhir(double hargaAwal);
}