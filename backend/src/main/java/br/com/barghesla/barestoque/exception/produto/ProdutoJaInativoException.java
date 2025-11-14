package br.com.barghesla.barestoque.exception.produto;

public class ProdutoJaInativoException extends RuntimeException{
    public ProdutoJaInativoException(String mensagem) {
        super(mensagem);
    }
}
