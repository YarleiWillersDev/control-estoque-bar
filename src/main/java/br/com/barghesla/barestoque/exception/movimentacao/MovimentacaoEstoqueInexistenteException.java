package br.com.barghesla.barestoque.exception.movimentacao;

public class MovimentacaoEstoqueInexistenteException extends RuntimeException {
    public MovimentacaoEstoqueInexistenteException(String mensagem) {
        super(mensagem);
    }
}
