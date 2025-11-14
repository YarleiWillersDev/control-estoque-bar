package br.com.barghesla.barestoque.service.produto;

import java.util.List;
import org.springframework.stereotype.Service;
import br.com.barghesla.barestoque.dto.produto.ProdutoRequest;
import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;

@Service
public interface ProdutoService {
    ProdutoResponse criar(ProdutoRequest request);
    ProdutoResponse atualizar(Long id, ProdutoRequest request);
    ProdutoResponse inativar(Long id);
    ProdutoResponse ativar(Long id);
    ProdutoResponse atualizarStatus(Long id, String novoStatus);
    ProdutoResponse buscarPorId(Long id);
    List<ProdutoResponse> buscarPorNome(String nome);
    List<ProdutoResponse> buscarPorCategoria(Long categoriaID);
    List<ProdutoResponse> listarTodos();
}
