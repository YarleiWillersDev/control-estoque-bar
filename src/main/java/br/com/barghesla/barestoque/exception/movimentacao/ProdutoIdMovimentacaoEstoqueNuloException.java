package br.com.barghesla.barestoque.exception.movimentacao;

public class ProdutoIdMovimentacaoEstoqueNuloException extends RuntimeException {
    public ProdutoIdMovimentacaoEstoqueNuloException(String mensagem) {
        super(mensagem);
    }
}
