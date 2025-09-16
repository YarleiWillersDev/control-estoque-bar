package br.com.barghesla.barestoque.exception.categoria;

public class CategoriaJaExistenteException extends RuntimeException {
    public CategoriaJaExistenteException(String mensagem) {
        super(mensagem);
    }
}
