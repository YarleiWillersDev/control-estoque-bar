package br.com.barghesla.barestoque.updater.movimentacao;

import br.com.barghesla.barestoque.exception.movimentacao.TipoDeMovimentacaoEstoqueInvalidoException;
import br.com.barghesla.barestoque.exception.movimentacao.TipoDeMovimentacaoEstoqueNuloException;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoPodeSerNuloException;
import br.com.barghesla.barestoque.exception.produto.QuantidadeInvalidaException;
import br.com.barghesla.barestoque.model.MovimentacaoEstoque;
import br.com.barghesla.barestoque.model.Produto;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MovimentacaoEstoqueUpdater {

    // -----------------------------------------------------------------------------------------
    // --- MÉTODOS PÚBLICOS (API da classe) ---
    // -----------------------------------------------------------------------------------------

    /**
     * Prepara uma nova entidade MovimentacaoEstoque para ser registrada.
     * Este método define a data (se nula) e ajusta o saldo do produto associado.
     *
     * @param movimentacao A entidade MovimentacaoEstoque, já com Produto e Usuário vinculados.
     */
    public void prepararParaRegistrar(MovimentacaoEstoque movimentacao) {
        validarDependencias(movimentacao);
        garantirData(movimentacao);

        Produto produto = movimentacao.getProduto();
        int quantidade = movimentacao.getQuantidade();

        switch (movimentacao.getTipo()) {
            case ENTRADA -> somarAoEstoque(produto, quantidade);
            case SAIDA -> subtrairDoEstoqueComValidacao(produto, quantidade);
            default -> throw new TipoDeMovimentacaoEstoqueInvalidoException("Tipo de movimentação desconhecido.");
        }
    }

    /**
     * Atualiza a quantidade de uma movimentação existente e ajusta o saldo do produto.
     * O método calcula a diferença (delta) entre a nova e a antiga quantidade e aplica essa
     * diferença ao estoque do produto.
     *
     * @param movimentacao   A entidade de movimentação persistida a ser atualizada.
     * @param novaQuantidade A nova quantidade para a movimentação.
     */
    public void atualizarQuantidade(MovimentacaoEstoque movimentacao, int quantidade) {
        validarDependencias(movimentacao);

        int quantidadeAntiga = movimentacao.getQuantidade();
        int delta = quantidade - quantidadeAntiga;

        if (delta == 0) {
            return; // Nenhuma alteração necessária
        }

        Produto produto = movimentacao.getProduto();

        switch (movimentacao.getTipo()) {
            case ENTRADA -> somarAoEstoque(produto, delta);
            case SAIDA -> subtrairDoEstoqueComValidacao(produto, delta);
            default -> throw new TipoDeMovimentacaoEstoqueInvalidoException("Tipo de movimentação desconhecido.");
        }

        // Atualiza a quantidade na própria entidade de movimentação
        movimentacao.setQuantidade(quantidade);
    }

    // -----------------------------------------------------------------------------------------
    // --- MÉTODOS PRIVADOS (Lógica interna) ---
    // -----------------------------------------------------------------------------------------

    /**
     * Garante que as dependências essenciais (Produto e Tipo) não são nulas.
     */
    private void validarDependencias(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getProduto() == null) {
            throw new ProdutoNaoPodeSerNuloException("O produto não pode ser nulo para esta operação.");
        }
        if (movimentacao.getTipo() == null) {
            throw new TipoDeMovimentacaoEstoqueNuloException("O tipo da movimentação não pode ser nulo.");
        }
    }
    
    /**
     * Define a data e hora atuais na movimentação se ela ainda não tiver uma.
     */
    private void garantirData(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getDataMovimentacao() == null) {
            movimentacao.setDataMovimentacao(LocalDateTime.now());
        }
    }

    /**
     * Adiciona um valor ao estoque do produto.
     */
    private void somarAoEstoque(Produto produto, int valor) {
        produto.setQuantidade(produto.getQuantidade() + valor);
    }

    /**
     * Subtrai um valor do estoque do produto, validando se o saldo resultante é suficiente.
     */
    private void subtrairDoEstoqueComValidacao(Produto produto, int valor) {
        int saldoResultante = produto.getQuantidade() - valor;
        if (saldoResultante < 0) {
            throw new QuantidadeInvalidaException("Operação resultaria em estoque negativo. Saldo insuficiente.");
        }
        produto.setQuantidade(saldoResultante);
    }
}
