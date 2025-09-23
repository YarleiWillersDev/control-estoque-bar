package br.com.barghesla.barestoque.exception.usuario;

public class NomeDuplicadoException extends RuntimeException {
    public NomeDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
