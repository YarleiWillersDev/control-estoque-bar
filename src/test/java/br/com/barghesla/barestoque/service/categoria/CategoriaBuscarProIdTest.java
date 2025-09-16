package br.com.barghesla.barestoque.service.categoria;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CategoriaBuscarPorIdTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @AfterEach
    void limparBanco() {
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve buscar categoria por ID com sucesso")
    void deveBuscarCategoriaPorId() {
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas"));

        Categoria encontrada = categoriaService.buscarPorId(categoria.getId());

        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getNome()).isEqualTo("Bebidas");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar categoria inexistente")
    void deveLancarExcecaoQuandoCategoriaNaoExistir() {
        assertThrows(CategoriaNaoEncontradaException.class,
                () -> categoriaService.buscarPorId(999L));
    }
}
