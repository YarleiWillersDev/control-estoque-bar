package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.model.Produto;
import br.com.barghesla.barestoque.model.StatusProduto;
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
@Transactional
class ProdutoBuscarPorIdTest {

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
        categoriaRepository.deleteAll();

        Categoria categoria = new Categoria();
        categoria.setNome("Bebidas-");
        categoria = categoriaRepository.save(categoria);

        produto = new Produto();
        produto.setNome("Cerveja IPA");
        produto.setPrecoUnitario(new BigDecimal("12.50"));
        produto.setQuantidade(20);
        produto.setStatus(StatusProduto.ATIVO);
        produto.setCategoria(categoria);
        produto = produtoRepository.save(produto);
    }

    @Test
    @DisplayName("Deve buscar um produto por ID com sucesso")
    void deveBuscarProdutoPorIdComSucesso() {
        // Ação: Chamar o serviço, que agora retorna ProdutoResponse
        ProdutoResponse response = produtoService.buscarPorId(produto.getId());

        // Verificação: Fazer as asserções nos campos do DTO
        assertNotNull(response);
        assertEquals(produto.getId(), response.id());
        assertEquals("Cerveja IPA", response.nome());
        assertEquals("Bebidas-", response.categoria().nome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar produto com ID inexistente")
    void naoDeveBuscarProdutoInexistente() {
        Long idInexistente = 999L;
        assertThrows(ProdutoNaoCadastradoException.class, () -> {
            produtoService.buscarPorId(idInexistente);
        });
    }
}
