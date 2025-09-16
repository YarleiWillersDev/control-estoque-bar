package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
    void deveRetornarProdutoAoBuscarPorNomeExistente() {
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas"));

        Produto produto = new Produto();
        produto.setNome("Cerveja");
        produto.setDescricao("Cerveja Pilsen");
        produto.setPrecoUnitario(new BigDecimal("5.00"));
        produto.setQuantidade(10);
        produto.setCategoria(categoria);
        produtoRepository.save(produto);

        List<Produto> resultado = produtoService.buscarPorNome("cerveja"); // case-insensitive

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Cerveja");
    }

    @Test
    void deveRetornarListaVaziaQuandoNomeNaoExiste() {
        List<Produto> resultado = produtoService.buscarPorNome("Vodka");
        assertThat(resultado).isEmpty();
    }

    @Test
    void deveRetornarMultiplosProdutosQuandoNomeContemTrecho() {
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas"));

        Produto produto1 = new Produto();
        produto1.setNome("Cerveja");
        produto1.setDescricao("Cerveja Pilsen");
        produto1.setPrecoUnitario(new BigDecimal("5.00"));
        produto1.setQuantidade(10);
        produto1.setCategoria(categoria);
        produtoRepository.save(produto1);

        Produto produto2 = new Produto();
        produto2.setNome("Cerveja Artesanal");
        produto2.setDescricao("IPA");
        produto2.setPrecoUnitario(new BigDecimal("12.00"));
        produto2.setQuantidade(15);
        produto2.setCategoria(categoria);
        produtoRepository.save(produto2);

        List<Produto> resultado = produtoService.buscarPorNome("Cerveja");

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Produto::getNome)
                .containsExactlyInAnyOrder("Cerveja", "Cerveja Artesanal");
    }
}
