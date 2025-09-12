package br.com.barghesla.barestoque.exception.produto;

public class ProdutoNaoCadastradoException extends RuntimeException{
    public ProdutoNaoCadastradoException(String mensagem) {
        super(mensagem);
    }
}
