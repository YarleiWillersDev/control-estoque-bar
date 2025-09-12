package br.com.barghesla.barestoque.service.produto;

import java.util.List;

import br.com.barghesla.barestoque.entity.Produto;

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
