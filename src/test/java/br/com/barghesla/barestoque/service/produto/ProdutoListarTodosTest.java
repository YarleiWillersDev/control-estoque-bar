package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.model.Produto;
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
@Transactional // Garante que as operações de cada teste sejam revertidas ao final
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
    @DisplayName("Deve listar todos os produtos como uma lista de DTOs")
    void deveListarTodosProdutosComSucesso() {
        // Arrange: Preparar o cenário com alguns produtos no banco
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas-"));

        Produto produto1 = new Produto();
        produto1.setNome("Cerveja");
        produto1.setPrecoUnitario(new BigDecimal("5.00"));
        produto1.setQuantidade(50);
        produto1.setCategoria(categoria);

        Produto produto2 = new Produto();
        produto2.setNome("Refrigerante");
        produto2.setPrecoUnitario(new BigDecimal("7.00"));
        produto2.setQuantidade(50);
        produto2.setCategoria(categoria);

        produtoRepository.save(produto1);
        produtoRepository.save(produto2);

        // Act: Chamar o método do serviço
        // CORREÇÃO: O tipo de retorno agora é List<ProdutoResponse>
        List<ProdutoResponse> produtosResponse = produtoService.listarTodos();

        // Assert: Validar a lista de DTOs retornada
        assertThat(produtosResponse).hasSize(2);
        // CORREÇÃO: Extrair o nome a partir do DTO (ProdutoResponse::nome)
        assertThat(produtosResponse).extracting(ProdutoResponse::nome)
                .containsExactlyInAnyOrder("Cerveja", "Refrigerante");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não existirem produtos cadastrados")
    void deveRetornarListaVaziaQuandoNaoExistiremProdutos() {
        // Act
        List<ProdutoResponse> produtosResponse = produtoService.listarTodos();
        
        // Assert
        assertThat(produtosResponse).isEmpty();
    }
}
