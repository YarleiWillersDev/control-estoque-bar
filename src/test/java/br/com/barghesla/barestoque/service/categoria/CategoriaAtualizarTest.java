package br.com.barghesla.barestoque.service.categoria;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.exception.categoria.CategoriaJaExistenteException;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CategoriaAtualizarTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @AfterEach
    void limparBanco() {
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve atualizar categoria com sucesso")
    void deveAtualizarCategoriaComSucesso() {
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas0"));

        Categoria atualizada = new Categoria(null, "Alimentos");

        Categoria result = categoriaService.atualizar(categoria.getId(), atualizada);

        assertThat(result.getNome()).isEqualTo("Alimentos");
    }

    @Test
    @DisplayName("Não deve atualizar categoria inexistente")
    void naoDeveAtualizarCategoriaInexistente() {
        Categoria categoria = new Categoria(null, "Alimentos");

        assertThrows(CategoriaNaoEncontradaException.class,
                () -> categoriaService.atualizar(999L, categoria));
    }

    @Test
    @DisplayName("Não deve atualizar categoria para nome duplicado")
    void naoDeveAtualizarCategoriaDuplicada() {
        Categoria cat1 = categoriaRepository.save(new Categoria(null, "Bebidas"));
        categoriaRepository.save(new Categoria(null, "Alimentos"));

        Categoria atualizada = new Categoria(null, "Alimentos");

        assertThrows(CategoriaJaExistenteException.class,
                () -> categoriaService.atualizar(cat1.getId(), atualizada));
    }
}
