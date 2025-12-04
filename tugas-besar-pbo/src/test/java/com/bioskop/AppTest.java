package com.bioskop;

import com.bioskop.data.CinemaData;
import com.bioskop.model.Film;
import com.bioskop.tiket.*;
import com.bioskop.strategy.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class AppTest {

    // 1. TEST SINGLETON & COLLECTION
    @Test
    public void testSingletonDatabase() {
        CinemaData db1 = CinemaData.getInstance();
        CinemaData db2 = CinemaData.getInstance();

        // Cek apakah objectnya sama persis (Singleton)
        assertSame("Singleton harus mengembalikan instance yang sama", db1, db2);

        // Cek apakah data dummy (ArrayList) masuk
        List<Film> films = db1.getAll();
        assertFalse("Database film tidak boleh kosong", films.isEmpty());
    }

    // 2. TEST DECORATOR PATTERN
    @Test
    public void testDecoratorTiket() {
        // UPDATE: Tambahkan parameter ke-4 (Stok) misal 50
        Film film = new Film("Test Movie", "Action", 50000, 50);
        
        // Beli Tiket Polos (50.000)
        Tiket tiket = new TiketFilm(film);
        assertEquals(50000.0, tiket.getHarga(), 0.0);

        // Tambah Popcorn (+25.000) -> Jadi 75.000
        tiket = new ExtraPopcorn(tiket);
        assertEquals(75000.0, tiket.getHarga(), 0.0);
        assertTrue(tiket.getDeskripsi().contains("Popcorn"));

        // Tambah VIP (+50.000) -> Jadi 125.000
        tiket = new ExtraVip(tiket);
        assertEquals(125000.0, tiket.getHarga(), 0.0);
    }

    // 3. TEST STRATEGY PATTERN
    @Test
    public void testStrategyDiskon() {
        double hargaAwal = 100000;

        // Cek Diskon Member (10%)
        PricingStrategy memberStrat = new DiskonMember();
        assertEquals(90000.0, memberStrat.hitungHargaAkhir(hargaAwal), 0.0);

        // Cek Harga Weekend (+10.000)
        PricingStrategy weekendStrat = new HargaWeekend();
        assertEquals(110000.0, weekendStrat.hitungHargaAkhir(hargaAwal), 0.0);
    }
}