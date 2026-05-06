package com.sicredi.libraryhub.infrastructure.repository;

import com.sicredi.libraryhub.domain.GeneroPojo;
import com.sicredi.libraryhub.domain.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivroRepository extends MongoRepository<Livro, String> {

    Optional<Livro> findByIsbn(String isbn);

    Page<Livro> findByGenero(GeneroPojo genero, Pageable pageable);
}
