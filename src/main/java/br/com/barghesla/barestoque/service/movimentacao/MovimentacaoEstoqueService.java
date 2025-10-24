package br.com.barghesla.barestoque.service.movimentacao;

import java.util.List;
import org.springframework.stereotype.Service;

import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueRequest;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueResponse;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueUpdateQuantidadeRequest;

@Service
public interface MovimentacaoEstoqueService {
    MovimentacaoEstoqueResponse registrarMovimentacao(MovimentacaoEstoqueRequest request);
    MovimentacaoEstoqueResponse atualizarQuantidade(Long id, MovimentacaoEstoqueUpdateQuantidadeRequest request);
    MovimentacaoEstoqueResponse buscarPorId(Long id);
    List<MovimentacaoEstoqueResponse> listarTodos();
    List<MovimentacaoEstoqueResponse> buscarPorProduto(Long produtoId);
}
