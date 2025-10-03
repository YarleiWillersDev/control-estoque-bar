package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.exception.produto.ProdutoJaInativoException;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // garante rollback após cada teste, isolando os cenários
class ProdutoInativarTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Produto produto;

    @BeforeEach
    void setup() {
        produtoRepository.deleteAll();

        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas-"));

        produto = new Produto();
        produto.setNome("Cerveja");
        produto.setDescricao("Cerveja artesanal");
        produto.setPrecoUnitario(BigDecimal.valueOf(10.0));
        produto.setQuantidade(50);
        produto.setCategoria(categoria);
        produto.setStatus(StatusProduto.ATIVO);

        produto = produtoRepository.saveAndFlush(produto);
    }

    @Test
    @DisplayName("Deve inativar um produto ativo com sucesso")
    void deveInativarProdutoComSucesso() {
        produtoService.inativar(produto.getId());

        Produto produtoAtualizado = produtoRepository.findById(produto.getId())
                .orElseThrow(() -> new IllegalStateException("Produto não encontrado após inativação"));

        assertEquals(StatusProduto.INATIVO, produtoAtualizado.getStatus());
    }

    @Test
    @DisplayName("Não deve inativar produto que já está inativo")
    void naoDeveInativarProdutoJaInativo() {
        produto.setStatus(StatusProduto.INATIVO);
        produtoRepository.saveAndFlush(produto);

        assertThrows(ProdutoJaInativoException.class,
                () -> produtoService.inativar(produto.getId()));
    }

    @Test
    @DisplayName("Não deve inativar produto inexistente")
    void naoDeveInativarProdutoInexistente() {
        assertThrows(ProdutoNaoCadastradoException.class,
                () -> produtoService.inativar(999L));
    }
}
