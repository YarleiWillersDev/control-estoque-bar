package br.com.barghesla.barestoque.service.produto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.updater.produto.ProdutoUpdater;
import br.com.barghesla.barestoque.validation.produto.ProdutoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProdutoBuscarPorCategoriaTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    private ProdutoValidator produtoValidator;

    private ProdutoUpdater produtoUpdater;

    private ProdutoServiceImpl produtoService;

    private Categoria categoria;

    @BeforeEach
    void setup() {
        produtoValidator = new ProdutoValidator(categoriaRepository);

        produtoService = new ProdutoServiceImpl(produtoRepository, produtoValidator, produtoUpdater);

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Bebidas");
    }

    @Test
    void deveRetornarProdutosQuandoCategoriaExistir() {
        Produto p1 = new Produto(1L, "Cerveja", "Skol", 50, new BigDecimal(5.50), categoria, null);
        Produto p2 = new Produto(2L, "Vodka", "Absolut", 20, new BigDecimal(80.0), categoria, null);

        when(produtoRepository.findByCategoriaIdOrderByNomeAsc(categoria.getId()))
                .thenReturn(Arrays.asList(p1, p2));

        List<Produto> produtos = produtoService.buscarPorCategoria(categoria.getId());

        assertThat(produtos).hasSize(2);
        assertThat(produtos).extracting("nome").contains("Cerveja", "Vodka");
    }

    @Test
    void deveRetornarListaVaziaQuandoCategoriaNaoTiverProdutos() {
        when(produtoRepository.findByCategoriaIdOrderByNomeAsc(categoria.getId()))
                .thenReturn(Collections.emptyList());

        List<Produto> produtos = produtoService.buscarPorCategoria(categoria.getId());

        assertThat(produtos).isEmpty();
    }

    @Test
    void deveRetornarListaComUmProdutoQuandoSoUmExistirNaCategoria() {
        Produto p1 = new Produto(1L, "Guaraná", "Antarctica", 100, new BigDecimal(6.0), categoria, null);

        when(produtoRepository.findByCategoriaIdOrderByNomeAsc(categoria.getId()))
                .thenReturn(List.of(p1));

        List<Produto> produtos = produtoService.buscarPorCategoria(categoria.getId());

        assertThat(produtos).hasSize(1);
        assertThat(produtos.get(0).getNome()).isEqualTo("Guaraná");
    }
}
