package com.bioskop.util;

import java.util.List;

// <T> adalah Generic Type. Bisa diganti Film, User, dll nanti.
public interface Repository<T> {
    void add(T item);
    List<T> getAll();
    void delete(T item);
}