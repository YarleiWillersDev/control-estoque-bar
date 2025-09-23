package br.com.barghesla.barestoque.service.movimentacao;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.barghesla.barestoque.entity.MovimentacaoEstoque;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.exception.movimentacao.MovimentacaoEstoqueInexistenteException;
import br.com.barghesla.barestoque.exception.movimentacao.ProdutoIdMovimentacaoEstoqueNuloException;
import br.com.barghesla.barestoque.repository.MovimentacaoRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.updater.movimentacao.MovimentacaoEstoqueUpdater;
import br.com.barghesla.barestoque.validation.movimentacao.MovimentacaoEstoqueValidation;

@Service
public class MovimentacaoEstoqueServiceImpl implements MovimentacaoEstoqueService{

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueUpdater movimentacaoEstoqueUpdater;
    private final MovimentacaoEstoqueValidation movimentacaoEstoqueValidation;

    public MovimentacaoEstoqueServiceImpl(MovimentacaoRepository movimentacaoRepository, ProdutoRepository produtoRepository, MovimentacaoEstoqueUpdater movimentacaoEstoqueUpdater, MovimentacaoEstoqueValidation movimentacaoEstoqueValidation) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
        this.movimentacaoEstoqueUpdater = movimentacaoEstoqueUpdater;
        this.movimentacaoEstoqueValidation = movimentacaoEstoqueValidation;
    }

    @Override
    @Transactional
    public MovimentacaoEstoque registrarMovimentacao(MovimentacaoEstoque mov) {
        validarEntrada(mov);
        Produto produto =carregarProdutoGerenciado(mov);
        aplicarRegras(produto, mov);
        vincular(produto, mov);
        return movimentacaoRepository.save(mov);
    }

    private void validarEntrada(MovimentacaoEstoque movimentacao) {
        movimentacaoEstoqueValidation.validarCamposParaRegistro(movimentacao);
        if (movimentacao.getProduto() == null || movimentacao.getProduto().getId() == null) {
            throw new ProdutoIdMovimentacaoEstoqueNuloException("Produto não pode ser nulo");
        }
    }

    private Produto carregarProdutoGerenciado(MovimentacaoEstoque mov) {
        Long produtoId = mov.getProduto().getId();
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
    }

    private void aplicarRegras(Produto produto, MovimentacaoEstoque mov) {
        movimentacaoEstoqueUpdater.prepararParaRegistrar(produto, mov);
    }

    private void vincular(Produto produto, MovimentacaoEstoque mov) {
        mov.setProduto(produto);
    }

    @Transactional
    public MovimentacaoEstoque atualizar(Long id, MovimentacaoEstoque novo) {
        movimentacaoEstoqueValidation.validarCamposParaAtualizarQuantidade(novo);
        MovimentacaoEstoque atual = carregarMovimentacao(id);
        Produto produto = carregarProduto(atual.getProduto().getId());
        movimentacaoEstoqueUpdater.aplicarAtualizacaoSomenteQuantidade(produto, atual, novo);
        return movimentacaoRepository.save(atual);
    }

    private Produto carregarProduto(Long id) {
        return produtoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
    }

    private MovimentacaoEstoque carregarMovimentacao(Long id) {
        return movimentacaoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Movimentação não encontrada."));
    }

    @Override
    @Transactional(readOnly = true)
    public MovimentacaoEstoque buscarPorId(Long id) {
        return movimentacaoRepository.findById(id)
            .orElseThrow(() -> new MovimentacaoEstoqueInexistenteException(
                    "Não existem movimentações de estoque para o ID: " + id + " cadastradas na base de dados"));
    }

    @Override
    public List<MovimentacaoEstoque> listarTodos() {
        return movimentacaoRepository.findAllByOrderByDataMovimentacao();
    }

    @Override
    public List<MovimentacaoEstoque> buscarPorProduto(Long produtoId) {
        return movimentacaoRepository.findByProdutoIdOrderByDataMovimentacaoDesc(produtoId);
    }

}
