package br.com.barghesla.barestoque.service.categoria;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;

@SpringBootTest
@Transactional
class CategoriaBuscarPorNomeTest { 

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void limparBanco() {
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    void deveRetornarCategoriaQuandoNomeExistir() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setNome("Bebidas Teste"); // 🔑 nome único
        categoriaRepository.saveAndFlush(categoria);

        // Act
        List<Categoria> resultado = categoriaService.buscarPorNome("bebidas");

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals("Bebidas Teste", resultado.get(0).getNome());
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoForEncontrada() {
        // Act & Assert
        CategoriaNaoEncontradaException exception = assertThrows(
            CategoriaNaoEncontradaException.class,
            () -> categoriaService.buscarPorNome("naoexiste")
        );

        assertEquals(
            "Não existe uma categoria com este nome cadastrada na base de dados!",
            exception.getMessage()
        );
    }
}
