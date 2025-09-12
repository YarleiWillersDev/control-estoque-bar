package br.com.barghesla.barestoque.exception.produto;

public class ProdutoJaExistenteException extends RuntimeException {
    public ProdutoJaExistenteException(String mensagem) {
        super(mensagem);
    }
}
