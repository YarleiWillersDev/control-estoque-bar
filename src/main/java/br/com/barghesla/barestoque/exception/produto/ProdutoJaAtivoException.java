package br.com.barghesla.barestoque.exception.produto;

public class ProdutoJaAtivoException extends RuntimeException {
    public ProdutoJaAtivoException(String mensagem) {
        super(mensagem);
    }
}
