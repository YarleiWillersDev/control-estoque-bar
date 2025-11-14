package br.com.barghesla.barestoque.exception.movimentacao;

public class QuantidadeMovimentacaoEstoqueNegativaException extends RuntimeException {
    public QuantidadeMovimentacaoEstoqueNegativaException(String mensagem) {
        super(mensagem);
    }

}
