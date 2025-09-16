package br.com.barghesla.barestoque.exception.usuario;

public class LoginJaExistenteException extends RuntimeException{
    public LoginJaExistenteException(String mensagem) {
        super(mensagem);
    }
}
