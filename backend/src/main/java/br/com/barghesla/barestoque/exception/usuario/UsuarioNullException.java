package br.com.barghesla.barestoque.exception.usuario;

public class UsuarioNullException extends RuntimeException{
    public UsuarioNullException(String mensagem) {
        super(mensagem);
    }
}
