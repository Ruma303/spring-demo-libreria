package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import com.example.demo.entities.Libro;

public interface LibroService {

    List<Libro> all();

    Optional<Libro> get(Long id);

    List<Libro> getByTitolo(String titolo);

    List<Libro> getByAutore(String autore);

    Libro create(Libro libro);

    Libro update(Long id, Libro libro);

    void delete(Long id);
}