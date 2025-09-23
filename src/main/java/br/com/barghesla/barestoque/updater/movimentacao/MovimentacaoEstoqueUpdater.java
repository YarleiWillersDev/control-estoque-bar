package br.com.barghesla.barestoque.updater.movimentacao;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import br.com.barghesla.barestoque.entity.MovimentacaoEstoque;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.TipoMovimentacaoEstoque;
import br.com.barghesla.barestoque.exception.movimentacao.AlteracaoDeDataException;
import br.com.barghesla.barestoque.exception.movimentacao.AlteracaoDeProdutoException;
import br.com.barghesla.barestoque.exception.movimentacao.AlteracaoDeTipoException;
import br.com.barghesla.barestoque.exception.movimentacao.AlteracaoDeUsuarioException;
import br.com.barghesla.barestoque.exception.movimentacao.TipoDeMovimentacaoEstoqueInvalidoException;
import br.com.barghesla.barestoque.exception.movimentacao.TipoDeMovimentacaoEstoqueNuloException;
import br.com.barghesla.barestoque.exception.produto.QuantidadeInvalidaException;

@Component
public class MovimentacaoEstoqueUpdater {

    public void prepararParaRegistrar(Produto produto, MovimentacaoEstoque mov) {
        validarTipo(mov);
        garantirData(mov);
        ajustarSaldo(produto, mov.getTipo(), mov.getQuantidade());
        vincularProduto(mov, produto);
    }

    private void validarTipo(MovimentacaoEstoque mov) {
        if (mov.getTipo() == null) {
            throw new TipoDeMovimentacaoEstoqueNuloException("Tipo não pode ser nulo.");
        }
    }

    private void garantirData(MovimentacaoEstoque mov) {
        if (mov.getDataMovimentacao() == null) {
            mov.setDataMovimentacao(LocalDateTime.now());
        }
    }

    private void ajustarSaldo(Produto produto, TipoMovimentacaoEstoque tipo, int quantidade) {
        if (tipo == TipoMovimentacaoEstoque.ENTRADA) {
            aplicarEntrada(produto, quantidade);
        } else if (tipo == TipoMovimentacaoEstoque.SAIDA) {
            aplicarSaida(produto, quantidade);
        } else {
            throw new TipoDeMovimentacaoEstoqueInvalidoException("Tipo de movimentação inválido.");
        }
    }

    private void aplicarEntrada(Produto produto, int quantidade) {
        produto.setQuantidade(produto.getQuantidade() + quantidade);
    }

    private void aplicarSaida(Produto produto, int quantidade) {
        int saldoAtual = produto.getQuantidade();
        if (quantidade > saldoAtual) {
            throw new QuantidadeInvalidaException("Saldo insuficiente para saída.");
        }
        produto.setQuantidade(saldoAtual - quantidade);
    }

    public void aplicarRegistro(Produto produto, MovimentacaoEstoque mov) {
        ajustarSaldoNoRegistro(produto, mov);
        vincularProduto(mov, produto);
        garantirDataQuandoNula(mov);
    }

    public void aplicarAtualizacaoSomenteQuantidade(Produto produto, MovimentacaoEstoque atual, MovimentacaoEstoque novo) {
        validarImutaveis(atual, novo);
        aplicarDeltaDeQuantidade(produto, atual, novo);
        copiarQuantidade(atual, novo);
    }

    // Registro

    private void ajustarSaldoNoRegistro(Produto produto, MovimentacaoEstoque mov) {
        switch (mov.getTipo()) {
            case ENTRADA -> somar(produto, mov.getQuantidade());
            case SAIDA   -> subtrairComValidacao(produto, mov.getQuantidade());
            default      -> throw new IllegalArgumentException("Tipo inválido.");
        }
    }

    private void vincularProduto(MovimentacaoEstoque mov, Produto produto) {
        mov.setProduto(produto);
    }

    private void garantirDataQuandoNula(MovimentacaoEstoque mov) {
        if (mov.getDataMovimentacao() == null) {
            mov.setDataMovimentacao(java.time.LocalDateTime.now());
        }
    }

    // Atualização somente quantidade

    private void validarImutaveis(MovimentacaoEstoque atual, MovimentacaoEstoque novo) {
        if (novo.getTipo() != null && novo.getTipo() != atual.getTipo()) {
            throw new AlteracaoDeTipoException("Não é permitido alterar o campo TIPO da movimentação");
        }
        if (novo.getProduto() != null && !novo.getProduto().getId().equals(atual.getProduto().getId())) {
            throw new AlteracaoDeProdutoException("Não é permitido alterar o campo PRODUTO da movimentação.");
        }
        if (novo.getUsuarioID() != null && !novo.getUsuarioID().getId().equals(atual.getUsuarioID().getId())) {
            throw new AlteracaoDeUsuarioException("Não é permitido alterar o campo USUARIO da movimentação.");
        }
        if (novo.getDataMovimentacao() != null && !novo.getDataMovimentacao().equals(atual.getDataMovimentacao())) {
            throw new AlteracaoDeDataException("Não é permitido alterar o campo DATA da movimentação.");
        }
    }

    private void aplicarDeltaDeQuantidade(Produto produto, MovimentacaoEstoque atual, MovimentacaoEstoque novo) {
        int delta = novo.getQuantidade() - atual.getQuantidade();
        if (delta == 0) return;

        switch (atual.getTipo()) {
            case ENTRADA -> somar(produto, delta);
            case SAIDA   -> subtrairComValidacao(produto, delta); // delta>0 aumenta retirada
            default      -> throw new TipoDeMovimentacaoEstoqueInvalidoException("Tipo inválido.");
        }
    }

    private void copiarQuantidade(MovimentacaoEstoque atual, MovimentacaoEstoque novo) {
        atual.setQuantidade(novo.getQuantidade());
    }

    // Helpers de saldo

    private void somar(Produto produto, int valor) {
        produto.setQuantidade(produto.getQuantidade() + valor);
    }

    private void subtrairComValidacao(Produto produto, int valor) {
        int saldoResultante = produto.getQuantidade() - valor;
        if (saldoResultante < 0) {
            throw new QuantidadeInvalidaException("Quantidade insuficiente.");
        }
        produto.setQuantidade(saldoResultante);
    }
}

