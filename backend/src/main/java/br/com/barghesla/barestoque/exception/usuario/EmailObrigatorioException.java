package br.com.barghesla.barestoque.exception.usuario;

public class EmailObrigatorioException extends RuntimeException {
    public EmailObrigatorioException(String mensagem) {
        super(mensagem);
    }
}
