package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.exception.produto.ProdutoJaAtivoException;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@SpringBootTest
class ProdutoAtivarTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    private Produto produto;

    @BeforeEach
    void setup() {
        produtoRepository.deleteAll();
        produto = new Produto();
        produto.setNome("Cerveja");
        produto.setDescricao("Cerveja artesanal");
        produto.setPrecoUnitario(new BigDecimal(10.0));
        produto.setQuantidade(50);
        produto.setStatus(StatusProduto.INATIVO);
        produto = produtoRepository.save(produto);
    }

    @Test
    void deveAtivarProdutoComSucesso() {
        Produto produtoAtivo = produtoService.ativar(produto.getId());
        assertEquals(StatusProduto.ATIVO, produtoAtivo.getStatus());
    }

    @Test
    void naoDeveAtivarProdutoJaAtivo() {
        produto.setStatus(StatusProduto.ATIVO);
        produtoRepository.save(produto);

        assertThrows(ProdutoJaAtivoException.class, () -> {
            produtoService.ativar(produto.getId());
        });
    }

    @Test
    void naoDeveAtivarProdutoInexistente() {
        assertThrows(ProdutoNaoCadastradoException.class, () -> {
            produtoService.ativar(999L);
        });
    }
}

