package br.com.barghesla.barestoque.exception.usuario;

public class SenhaFracaException extends RuntimeException {
    public SenhaFracaException(String mensagem) {
        super(mensagem);
    }
}
