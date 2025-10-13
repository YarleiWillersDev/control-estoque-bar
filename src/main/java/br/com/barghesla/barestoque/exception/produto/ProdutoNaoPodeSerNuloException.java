package br.com.barghesla.barestoque.exception.produto;

public class ProdutoNaoPodeSerNuloException extends RuntimeException {
    public ProdutoNaoPodeSerNuloException(String mensagem) {
        super(mensagem);
    }
}
