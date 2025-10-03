package br.com.barghesla.barestoque.service.categoria;

import br.com.barghesla.barestoque.dto.categoria.CategoriaRequest;
import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;
import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.exception.categoria.CategoriaJaExistenteException;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
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
@Transactional // Adicionado para rollback automático, mais eficiente que @AfterEach
class CategoriaAtualizarTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // Usando @BeforeEach para limpar o banco, garantindo um estado limpo
    @BeforeEach
    void limparBanco() {
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve atualizar categoria com sucesso")
    void deveAtualizarCategoriaComSucesso() {
        // Arrange: Criar a categoria inicial
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Bebidas"));
        
        // Criar o DTO de requisição com os novos dados
        CategoriaRequest requestAtualizada = new CategoriaRequest("Alimentos");

        // Act: Chamar o serviço com o ID e o DTO
        CategoriaResponse categoriaAtualizada = categoriaService.atualizar(categoria.getId(), requestAtualizada);

        // Assert: Validar o DTO de resposta
        assertThat(categoriaAtualizada.nome()).isEqualTo("Alimentos");
        assertThat(categoriaAtualizada.id()).isEqualTo(categoria.getId());
    }

    @Test
    @DisplayName("Não deve atualizar categoria inexistente")
    void naoDeveAtualizarCategoriaInexistente() {
        // Arrange
        CategoriaRequest request = new CategoriaRequest("Alimentos");

        // Act & Assert
        assertThrows(CategoriaNaoEncontradaException.class,
                () -> categoriaService.atualizar(999L, request));
    }

    @Test
    @DisplayName("Não deve atualizar categoria para nome duplicado")
    void naoDeveAtualizarCategoriaDuplicada() {
        // Arrange: Criar as duas categorias iniciais
        Categoria cat1 = categoriaRepository.save(new Categoria(null, "Bebidas"));
        categoriaRepository.save(new Categoria(null, "Alimentos"));

        // Criar o DTO que tenta usar um nome já existente
        CategoriaRequest requestDuplicada = new CategoriaRequest("Alimentos");

        // Act & Assert
        assertThrows(CategoriaJaExistenteException.class,
                () -> categoriaService.atualizar(cat1.getId(), requestDuplicada));
    }
}
