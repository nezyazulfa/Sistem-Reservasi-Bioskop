package com.bioskop.data;

import com.bioskop.model.Film;
import com.bioskop.util.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S6548")
public class CinemaData implements Repository<Film> {
    
    private static CinemaData instance;
    private List<Film> filmList;
    private Map<String, Integer> inventory; 
    
    // NAMA FILE DATABASE FISIK
    private static final String DB_FILE = "cinema_database.dat";

    private CinemaData() {
        // Coba LOAD data dari file dulu
        if (!loadData()) {
            // Kalau file belum ada (Run pertama kali), buat data baru
            filmList = new ArrayList<>();
            inventory = new HashMap<>(); 
            seedData();
        }
    }

    public static synchronized CinemaData getInstance() {
        if (instance == null) {
            instance = new CinemaData();
        }
        return instance;
    }

    private void seedData() {
        // Data Awal (Hanya dipakai jika file database belum ada)
        add(new Film("Avatar: The Way of Water", "Sci-Fi", 50000, 30));
        add(new Film("The Super Mario Bros", "Animation", 40000, 20));
        add(new Film("John Wick 4", "Action", 55000, 15));
        add(new Film("Evil Dead Rise", "Horror", 45000, 50));

        inventory.put("Popcorn", 100);   
        inventory.put("Softdrink", 100); 
        saveData(); // Simpan data awal ke file
    }

    // --- LOGIC PENYIMPANAN PERMANEN (FILE I/O) ---
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_FILE))) {
            oos.writeObject(filmList);
            oos.writeObject(inventory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Tetap biarkan @SuppressWarnings di sini karena casting terjadi di sini
    @SuppressWarnings("unchecked")
    private boolean loadData() {
        File file = new File(DB_FILE);
        if (!file.exists()) return false; // File gak ada, return false

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DB_FILE))) {
            filmList = (List<Film>) ois.readObject();
            inventory = (Map<String, Integer>) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    // --- Inventory Methods ---
    public int getItemStock(String itemName) {
        return inventory.getOrDefault(itemName, 0);
    }

    public void reduceItemStock(String itemName, int amount) {
        inventory.computeIfPresent(itemName, (key, currentStock) -> {
            saveData(); // SAVE SETIAP ADA PERUBAHAN
            return currentStock - amount;
        });
    }

    // --- Repository Methods ---
    @Override
    public void add(Film item) { 
        filmList.add(item); 
        saveData(); // SAVE SETIAP ADA FILM BARU
    }

    @Override
    public List<Film> getAll() { return filmList; }

    @Override
    public void delete(Film item) { 
        filmList.remove(item); 
        saveData();
    }
    
    public void updateFilmStock() {
        saveData();
    }
}