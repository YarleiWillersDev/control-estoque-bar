package br.com.barghesla.barestoque.service.produto;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.updater.produto.ProdutoUpdater;
import br.com.barghesla.barestoque.validation.produto.ProdutoValidator;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoValidator produtoValidator;
    private final ProdutoUpdater produtoUpdater;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, ProdutoValidator produtoValidator,
            ProdutoUpdater produtoUpdater) {
        this.produtoRepository = produtoRepository;
        this.produtoValidator = produtoValidator;
        this.produtoUpdater = produtoUpdater;
    }

    @Override
    public Produto criar(Produto produto) {
        produtoValidator.validarCriacao(produto);
        produto.setStatus(StatusProduto.ATIVO);
        return produtoRepository.save(produto);
    }

    @Override
    @Transactional
    public Produto atualizar(Long id, Produto produto) {
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoCadastradoException(
                        "Não existem produtos cadastrados para este id na base de dados!"));

        produtoValidator.validarAtualizacao(existente, produto);
        produtoUpdater.aplicar(existente, produto);
        return produtoRepository.save(existente);
    }

    @Override
    @Transactional
    public void inativar(Long id) {
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoCadastradoException(
                        "Não existem produtos cadastrados para este id na base de dados!"));
        produtoValidator.validarInativacao(existente);
        existente.setStatus(StatusProduto.INATIVO);
        produtoRepository.save(existente);
    }

    @Override
    @Transactional
    public Produto ativar(Long id) {
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoCadastradoException(
                        "Não existem produtos cadastrados para este id na base de dados!"));
        produtoValidator.validarAtivacao(existente);
        existente.setStatus(StatusProduto.ATIVO);
        return produtoRepository.save(existente);
    }

    @Override
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoCadastradoException(
                        "Não existem produtos cadastrados para este id na base de dados!"));
    }

    @Override
    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Override
    public List<Produto> buscarPorCategoria(Long categoriaID) {
        return produtoRepository.findByCategoriaIdOrderByNomeAsc(categoriaID);
    }

    @Override
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }
}
