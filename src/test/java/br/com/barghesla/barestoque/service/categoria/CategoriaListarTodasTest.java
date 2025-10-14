package br.com.barghesla.barestoque.service.categoria;

import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;
import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista de DTOs de categoria, ordenados por nome")
    void deveRetornarCategoriasOrdenadasQuandoExistirem() {
        // Arrange
        categoriaRepository.save(new Categoria(null, "Bebidas"));
        categoriaRepository.save(new Categoria(null, "Aperitivos"));

        // Act
        // O resultado agora é uma lista de CategoriaResponse
        List<CategoriaResponse> categorias = categoriaService.listarTodas();

        // Assert
        assertThat(categorias).hasSize(2);
        // Verifica a ordem alfabética
        assertThat(categorias.get(0).nome()).isEqualTo("Aperitivos");
        assertThat(categorias.get(1).nome()).isEqualTo("Bebidas");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não existirem categorias")
    void deveRetornarListaVaziaQuandoNaoExistiremCategorias() {
        // Act
        List<CategoriaResponse> categorias = categoriaService.listarTodas();

        // Assert
        // O correto é verificar se a lista está vazia, não esperar uma exceção.
        assertThat(categorias).isEmpty();
    }
}
