package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.exception.produto.ProdutoJaAtivoException;
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

/**
 * Classe de teste de integração para o método 'ativar' do ProdutoService.
 * A anotação @Transactional garante que cada teste execute em sua própria transação,
 * que será revertida (rollback) ao final, isolando os testes entre si.
 */
@SpringBootTest
@Transactional
class ProdutoAtivarTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Produto produto;

    /**
     * Método de setup que executa antes de cada teste.
     * Limpa os repositórios e cria um cenário base com um produto inativo.
     */
    @BeforeEach
    void setup() {
        // 1. Apaga todos os registros para garantir um estado limpo
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();

        // 2. Cria e salva uma Categoria, pois é uma dependência para o Produto
        Categoria categoria = new Categoria();
        categoria.setNome("Bebidas-");
        categoria = categoriaRepository.save(categoria);

        // 3. Cria e salva o Produto de teste com status INATIVO
        produto = new Produto();
        produto.setNome("Cerveja IPA");
        produto.setDescricao("Cerveja artesanal amarga");
        produto.setPrecoUnitario(new BigDecimal("12.50"));
        produto.setQuantidade(100);
        produto.setStatus(StatusProduto.INATIVO);
        produto.setCategoria(categoria);
        produto = produtoRepository.save(produto);
    }

    @Test
    @DisplayName("Deve ativar um produto inativo com sucesso e retornar o DTO de resposta")
    void deveAtivarProdutoComSucesso() {
        // Ação: Chamar o método de serviço para ativar o produto
        ProdutoResponse produtoAtivadoResponse = produtoService.ativar(produto.getId());

        // Verificação: Garantir que a resposta (DTO) não é nula e que os dados estão corretos
        assertNotNull(produtoAtivadoResponse, "A resposta não deve ser nula.");
        assertEquals(StatusProduto.ATIVO, produtoAtivadoResponse.status(), "O status do produto deve ser ATIVO.");
        assertEquals(produto.getId(), produtoAtivadoResponse.id(), "O ID na resposta deve ser o mesmo do produto ativado.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar ativar um produto que já está ativo")
    void naoDeveAtivarProdutoJaAtivo() {
        // Preparação: Alterar o estado do produto para ATIVO no banco de dados
        produto.setStatus(StatusProduto.ATIVO);
        produtoRepository.save(produto);

        // Ação e Verificação: Verificar se a exceção correta é lançada
        assertThrows(ProdutoJaAtivoException.class, () -> {
            produtoService.ativar(produto.getId());
        }, "Deveria lançar ProdutoJaAtivoException ao tentar ativar um produto já ativo.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar ativar um produto com ID inexistente")
    void naoDeveAtivarProdutoInexistente() {
        // Ação e Verificação: Verificar se a exceção correta é lançada ao usar um ID inválido
        Long idInexistente = 999L;
        assertThrows(ProdutoNaoCadastradoException.class, () -> {
            produtoService.ativar(idInexistente);
        }, "Deveria lançar ProdutoNaoCadastradoException ao tentar ativar um produto inexistente.");
    }
}
