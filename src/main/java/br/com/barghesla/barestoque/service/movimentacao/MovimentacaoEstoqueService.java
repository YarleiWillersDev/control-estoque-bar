package br.com.barghesla.barestoque.service.movimentacao;

import java.util.List;
import org.springframework.stereotype.Service;
import br.com.barghesla.barestoque.entity.MovimentacaoEstoque;

@Service
public interface MovimentacaoEstoqueService {
    MovimentacaoEstoque registrarMovimentacao(MovimentacaoEstoque movimentacao);
    MovimentacaoEstoque atualizar(Long id, MovimentacaoEstoque movimentacao);
    MovimentacaoEstoque buscarPorId(Long id);
    List<MovimentacaoEstoque> listarTodos();
    List<MovimentacaoEstoque> buscarPorProduto(Long produtoId);
}
