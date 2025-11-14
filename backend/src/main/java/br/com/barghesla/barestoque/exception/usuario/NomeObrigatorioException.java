package br.com.barghesla.barestoque.exception.usuario;

public class NomeObrigatorioException extends RuntimeException{
    public NomeObrigatorioException(String mensagem) {
        super(mensagem);
    }
}
