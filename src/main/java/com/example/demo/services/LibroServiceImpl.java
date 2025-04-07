package com.example.demo.services;

import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.entities.Categoria;
import com.example.demo.entities.Libro;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.repositories.CategoriaRepository;
import com.example.demo.repositories.LibroRepository;

@Service
public class LibroServiceImpl implements LibroService {

	@Autowired
	private LibroRepository libroRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Override
	public List<Libro> all() {
		return libroRepository.findAll();
	}

	@Override
	public Page<Libro> paginate(Pageable pageable) {
		return libroRepository.findAll(pageable);
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
		if (libro.getCategorie() != null && !libro.getCategorie().isEmpty()) {
			Set<Categoria> categorieFinali = libro.getCategorie().stream().map(cat -> {
				if (cat.getId() != null) {
					return categoriaRepository.findById(cat.getId())
							.orElseThrow(() -> new RuntimeException("Categoria non trovata con id: " + cat.getId()));
				} else if (cat.getNome() != null) {
					return categoriaRepository.findByNomeIgnoreCase(cat.getNome())
							.orElseGet(() -> categoriaRepository.save(new Categoria(cat.getNome())));
				} else {
					throw new IllegalArgumentException("Categoria senza id e senza nome");
				}
			}).collect(Collectors.toSet());

			libro.setCategorie(categorieFinali);
		}

		return libroRepository.save(libro);
	}

	@Override
	public Libro update(Long id, Libro libro) {
	    Libro esistente = libroRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Libro non trovato con id: " + id));

	    esistente.setTitolo(libro.getTitolo());
	    esistente.setAutore(libro.getAutore());
	    esistente.setIsbn(libro.getIsbn());
	    esistente.setAnnoPubblicazione(libro.getAnnoPubblicazione());
	    esistente.setGenere(libro.getGenere());

	    if (libro.getCategorie() != null) {
	        Set<Categoria> categorieFinali = libro.getCategorie().stream()
	            .map(cat -> {
	                if (cat.getId() != null) {
	                    return categoriaRepository.findById(cat.getId())
	                        .orElseThrow(() -> new RuntimeException("Categoria non trovata con id: " + cat.getId()));
	                } else if (cat.getNome() != null) {
	                    return categoriaRepository.findByNomeIgnoreCase(cat.getNome())
	                        .orElseGet(() -> categoriaRepository.save(new Categoria(cat.getNome())));
	                } else {
	                    throw new IllegalArgumentException("Categoria senza id e senza nome");
	                }
	            })
	            .collect(Collectors.toSet());

	        esistente.setCategorie(categorieFinali);
	    }

	    return libroRepository.save(esistente);
	}

	@Override
	public void delete(Long id) {
		libroRepository.deleteById(id);
	}
}