package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProdutoBuscarPorIdTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveRetornarProdutoQuandoIdExistir() {
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Destilados"));

        Produto produto = new Produto();
        produto.setNome("Vodka Absolut");
        produto.setDescricao("Vodka Sueca Premium");
        produto.setPrecoUnitario(new BigDecimal("89.90"));
        produto.setQuantidade(20);
        produto.setCategoria(categoria);

        produto = produtoRepository.save(produto);

        Produto encontrado = produtoService.buscarPorId(produto.getId());

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNome()).isEqualTo("Vodka Absolut");
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExistir() {
        assertThatThrownBy(() -> produtoService.buscarPorId(999L))
                .isInstanceOf(ProdutoNaoCadastradoException.class)
                .hasMessage("Não existem produtos cadastrados para este id na base de dados!");
    }
}


