package br.com.barghesla.barestoque.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.model.Produto;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.MovimentacaoRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UsuarioRepository usuarioRepository;

    @Autowired
    protected MovimentacaoRepository movimentacaoRepository;

    @Autowired
    protected ProdutoRepository produtoRepository;

    @Autowired
    protected CategoriaRepository categoriaRepository;

    @AfterEach
    void tearDown() {
        usuarioRepository.deleteAll();
        movimentacaoRepository.deleteAll();
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    protected Categoria criarCategoriaParaTeste() {
        Categoria categoria = new Categoria();
        categoria.setNome("Bebidas");
        return categoriaRepository.save(categoria);
    }

    protected Produto criarProdutoParaTeste() {
        Produto produto = new Produto();
        produto.setNome("Cerveja Pilsen");
        produto.setDescricao("Schin 600ml");
        produto.setQuantidade(100);
        produto.setPrecoUnitario(new BigDecimal("8.59"));
        produto.setCategoria(criarCategoriaParaTeste());
        return produtoRepository.save(produto);
    }

}
