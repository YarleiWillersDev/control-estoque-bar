package br.com.barghesla.barestoque.service.categoria;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;

@SpringBootTest
@Transactional
class CategoriaListarTodasTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void limparBanco() {
        // ordem correta: primeiro apaga filhos, depois pais
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    void deveRetornarCategoriasOrdenadasQuandoExistirem() {
        // Arrange
        Categoria c1 = new Categoria();
        c1.setNome("Bebidas_teste_listar_todas");
        categoriaRepository.save(c1);

        Categoria c2 = new Categoria();
        c2.setNome("Aperitivos_teste_listar_todas");
        categoriaRepository.save(c2);

        // Act
        List<Categoria> categorias = categoriaService.listarTodas();

        // Assert
        assertEquals(2, categorias.size());
        assertEquals("Aperitivos_teste_listar_todas", categorias.get(0).getNome()); // ordem alfabética
        assertEquals("Bebidas_teste_listar_todas", categorias.get(1).getNome());
    }

    @Test
    void deveLancarExcecaoQuandoNaoExistiremCategorias() {
        // Act & Assert
        CategoriaNaoEncontradaException ex = assertThrows(
            CategoriaNaoEncontradaException.class,
            () -> categoriaService.listarTodas()
        );

        assertEquals(
            "Não existe uma categoria com este nome cadastrada na base de dados!",
            ex.getMessage()
        );
    }
}

