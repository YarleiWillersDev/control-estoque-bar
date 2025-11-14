package br.com.barghesla.barestoque.exception.movimentacao;

public class DataMovimentacaoEstoqueNulaException extends RuntimeException {
    public DataMovimentacaoEstoqueNulaException(String mensagem) {
        super(mensagem);
    }
}
