package br.com.barghesla.barestoque.exception.categoria;

public class CategoriaNaoEncontradaException extends RuntimeException{
    public CategoriaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
