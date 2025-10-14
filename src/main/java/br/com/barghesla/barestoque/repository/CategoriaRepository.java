package br.com.barghesla.barestoque.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.barghesla.barestoque.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findAllByNomeContainingIgnoreCase(String nome);
    List<Categoria> findAllByOrderByNomeAsc();
    Optional<Categoria> findFirstByNomeIgnoreCase(String nome);
}
