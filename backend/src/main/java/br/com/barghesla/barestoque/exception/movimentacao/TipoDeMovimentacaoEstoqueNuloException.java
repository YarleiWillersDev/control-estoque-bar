package br.com.barghesla.barestoque.exception.movimentacao;

public class TipoDeMovimentacaoEstoqueNuloException extends RuntimeException {
    public TipoDeMovimentacaoEstoqueNuloException(String mensagem) {
        super(mensagem);
    }
}
