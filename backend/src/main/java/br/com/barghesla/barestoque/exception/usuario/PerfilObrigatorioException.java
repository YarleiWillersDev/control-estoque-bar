package br.com.barghesla.barestoque.exception.usuario;

public class PerfilObrigatorioException extends RuntimeException {
    public PerfilObrigatorioException(String mensagem) {
        super(mensagem);
    }
}
