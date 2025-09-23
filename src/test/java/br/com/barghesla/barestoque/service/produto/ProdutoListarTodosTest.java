package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // garante rollback a cada teste
class ProdutoListarTodosTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setup() {
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve listar todos os produtos com sucesso")
    void deveListarTodosProdutosComSucesso() {
        // Arrange
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas+"));

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
        assertThat(produtos).extracting(Produto::getNome)
                .containsExactlyInAnyOrder("Cerveja", "Refrigerante");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremProdutos() {
        List<Produto> produtos = produtoService.listarTodos();
        assertThat(produtos).isEmpty();
    }

}
