package com.bioskop.data;

import com.bioskop.model.Film;
import com.bioskop.model.User;
import com.bioskop.util.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IMPLEMENTASI SINGLETON PATTERN
 * Class ini bertindak sebagai In-Memory Database pusat.
 */
public class CinemaData implements Repository<Film> { // Kita implement generic khusus Film disini
    
    // 1. Static variable untuk menyimpan satu-satunya instance
    private static CinemaData instance;

    // 2. JAVA COLLECTION FRAMEWORK
    // ArrayList untuk Film (karena butuh urutan untuk ditampilkan di Tabel GUI)
    private List<Film> filmList;
    
    // HashMap untuk User (agar Login cepat / O(1) complexity)
    private Map<String, User> userMap;

    // 3. Private Constructor (Agar tidak bisa di-new dari luar)
    private CinemaData() {
        filmList = new ArrayList<>();
        userMap = new HashMap<>();
        seedData(); // Isi data palsu saat aplikasi mulai
    }

    // 4. Public Access Point (Thread Safe)
    public static synchronized CinemaData getInstance() {
        if (instance == null) {
            instance = new CinemaData();
        }
        return instance;
    }

    // Method Seeding Data (Agar GUI tidak kosong)
    private void seedData() {
        // Data Film
        add(new Film("Avatar: The Way of Water", "Sci-Fi", 50000));
        add(new Film("The Super Mario Bros", "Animation", 40000));
        add(new Film("John Wick 4", "Action", 55000));
        add(new Film("Evil Dead Rise", "Horror", 45000));

        // Data User (Admin & Customer)
        userMap.put("admin", new User("admin", "admin123", "ADMIN"));
        userMap.put("budi", new User("budi", "12345", "CUSTOMER"));
    }

    // --- Implementasi Generic Repository untuk Film ---
    @Override
    public void add(Film item) {
        filmList.add(item);
    }

    @Override
    public List<Film> getAll() {
        return filmList;
    }

    @Override
    public void delete(Film item) {
        filmList.remove(item);
    }

    // --- Method Khusus User (HashMap Logic) ---
    public boolean isValidUser(String username, String password) {
        if (userMap.containsKey(username)) {
            User u = userMap.get(username);
            return u.getPassword().equals(password);
        }
        return false;
    }
    
    public User getUser(String username) {
        return userMap.get(username);
    }
}