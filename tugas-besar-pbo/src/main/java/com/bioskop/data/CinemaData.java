package com.bioskop.data;

import com.bioskop.model.Film;
import com.bioskop.model.User;
import com.bioskop.util.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IMPLEMENTASI SINGLETON PATTERN (Enum Implementation)
 * Ini adalah cara paling aman dan disukai SonarQube untuk membuat Singleton.
 */
@SuppressWarnings("java:S6548")
public enum CinemaData implements Repository<Film> {
    
    // 1. Instance Singleton didefinisikan sebagai Enum Constant
    INSTANCE;

    // 2. Variable Data (Non-static karena Enum ini sudah Singleton)
    private final List<Film> filmList;
    private final Map<String, User> userMap;

    // 3. Constructor Enum (Otomatis private & dipanggil sekali saat program jalan)
    CinemaData() {
        filmList = new ArrayList<>();
        userMap = new HashMap<>();
        seedData();
    }

    // 4. Helper Method agar Main.java tidak perlu diubah kodenya
    // Main.java tetap bisa panggil CinemaData.getInstance()
    public static CinemaData getInstance() {
        return INSTANCE;
    }

    // Method Seeding Data
    private void seedData() {
        add(new Film("Avatar: The Way of Water", "Sci-Fi", 50000));
        add(new Film("The Super Mario Bros", "Animation", 40000));
        add(new Film("John Wick 4", "Action", 55000));
        add(new Film("Evil Dead Rise", "Horror", 45000));

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