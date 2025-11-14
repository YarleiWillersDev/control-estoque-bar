package br.com.barghesla.barestoque.service.categoria;

import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional // Adicionado para rollback automático
class CategoriaBuscarPorIdTest {

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
    @DisplayName("Deve buscar categoria por ID com sucesso")
    void deveBuscarCategoriaPorId() {
        // Arrange: Salvar uma categoria no banco para ter um ID válido
        Categoria categoriaSalva = categoriaRepository.save(new Categoria(null, "Bebidas"));

        // Act: Chamar o serviço, que agora retorna um CategoriaResponse
        CategoriaResponse response = categoriaService.buscarPorId(categoriaSalva.getId());

        // Assert: Validar os campos do DTO de resposta
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(categoriaSalva.getId());
        assertThat(response.nome()).isEqualTo("Bebidas");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar categoria inexistente")
    void deveLancarExcecaoQuandoCategoriaNaoExistir() {
        // Arrange
        long idInexistente = 999L;

        // Act & Assert
        assertThrows(CategoriaNaoEncontradaException.class,
                () -> categoriaService.buscarPorId(idInexistente));
    }
}
