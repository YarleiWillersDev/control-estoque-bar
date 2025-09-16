package br.com.barghesla.barestoque.service.categoria;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.exception.categoria.CategoriaJaExistenteException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional // garante rollback após cada teste
class CategoriaCriarTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void limparBanco() {
        // Remover primeiro os produtos (pois dependem da categoria)
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar uma nova categoria com sucesso")
    void deveCriarCategoriaComSucesso() {
        Categoria categoria = new Categoria(null, "Bebidas");
        Categoria salva = categoriaService.criar(categoria);

        assertNotNull(salva.getId(), "O ID da categoria deve ser gerado");
    }

    @Test
    @DisplayName("Não deve criar categoria duplicada")
    void naoDeveCriarCategoriaDuplicada() {
        Categoria categoria = new Categoria(null, "Bebidas");
        categoriaService.criar(categoria);

        assertThatThrownBy(() -> categoriaService.criar(new Categoria(null, "Bebidas")))
                .isInstanceOf(CategoriaJaExistenteException.class)
                .hasMessage("Já existe uma categoria com este nome cadastrada na base de dados!");
    }
}
