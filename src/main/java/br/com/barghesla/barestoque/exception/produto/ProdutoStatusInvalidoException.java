package br.com.barghesla.barestoque.exception.produto;

public class ProdutoStatusInvalidoException extends RuntimeException {
    public ProdutoStatusInvalidoException(String mensagem) {
        super(mensagem);
    }
}
