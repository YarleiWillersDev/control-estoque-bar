package br.com.barghesla.barestoque.exception.produto;

public class QuantidadeInvalidaException extends RuntimeException{
    public QuantidadeInvalidaException(String mensagem) {
        super(mensagem);
    }
}
