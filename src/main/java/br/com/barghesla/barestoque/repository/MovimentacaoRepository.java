package br.com.barghesla.barestoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.barghesla.barestoque.entity.MovimentacaoEstoque;

public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, Long> {

}
