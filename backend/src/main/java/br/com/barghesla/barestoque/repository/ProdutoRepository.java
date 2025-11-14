package br.com.barghesla.barestoque.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.barghesla.barestoque.model.Produto;
import br.com.barghesla.barestoque.model.StatusProduto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    List<Produto> findByStatus(StatusProduto status);
    List<Produto> findByCategoriaIdOrderByNomeAsc(Long categoriaId);
}
