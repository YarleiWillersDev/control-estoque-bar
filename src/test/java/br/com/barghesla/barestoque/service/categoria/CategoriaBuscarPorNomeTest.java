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
    @DisplayName("Deve retornar uma lista de DTOs quando o nome existir")
    void deveRetornarCategoriaQuandoNomeExistir() {
        // Arrange
        categoriaRepository.save(new Categoria(null, "Bebidas Teste"));

        // Act
        // O resultado agora é uma lista de CategoriaResponse
        List<CategoriaResponse> resultado = categoriaService.buscarPorNome("bebidas");

        // Assert
        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).nome()).isEqualTo("Bebidas Teste");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando a categoria não for encontrada")
    void deveRetornarListaVaziaQuandoCategoriaNaoForEncontrada() {
        // Act
        List<CategoriaResponse> resultado = categoriaService.buscarPorNome("naoexiste");

        // Assert
        // A convenção para buscas sem resultado é retornar uma lista vazia, não uma exceção.
        assertThat(resultado).isEmpty();
    }
}
