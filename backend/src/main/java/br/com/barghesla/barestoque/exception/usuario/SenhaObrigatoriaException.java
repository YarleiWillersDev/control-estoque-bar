package br.com.barghesla.barestoque.exception.usuario;

public class SenhaObrigatoriaException extends RuntimeException {
    public SenhaObrigatoriaException(String mensagem) {
        super(mensagem);
    }
}
