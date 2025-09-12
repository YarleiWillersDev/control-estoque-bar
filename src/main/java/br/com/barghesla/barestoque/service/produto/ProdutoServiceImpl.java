package br.com.barghesla.barestoque.service.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.validation.produto.ProdutoValidator;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoValidator produtoValidator;

    @Override
    public Produto criar(Produto produto) {
        produtoValidator.validar(produto);
        produto.setStatus(StatusProduto.ATIVO);
        return produtoRepository.save(produto);
    }

    @Override
    public Produto atualizar(Long id, Produto produto) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoCadastradoException(
                        "Não existem produtos cadastrados para este id na base de dados!"));
        produtoValidator.atualizarCampos(produtoExistente, produto);
        produtoValidator.validar(produtoExistente);
        return produtoRepository.save(produtoExistente);
    }

    @Override
    public void inativar(Long id) {
        Produto produtoEncontrado = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoCadastradoException(
                        "Não existem produtos cadastrados para este id na base de dados!"));
        produtoValidator.inativar(produtoEncontrado);
        produtoRepository.save(produtoEncontrado);
    }

    @Override
    public Produto ativar(Long id) {
        Produto produtoEncontrado = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoCadastradoException(
                        "Não existem produtos cadastrados para este id na base de dados!"));
        produtoValidator.ativar(produtoEncontrado);
        return produtoRepository.save(produtoEncontrado);
    }

    @Override
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoCadastradoException(
                        "Não existem produtos cadastrados para este id na base de dados!"));
    }

    @Override
    public List<Produto> buscarPorNome(String nome) {
        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);
        produtoValidator.validarListaVazia(produtos, nome);
        return produtos;
    }

    @Override
    public List<Produto> buscarPorCategoria(Long categoriaID) {
        List<Produto> produtos = produtoRepository.findByCategoriaId(categoriaID);
        produtoValidator.validarListaVazia(produtos, categoriaID);
        return produtos;
    }

    @Override
    public List<Produto> listarTodos() {
        List<Produto> produtos = produtoRepository.findAll();
        produtoValidator.validarListaVazia(produtos);
        return produtos;
    }
}
