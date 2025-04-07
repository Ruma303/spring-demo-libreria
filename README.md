# **Database**

In questa esercitazione il database MySQL è stato creato tramite i seguenti script SQL:

```sql
CREATE DATABASE IF NOT EXISTS libreria_db;

USE libreria_db;


-- Libri

CREATE TABLE libri (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titolo VARCHAR(500) NOT NULL,
    autore VARCHAR(500) NOT NULL,
    isbn VARCHAR(13) UNIQUE,
    anno_pubblicazione INT,
    genere VARCHAR(255)
) ENGINE=InnoDB;

DESCRIBE libri;


-- Categorie

CREATE TABLE categorie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

DESCRIBE categorie;


-- Tabella Pivot per relazione Molti a Molti

CREATE TABLE libro_categoria (
    libro_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (libro_id, categoria_id),
    FOREIGN KEY (libro_id) REFERENCES libri(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categorie(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

DESCRIBE libro_categoria;


-- Inserimento dei libri

INSERT INTO libri(titolo, autore, isbn, anno_pubblicazione, genere) VALUES
('Dune', 'Frank Herbert', '9780441172719', 1965, 'Fantascienza'),
('1984', 'George Orwell', '9780451524935', 1949, 'Distopico'),
('Il Signore degli Anelli', 'J.R.R. Tolkien', '9780261102385', 1954, 'Fantasy'),
('Orgoglio e Pregiudizio', 'Jane Austen', '9780141439518', 1813, 'Romanzo'),
('Il nome della rosa', 'Umberto Eco', '9788807882480', 1980, 'Giallo'),
('Dracula', 'Bram Stoker', '9780141439846', 1897, 'Horror'),
('Frankenstein', 'Mary Shelley', '9780486282114', 1818, 'Horror'),
('La storia infinita', 'Michael Ende', '9788884513460', 1979, 'Fantasy'),
('Il codice da Vinci', 'Dan Brown', '9780307474278', 2003, 'Thriller'),
('Sapiens', 'Yuval Noah Harari', '9780062316097', 2011, 'Saggio'),
('Il cacciatore di aquiloni', 'Khaled Hosseini', '9788807811992', 2003, 'Romanzo'),
('La ragazza con l’orecchino di perla', 'Tracy Chevalier', '9788807811282', 1999, 'Storico'),
('Il piccolo principe', 'Antoine de Saint-Exupéry', '9780156013987', 1943, 'Favola'),
('It', 'Stephen King', '9781501142970', 1986, 'Horror'),
('Moby Dick', 'Herman Melville', '9781503280786', 1851, 'Avventura'),
('La fattoria degli animali', 'George Orwell', '9780451526342', 1945, 'Satira'),
('La strada', 'Cormac McCarthy', '9780307387899', 2006, 'Post-apocalittico'),
('La versione di Barney', 'Mordecai Richler', '9788807816065', 1997, 'Romanzo'),
('Harry Potter e la pietra filosofale', 'J.K. Rowling', '9780747532743', 1997, 'Fantasy'),
('Steve Jobs', 'Walter Isaacson', '9781451648539', 2011, 'Biografico');

SELECT * FROM libri;


-- Inserimento delle categorie

INSERT INTO categorie (nome) VALUES
('Fantascienza'),
('Romanzo'),
('Storico'),
('Fantasy'),
('Giallo'),
('Biografico'),
('Horror'),
('Saggio');

SELECT * FROM categorie;


-- Associazione libri-categorie

INSERT INTO libro_categoria (libro_id, categoria_id) VALUES
(1, 1),
(1, 4),
(2, 1),
(2, 2),
(3, 4),
(4, 2),
(5, 2),
(5, 5),
(6, 7),
(7, 7),
(8, 4),
(9, 5),
(9, 2),
(10, 8),
(11, 2),
(11, 3),
(12, 3),
(13, 2),
(14, 7),
(14, 2),
(15, 2),
(16, 2),
(16, 1),
(17, 1),
(17, 4),
(18, 2),
(19, 4),
(20, 6),
(20, 8);
```

---

## **Strutture tabelle**

- Le tabelle `libri` e `categorie` sono state unite con una relazione molti a molti per mezzo della tabella ponte `libro_categoria`:

```sql
mysql> describe libri;
+--------------------+--------------+------+-----+---------+----------------+
| Field              | Type         | Null | Key | Default | Extra          |
+--------------------+--------------+------+-----+---------+----------------+
| id                 | bigint       | NO   | PRI | NULL    | auto_increment |
| titolo             | varchar(500) | NO   |     | NULL    |                |
| autore             | varchar(500) | NO   |     | NULL    |                |
| isbn               | varchar(13)  | YES  | UNI | NULL    |                |
| anno_pubblicazione | int          | YES  |     | NULL    |                |
| genere             | varchar(255) | YES  |     | NULL    |                |
+--------------------+--------------+------+-----+---------+----------------+

mysql> describe categorie;
+-------+--------------+------+-----+---------+----------------+
| Field | Type         | Null | Key | Default | Extra          |
+-------+--------------+------+-----+---------+----------------+
| id    | bigint       | NO   | PRI | NULL    | auto_increment |
| nome  | varchar(255) | NO   |     | NULL    |                |
+-------+--------------+------+-----+---------+----------------+

mysql> describe libro_categoria;
+--------------+--------+------+-----+---------+-------+
| Field        | Type   | Null | Key | Default | Extra |
+--------------+--------+------+-----+---------+-------+
| libro_id     | bigint | NO   | PRI | NULL    |       |
| categoria_id | bigint | NO   | PRI | NULL    |       |
+--------------+--------+------+-----+---------+-------+
```

---

# **Spring Boot**

Passiamo alla creazione del repository `spring-demo-libreria`.

---

## `pom.xml`

- Dipendenze usate:

```xml
<dependencies>
	<!-- Spring Boot Data JPA Starter con Jakarta -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
	</dependency>

	<!-- Spring Boot Web Starter (Per creare API REST) -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>

	<!-- MySQL Driver -->
	<dependency>
		<groupId>com.mysql</groupId>
		<artifactId>mysql-connector-j</artifactId>
		<scope>runtime</scope>
	</dependency>

	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	</dependency>
</dependencies>
```

---

## `application.properties`

- Oltre ai dati personali locali, sono state aggiunte configurazioni per il debug delle query SQL.

```properties
# Configurazione del server
spring.application.name=spring-demo-libreria
server.port=8080

# Configurazione Datasource
spring.datasource.url=jdbc:mysql://localhost:3306/libreria_db?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configurazione JPA (Hibernate)
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## **Entities**

### `Libro`

- In questa entità `Libro` è stata indicata la tabella pivot in relazione con `Category`, specificando la tabella pivot tramite `@JoinTable`.
- È stato aggiunto anche `@JsonIgnoreProperties` per evitare loop durante la serializzazione nelle richieste HTTP.

```java
package com.example.demo.entities;

import jakarta.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "libri")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titolo", nullable = false, length = 500)
    private String titolo;

    @Column(name = "autore", nullable = false, length = 500)
    private String autore;

    @Column(name = "isbn", unique = true, nullable = true, length = 13)
    private String isbn;

    @Column(name = "anno_pubblicazione", nullable = true)
    private Integer annoPubblicazione;

    @Column(name = "genere", nullable = true, length = 255)
    private String genere;

    @ManyToMany
    @JoinTable(
        name = "libro_categoria",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @JsonIgnoreProperties("libri")
    private Set<Categoria> categorie;

	// Getters e Setters
```

---

### `Categoria`

- Nell'entità `Categoria` è stata mappata la relazione `@ManyToMany` con i libri.
- Sono stati usati due costruttori, uno dei quali recupera il `nome` della categoria in modo da usarlo per creare e aggiornare una categoria, se viene passato questo campo durante le richieste HTTP.

```java
package com.example.demo.entities;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categorie")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @ManyToMany(mappedBy = "categorie")
    private Set<Libro> libri;

    public Categoria() {}

    public Categoria(String nome) {
        this.nome = nome;
    }

	// Getters e Setters
}
```

---

## **Repositories**

---

### `LibroRepository`

- Nel repository dei libri sono stati definiti un paio di metodi per cercare un libro nel campo `titolo` o `autore`.

```java
package com.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByTitoloContainingIgnoreCase(String titolo);

    List<Libro> findByAutoreContainingIgnoreCase(String autore);
}
```

---

### `CategoriaRepository`

- Nel repository delle categorie è stato aggiunto un metodo per la ricerca della categoria tramite `nome`.

```java
package com.example.demo.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNomeIgnoreCase(String nome);

}
```

---

## **Services**

>Dai servizi in poi verranno create delle strutture Java solo per l'entità `Libro`.
>I servizi sono stati creati come interfacce: una per definire i metodi ed una per implementarli.

---

### `LibroService`

- Oltre ai normali metodi per operazioni CRUD, è stato aggiunto anche un metodo `paginate()` che ritorna tutti i libri ma paginati.

```java
package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import com.example.demo.entities.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LibroService {

    List<Libro> all();

    Page<Libro> paginate(Pageable pageable);

    Optional<Libro> get(Long id);

    List<Libro> getByTitolo(String titolo);

    List<Libro> getByAutore(String autore);

    Libro create(Libro libro);

    Libro update(Long id, Libro libro);

    void delete(Long id);

}
```

---

### `LibroServiceImpl`

- `LibroServiceImpl` è una **classe di servizio** che implementa l’interfaccia `LibroService` e fornisce la logica applicativa per la gestione dei libri e relative categorie associate.
- Nel metodo `create()` salvare un nuovo libro nel database, eventualmente associato a una o più categorie. Queste possono essere già esistenti o nuove (identificate dalla presenza del campo `name`).
- Il metodo `update()` esegue gli stessi controlli: aggiorna un libro ma anche le categorie se sono aggiunte nel corpo della richiesta.

```java
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
```

---

## **Controllers**

>Di seguito verrà mostrato un unico controller per la gestione dei libri, ed eventuali categorie associate.

---

### `LibroController`

- In questo controller recuperiamo tutti i libri con `all()`, ma volendo anche paginati con `paginate()`.
- I parametri dalla query string dell'URL vengono utilizzati per costruire un oggetto dell'interfaccia `Pageable`, dove:
    - `page` : L’indice della pagina (0-based).
    - `size` : Il numero di risultati per pagina.
    - `sortBy` e `direction` : La colonna su cui ordinare e la direzione di ordinamento (ascendente o discendente).

```java
package com.example.demo.controllers;

import java.util.List;
import com.example.demo.entities.Libro;
import com.example.demo.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;

@RestController
@RequestMapping("/api/libri")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public List<Libro> all() {
        return libroService.all();
    }

    @GetMapping("/paginate")
    public Page<Libro> paginate(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() :
                    Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return libroService.paginate(pageable);
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
```

---

# **API Testing**

---

## `GET`

---

### **Recuperare tutti i libri**

```HTTP
GET http://localhost:8080/api/libri
```

---

### **Recupero libri con paginazione**

- Esempio di richiesta per ottenere la prima pagina (**10** libri), ordinati per `titolo` in modo crescente:

```HTTP
GET http://localhost:8080/api/libri/paginate?page=0&size=10&sortBy=titolo&direction=asc
```

- Esempio di recupero dell’ultima pagina ordinata per `annoPubblicazione`:

```HTTP
GET http://localhost:8080/api/libri/paginate?page=2&size=5&sortBy=annoPubblicazione&direction=desc
```

---

### **Recupero singoli libri**

- Esempio: cercando da `id`:

```HTTP
GET http://localhost:8080/api/libri/13
```

- Esempio: cercando da `titolo`:

```HTTP
GET http://localhost:8080/api/libri/titolo/Il piccolo principe
```

- Esempio: cercando da `autore`:

```HTTP
GET http://localhost:8080/api/libri/autore/Stephen King
```

---

## `POST`

---

### **Creazione Libro e categorie associate**

```HTTP
POST http://localhost:8080/api/libri
```

- Creazione libro con campi vuoti:

```json
{
    "titolo": "",
    "autore": "",
    "isbn": "",
    "anno_pubblicazione": "",
    "genere": ""
}
```

-  Creazione libro con valori:

```json
{
    "titolo": "Il Grande Gatsby",
    "autore": "F. Scott Fitzgerald",
    "isbn": "9783161484100",
    "anno_pubblicazione": "1925",
    "genere": "Romanzo"
}
```

- Creazione libro con categorie associate:

```json
{
  "titolo": "La nuova avventura",
  "autore": "Mario Rossi",
  "isbn": "1234567890123",
  "annoPubblicazione": 2024,
  "genere": "Avventura",
  "categorie": [
    { "id": 1 },
    { "id": 3 }
  ]
}
```

- È possibile specificare anche il nome della categoria:

```json
{
  "id": 22,
  "titolo": "La nuova avventura",
  "autore": "Mario Rossi",
  "isbn": "1234567890124",
  "annoPubblicazione": 2024,
  "genere": "Avventura",
  "categorie": [
    {
      "id": 1,
      "nome": "Fantasy"
    },
    {
      "id": 3,
      "nome": "Avventura"
    }
  ]
}
```

- Creazione libro e una nuova categoria non preesistenti.
- Nel campo `categorie` basterà non inserire l'`id` ma solo il campo `nome` della categoria per crearla (solo una categoria).

```json
{
  "titolo": "Libro con nuove categorie",
  "autore": "Giulia Verdi",
  "isbn": "9876543210001",
  "annoPubblicazione": 2023,
  "genere": "Fantasy",
  "categorie": [
    { "nome": "Manga" }
  ]
}
```

---

### **Aggiornamento Libro**

- Aggiornare libro passando un `id` come parametro di rotta:

```HTTP
PUT http://localhost:8080/api/libri/20
```

```json
{
    "titolo": "Il Cigno Nero",
    "autore": "Nassim Nicholas Taleb",
    "isbn": "9780812973815",
    "anno_pubblicazione": "2007",
    "genere": "Saggistica",
}
```

- Aggiornare singolo libro e categorie associate, indicando le categorie da cambiare tramite il loro `id`:

```HTTP
PUT http://localhost:8080/api/libri/21
```

```json
{
    "titolo": "Il Cigno Nero",
    "autore": "Nassim Nicholas Taleb",
    "isbn": "9780812973815",
    "anno_pubblicazione": "2007",
    "genere": "Saggistica",
    "categorie": [
	    { "id": 5 },
	    { "id": 7 },
	    { "id": 10 }
	]
}
```

La risposta sarà:

```json
{
    "id": 20,
    "titolo": "Il Cigno Nero",
    "autore": "Nassim Nicholas Taleb",
    "isbn": "9780812973815",
    "annoPubblicazione": null,
    "genere": "Saggistica",
    "categorie": [
        {
            "id": 10,
            "nome": "Romanzo"
        },
        {
            "id": 5,
            "nome": "Giallo"
        },
        {
            "id": 7,
            "nome": "Horror"
        }
    ]
}
```

---

### **Eliminazione Libro**

- Esempio di eliminazione tramite `id`:

```HTTP
DELETE http://localhost:8080/api/libri/21
```