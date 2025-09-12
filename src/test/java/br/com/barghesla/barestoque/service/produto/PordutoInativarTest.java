package br.com.barghesla.barestoque.service.produto;

import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.exception.produto.ProdutoJaInativoException;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@SpringBootTest
class ProdutoInativarTest {

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
        produto.setStatus(StatusProduto.ATIVO);
        produto = produtoRepository.save(produto);
    }

    @Test
    void deveInativarProdutoComSucesso() {
        produtoService.inativar(produto.getId());

        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).get();
        assertEquals(StatusProduto.INATIVO, produtoAtualizado.getStatus());
    }

    @Test
    void naoDeveInativarProdutoJaInativo() {
        produto.setStatus(StatusProduto.INATIVO);
        produtoRepository.save(produto);

        assertThrows(ProdutoJaInativoException.class, () -> {
            produtoService.inativar(produto.getId());
        });
    }

    @Test
    void naoDeveInativarProdutoInexistente() {
        assertThrows(ProdutoNaoCadastradoException.class, () -> {
            produtoService.inativar(999L);
        });
    }
}

