package br.com.barghesla.barestoque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    List<Produto> findByStatus(StatusProduto status);
    List<Produto> findByCategoriaId(Long categoriaId);
}
