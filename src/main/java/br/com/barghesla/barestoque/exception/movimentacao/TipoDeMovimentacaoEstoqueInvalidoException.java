package br.com.barghesla.barestoque.exception.movimentacao;

public class TipoDeMovimentacaoEstoqueInvalidoException extends RuntimeException {
    public TipoDeMovimentacaoEstoqueInvalidoException(String mensagem) {
        super(mensagem);
    }
}
