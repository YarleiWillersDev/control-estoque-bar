package br.com.barghesla.barestoque.validation.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
import br.com.barghesla.barestoque.exception.produto.ProdutoJaAtivoException;
import br.com.barghesla.barestoque.exception.produto.ProdutoJaInativoException;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.exception.produto.QuantidadeInvalidaException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;

@Component
public class ProdutoValidator {
    
    @Autowired
    private CategoriaRepository categoriaRepository;;

    public void validar(Produto produto) {
        validarQuantidade(produto);
        validarCategoria(produto);
    }

    public void validarQuantidade(Produto produto) {
        if (produto.getQuantidade() < 0) {
            throw new QuantidadeInvalidaException("Quantidade não pode ser negativa!");
        }
    }

    public void validarCategoria(Produto produto) {
        if (produto.getCategoria() != null) {
            categoriaRepository.findById(produto.getCategoria().getId())
                .orElseThrow(() -> new CategoriaNaoEncontradaException("Categoria Inválida!"));
        }
    }

    public void atualizarCampos(Produto existente, Produto novo) {
        if (novo.getNome() != null) existente.setNome(novo.getNome());
        if (novo.getDescricao() != null) existente.setDescricao(novo.getDescricao());
        if (novo.getQuantidade() != null) existente.setQuantidade(novo.getQuantidade());
        if (novo.getPrecoUnitario() != null) existente.setPrecoUnitario(novo.getPrecoUnitario());
        if (novo.getCategoria() != null) existente.setCategoria(novo.getCategoria());
    }

    public void validarAtivacao(Produto produto) {
        if (produto.getStatus() == StatusProduto.ATIVO) {
            throw new ProdutoJaAtivoException("Produto já está ATIVO!");
        }
    }

    public void validarInativacao(Produto produto) {
        if (produto.getStatus() == StatusProduto.INATIVO) {
            throw new ProdutoJaInativoException("Produto já está INATIVO");
        }
    }

    public void ativar(Produto produto) {
        validarAtivacao(produto);
        produto.setStatus(StatusProduto.ATIVO);
    }

    public void inativar(Produto produto) {
        validarInativacao(produto);
        produto.setStatus(StatusProduto.INATIVO);
    }

    public void validarListaVazia(List<Produto> produtos, String nome) {
        validarListaVaziaInterno(produtos, "Não existem produtos cadastrados para este nome na base de dados!");
    }

    public void validarListaVazia(List<Produto> produtos, Long categoriaId) {
        validarListaVaziaInterno(produtos, "Não existem produtos cadastrados para este id na base de dados!");
    }

    public void validarListaVazia(List<Produto> produtos) {
        validarListaVaziaInterno(produtos, "Não existem produtos cadastrados na base de dados!");
    }

    private void validarListaVaziaInterno(List<Produto> produtos, String mensagemCustomizada) {
        if (produtos == null || produtos.isEmpty()) {
            throw new ProdutoNaoCadastradoException(mensagemCustomizada);
        }
    }
}
