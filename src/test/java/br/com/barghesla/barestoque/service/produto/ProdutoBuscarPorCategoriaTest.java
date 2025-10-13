package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.updater.produto.ProdutoUpdater;
import br.com.barghesla.barestoque.validation.produto.ProdutoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoBuscarPorCategoriaTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProdutoValidator produtoValidator;

    @Mock
    private ProdutoUpdater produtoUpdater;

    // A classe que estamos testando de fato
    private ProdutoServiceImpl produtoService;

    private Categoria categoria;

    @BeforeEach
    void setup() {
        // CORREÇÃO 1: Usar o novo construtor do ProdutoServiceImpl
        // Agora incluindo o 'categoriaRepository' que foi adicionado.
        produtoService = new ProdutoServiceImpl(produtoRepository, produtoValidator, produtoUpdater, categoriaRepository);

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Bebidas-");
    }

    @Test
    @DisplayName("Deve retornar uma lista de DTOs de produto quando a categoria existir")
    void deveRetornarProdutosQuandoCategoriaExistir() {
        // Preparação: Criar as entidades que o mock do repositório irá retornar
        Produto p1 = new Produto(1L, "Cerveja", "Skol", 50, new BigDecimal("5.50"), categoria, null);
        Produto p2 = new Produto(2L, "Vodka", "Absolut", 20, new BigDecimal("80.00"), categoria, null);
        when(produtoRepository.findByCategoriaIdOrderByNomeAsc(categoria.getId())).thenReturn(Arrays.asList(p1, p2));

        // Ação: Chamar o método do serviço
        // CORREÇÃO 2: O tipo da variável de retorno agora é List<ProdutoResponse>
        List<ProdutoResponse> produtosResponse = produtoService.buscarPorCategoria(categoria.getId());

        // Verificação: Fazer as asserções na lista de DTOs
        assertThat(produtosResponse).hasSize(2);
        // Usar method reference (::) para extrair o campo 'nome' de forma segura
        assertThat(produtosResponse).extracting(ProdutoResponse::nome).containsExactlyInAnyOrder("Cerveja", "Vodka");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando a categoria não tiver produtos")
    void deveRetornarListaVaziaQuandoCategoriaNaoTiverProdutos() {
        // Preparação
        when(produtoRepository.findByCategoriaIdOrderByNomeAsc(categoria.getId())).thenReturn(Collections.emptyList());

        // Ação
        List<ProdutoResponse> produtosResponse = produtoService.buscarPorCategoria(categoria.getId());

        // Verificação
        assertThat(produtosResponse).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar uma lista com um único produto DTO")
    void deveRetornarListaComUmProdutoQuandoSoUmExistirNaCategoria() {
        // Preparação
        Produto p1 = new Produto(1L, "Guaraná", "Antarctica", 100, new BigDecimal("6.00"), categoria, null);
        when(produtoRepository.findByCategoriaIdOrderByNomeAsc(categoria.getId())).thenReturn(List.of(p1));

        // Ação
        List<ProdutoResponse> produtosResponse = produtoService.buscarPorCategoria(categoria.getId());

        // Verificação
        assertThat(produtosResponse).hasSize(1);
        assertThat(produtosResponse.get(0).nome()).isEqualTo("Guaraná");
    }
}
