package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import java.math.BigDecimal;

@SpringBootTest
class ProdutoAtualizaTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveAtualizarProdutoComSucesso() {
        // Arrange
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas-"));

        Produto produto = new Produto();
        produto.setNome("Cerveja");
        produto.setDescricao("Cerveja Pilsen");
        produto.setPrecoUnitario(new BigDecimal("5.00"));
        produto.setQuantidade(10);
        produto.setCategoria(categoria);

        produto = produtoRepository.save(produto);

        Produto novoProduto = new Produto();
        novoProduto.setNome("Cerveja Artesanal");
        novoProduto.setDescricao("IPA");
        novoProduto.setPrecoUnitario(new BigDecimal("12.00"));
        novoProduto.setQuantidade(15);
        novoProduto.setCategoria(categoria);

        // Act
        Produto atualizado = produtoService.atualizar(produto.getId(), novoProduto);

        // Assert
        assertThat(atualizado.getNome()).isEqualTo("Cerveja Artesanal");
        assertThat(atualizado.getDescricao()).isEqualTo("IPA");
        assertThat(atualizado.getPrecoUnitario()).isEqualTo(new BigDecimal("12.00"));
        assertThat(atualizado.getQuantidade()).isEqualTo(15);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {
        Produto novoProduto = new Produto();
        novoProduto.setNome("Suco");

        assertThatThrownBy(() -> produtoService.atualizar(999L, novoProduto))
                .isInstanceOf(ProdutoNaoCadastradoException.class)
                .hasMessage("Não existem produtos cadastrados para este id na base de dados!");
    }
}
