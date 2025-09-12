package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProdutoListarTodosTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @AfterEach
    void limparBanco() {
        // Remove tudo para não deixar lixo para os próximos testes
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    void deveListarTodosProdutosComSucesso() {
        // Arrange
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas"));

        Produto produto1 = new Produto();
        produto1.setNome("Cerveja");
        produto1.setDescricao("Pilsen");
        produto1.setPrecoUnitario(new BigDecimal("5.00"));
        produto1.setQuantidade(10);
        produto1.setCategoria(categoria);

        Produto produto2 = new Produto();
        produto2.setNome("Refrigerante");
        produto2.setDescricao("Cola");
        produto2.setPrecoUnitario(new BigDecimal("7.00"));
        produto2.setQuantidade(20);
        produto2.setCategoria(categoria);

        produtoRepository.save(produto1);
        produtoRepository.save(produto2);

        // Act
        List<Produto> produtos = produtoService.listarTodos();

        // Assert
        assertThat(produtos).hasSize(2);
        assertThat(produtos).extracting("nome")
                .containsExactlyInAnyOrder("Cerveja", "Refrigerante");
    }

    @Test
    void deveLancarExcecaoQuandoNaoExistiremProdutos() {
        // Arrange → garante banco vazio
        produtoRepository.deleteAll();

        // Act + Assert
        assertThatThrownBy(() -> produtoService.listarTodos())
                .isInstanceOf(ProdutoNaoCadastradoException.class)
                .hasMessage("Não existem produtos cadastrados na base de dados!");
    }
}
