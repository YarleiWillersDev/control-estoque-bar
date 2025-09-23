package br.com.barghesla.barestoque.exception.produto;

public class PrecoInvalidoException extends RuntimeException{
    public PrecoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
