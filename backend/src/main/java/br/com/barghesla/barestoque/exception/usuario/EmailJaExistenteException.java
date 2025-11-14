package br.com.barghesla.barestoque.exception.usuario;

public class EmailJaExistenteException extends RuntimeException{
    public EmailJaExistenteException(String mensagem) {
        super(mensagem);
    }
}
