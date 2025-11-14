package br.com.barghesla.barestoque.exception.usuario;

public class CampoNomeNuloException extends RuntimeException {
    public CampoNomeNuloException(String mensagem) {
        super(mensagem);
    }
}
