package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
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
@Transactional
class ProdutoBuscarPorNomeTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @BeforeEach
    void setup() {
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar um DTO de produto ao buscar por nome existente")
    void deveRetornarProdutoAoBuscarPorNomeExistente() {
        // Preparação
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas-"));
        Produto produto = new Produto();
        produto.setNome("Cerveja");
        produto.setCategoria(categoria);
        produto.setQuantidade(50);
        produto.setPrecoUnitario(new BigDecimal(8.50));
        produtoRepository.save(produto);

        // Ação: O tipo da variável de retorno agora é List<ProdutoResponse>
        List<ProdutoResponse> resultado = produtoService.buscarPorNome("cerveja"); // case-insensitive

        // Verificação: As asserções são feitas na lista de DTOs
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nome()).isEqualTo("Cerveja");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nome do produto não existe")
    void deveRetornarListaVaziaQuandoNomeNaoExiste() {
        // Ação
        List<ProdutoResponse> resultado = produtoService.buscarPorNome("Vodka");
        
        // Verificação
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar múltiplos DTOs quando o nome contém o trecho buscado")
    void deveRetornarMultiplosProdutosQuandoNomeContemTrecho() {
        // Preparação
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas"));

        Produto produto1 = new Produto();
        produto1.setNome("Cerveja");
        produto1.setCategoria(categoria);
        produto1.setQuantidade(50);
        produto1.setPrecoUnitario(new BigDecimal(8.50));
        produtoRepository.save(produto1);

        Produto produto2 = new Produto();
        produto2.setNome("Cerveja Artesanal");
        produto2.setCategoria(categoria);
        produto2.setQuantidade(50);
        produto2.setPrecoUnitario(new BigDecimal(8.50));
        produtoRepository.save(produto2);

        // Ação
        List<ProdutoResponse> resultado = produtoService.buscarPorNome("Cerveja");

        // Verificação
        assertThat(resultado).hasSize(2);
        // Usar method reference (::) para extrair o campo 'nome' dos DTOs
        assertThat(resultado).extracting(ProdutoResponse::nome)
                .containsExactlyInAnyOrder("Cerveja", "Cerveja Artesanal");
    }
}
