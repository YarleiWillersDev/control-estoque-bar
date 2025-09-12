package br.com.barghesla.barestoque.service.produto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.validation.produto.ProdutoValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

class ProdutoBuscarPorCategoriaTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Spy
    private ProdutoValidator produtoValidator;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    private Categoria categoria;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Bebidas");
    }

    @Test
    void deveRetornarProdutosQuandoCategoriaExistir() {
        Produto p1 = new Produto(1L, "Cerveja", "Skol", 50, new BigDecimal(5.50), categoria, null);
        Produto p2 = new Produto(2L, "Vodka", "Absolut", 20, new BigDecimal(80.0), categoria, null);

        when(produtoRepository.findByCategoriaId(categoria.getId()))
                .thenReturn(Arrays.asList(p1, p2));

        List<Produto> produtos = produtoService.buscarPorCategoria(categoria.getId());

        assertThat(produtos).hasSize(2);
        assertThat(produtos).extracting("nome").contains("Cerveja", "Vodka");
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoTiverProdutos() {
        when(produtoRepository.findByCategoriaId(categoria.getId()))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> produtoService.buscarPorCategoria(categoria.getId()))
                .isInstanceOf(ProdutoNaoCadastradoException.class)
                .hasMessage("Não existem produtos cadastrados para este id na base de dados!");
    }

    @Test
    void deveRetornarListaComUmProdutoQuandoSoUmExistirNaCategoria() {
        Produto p1 = new Produto(1L, "Guaraná", "Antarctica", 100, new BigDecimal(6.0), categoria, null);

        when(produtoRepository.findByCategoriaId(categoria.getId()))
                .thenReturn(List.of(p1));

        List<Produto> produtos = produtoService.buscarPorCategoria(categoria.getId());

        assertThat(produtos).hasSize(1);
        assertThat(produtos.get(0).getNome()).isEqualTo("Guaraná");
    }
}

