package br.com.barghesla.barestoque.service.produto;

import java.util.List;
import org.springframework.stereotype.Service;
import br.com.barghesla.barestoque.entity.Produto;

@Service
public interface ProdutoService {
    Produto criar(Produto produto);
    Produto atualizar(Long id, Produto produto);
    void inativar(Long id);
    Produto ativar(Long id);
    Produto buscarPorId(Long id);
    List<Produto> buscarPorNome(String nome);
    List<Produto> buscarPorCategoria(Long categoriaID);
    List<Produto> listarTodos();
}
