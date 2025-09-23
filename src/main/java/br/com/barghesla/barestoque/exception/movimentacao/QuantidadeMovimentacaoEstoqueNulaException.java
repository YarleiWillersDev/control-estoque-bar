package br.com.barghesla.barestoque.exception.movimentacao;

public class QuantidadeMovimentacaoEstoqueNulaException extends RuntimeException {
    public QuantidadeMovimentacaoEstoqueNulaException(String mensagem) {
        super(mensagem);
    }
}
