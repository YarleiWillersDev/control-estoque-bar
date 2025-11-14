package br.com.barghesla.barestoque.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.barghesla.barestoque.model.MovimentacaoEstoque;

@Repository
public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    List<MovimentacaoEstoque> findAllByOrderByDataMovimentacao();
    List<MovimentacaoEstoque> findByProdutoIdOrderByDataMovimentacaoDesc(Long produtoId);
    List<MovimentacaoEstoque> findByDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(LocalDate inicio, LocalDate fim);
}
