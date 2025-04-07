package com.example.demo.controllers;

import java.util.List;
import com.example.demo.entities.Libro;
import com.example.demo.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/libri")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public List<Libro> all() {
        return libroService.all();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> get(@PathVariable Long id) {
        return libroService.get(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/titolo/{titolo}")
    public List<Libro> getByTitolo(@PathVariable String titolo) {
        return libroService.getByTitolo(titolo);
    }

    @GetMapping("/autore/{autore}")
    public List<Libro> getByAutore(@PathVariable String autore) {
        return libroService.getByAutore(autore);
    }

    @PostMapping
    public Libro create(@RequestBody Libro libro) {
        return libroService.create(libro);
    }

    @PutMapping("/{id}")
    public Libro update(@PathVariable Long id, @RequestBody Libro libro) {
        return libroService.update(id, libro);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        libroService.delete(id);
    }
}