package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.dto.produto.ProdutoRequest;
import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
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
class ProdutoAtualizaTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Produto produtoExistente;
    private Categoria categoria;
    private Categoria novaCategoria;

    @BeforeEach
    void setup() {
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();

        categoria = new Categoria();
        categoria.setNome("Bebidas-");
        categoria = categoriaRepository.save(categoria);

        novaCategoria = new Categoria();
        novaCategoria.setNome("Lanches");
        novaCategoria = categoriaRepository.save(novaCategoria);

        produtoExistente = new Produto();
        produtoExistente.setNome("Cerveja Original");
        produtoExistente.setPrecoUnitario(new BigDecimal("8.00"));
        produtoExistente.setQuantidade(20);
        produtoExistente.setStatus(StatusProduto.ATIVO);
        produtoExistente.setCategoria(categoria);
        produtoExistente = produtoRepository.save(produtoExistente);
    }

    @Test
    @DisplayName("Deve atualizar um produto com sucesso")
    void deveAtualizarProdutoComSucesso() {
        // Preparação: Criar o DTO de requisição com os novos dados
        ProdutoRequest requestComNovosDados = new ProdutoRequest(
                "Cerveja Skol",
                "Pilsen",
                20,
                new BigDecimal("5.00"),
                novaCategoria.getId()
        );

        // Ação: Chamar o método de serviço com o ID do produto e o DTO de requisição
        ProdutoResponse response = produtoService.atualizar(produtoExistente.getId(), requestComNovosDados);

        // Verificação: Garantir que a resposta contém os dados atualizados
        assertNotNull(response);
        assertEquals(produtoExistente.getId(), response.id());
        assertEquals("Cerveja Skol", response.nome());
        assertEquals("Pilsen", response.descricao());
        assertEquals(20, response.quantidade());
        // Comparar BigDecimal com compareTo para evitar problemas de escala
        assertEquals(0, new BigDecimal("5.00").compareTo(response.precoUnitario()));
        assertEquals("Lanches", response.categoriaID().nome());
    }
}
