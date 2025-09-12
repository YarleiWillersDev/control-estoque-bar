package br.com.barghesla.barestoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.barghesla.barestoque.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
}
