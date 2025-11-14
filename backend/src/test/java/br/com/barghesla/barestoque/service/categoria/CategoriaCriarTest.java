package br.com.barghesla.barestoque.service.categoria;

import br.com.barghesla.barestoque.dto.categoria.CategoriaRequest;
import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;
import br.com.barghesla.barestoque.exception.categoria.CategoriaJaExistenteException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class CategoriaCriarTest {

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
    @DisplayName("Deve criar uma nova categoria com sucesso")
    void deveCriarCategoriaComSucesso() {
        // Arrange: Criar o DTO de requisição
        CategoriaRequest request = new CategoriaRequest("Bebidas");

        // Act: Chamar o serviço, que agora recebe um request e retorna um response
        CategoriaResponse response = categoriaService.criar(request);

        // Assert: Validar o DTO de resposta
        assertNotNull(response.id(), "O ID da categoria não deve ser nulo após a criação.");
        assertThat(response.nome()).isEqualTo("Bebidas");
    }

    @Test
    @DisplayName("Não deve criar categoria com nome duplicado")
    void naoDeveCriarCategoriaDuplicada() {
        // Arrange: Criar a primeira categoria usando o DTO
        CategoriaRequest request = new CategoriaRequest("Bebidas");
        categoriaService.criar(request);

        // Act & Assert: Verificar se a tentativa de criar a mesma categoria lança a exceção correta
        assertThatThrownBy(() -> categoriaService.criar(new CategoriaRequest("Bebidas")))
                .isInstanceOf(CategoriaJaExistenteException.class)
                .hasMessage("Já existe uma categoria com este nome cadastrada na base de dados!");
    }
}
