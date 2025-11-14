package br.com.barghesla.barestoque.exception.usuario;

public class SenhaInvalidaException extends RuntimeException{
    public SenhaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
