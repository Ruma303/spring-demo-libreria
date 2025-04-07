package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Libro;
import com.example.demo.repositories.LibroRepository;

@Service
public class LibroServiceImpl implements LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Override
    public List<Libro> all() {
        return libroRepository.findAll();
    }

    @Override
    public Optional<Libro> get(Long id) {
        return libroRepository.findById(id);
    }

    @Override
    public List<Libro> getByTitolo(String titolo) {
        return libroRepository.findByTitoloContainingIgnoreCase(titolo);
    }

    @Override
    public List<Libro> getByAutore(String autore) {
        return libroRepository.findByAutoreContainingIgnoreCase(autore);
    }

    @Override
    public Libro create(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public Libro update(Long id, Libro libro) {
        libro.setId(id);
        return libroRepository.save(libro);
    }

    @Override
    public void delete(Long id) {
        libroRepository.deleteById(id);
    }
}