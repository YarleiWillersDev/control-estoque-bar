package br.com.barghesla.barestoque.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.model.MovimentacaoEstoque;
import br.com.barghesla.barestoque.model.Perfil;
import br.com.barghesla.barestoque.model.Produto;
import br.com.barghesla.barestoque.model.TipoMovimentacaoEstoque;
import br.com.barghesla.barestoque.model.Usuario;
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
        movimentacaoRepository.deleteAll();
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
        usuarioRepository.deleteAll();
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

    protected Produto criaProdutoParaTeste(Categoria categoria) {
        Produto produto = new Produto();
        produto.setNome("Cerveja Pilsen");
        produto.setDescricao("Schin 600ml");
        produto.setQuantidade(100);
        produto.setPrecoUnitario(new BigDecimal("8.59"));
        produto.setCategoria(categoria);
        return produtoRepository.save(produto);
    }

    protected Usuario criarUsuarioGerenteParaTeste() {
        Usuario usuario = new Usuario();
        usuario.setNome("Yarlei");
        usuario.setEmail("yarlei@email");
        usuario.setSenha("Antena2000ACG");
        usuario.setPerfil(Perfil.GERENTE);
        return usuarioRepository.save(usuario);
    }

    protected Usuario criarUsuarioVendedorParaTeste() {
        Usuario usuario = new Usuario();
        usuario.setNome("Gabriel");
        usuario.setEmail("gabriel@email");
        usuario.setSenha("Antena2000ACG");
        usuario.setPerfil(Perfil.VENDEDOR);
        return usuarioRepository.save(usuario);
    }

    protected MovimentacaoEstoque criarMovimentacaoEstoqueTeste(Produto produto, Usuario usuario) {
        MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();
        movimentacaoEstoque.setTipo(TipoMovimentacaoEstoque.ENTRADA);
        movimentacaoEstoque.setQuantidade(50);
        movimentacaoEstoque.setDataMovimentacao(LocalDateTime.now());
        movimentacaoEstoque.setProduto(produto);
        movimentacaoEstoque.setUsuarioID(usuario);
        return movimentacaoRepository.save(movimentacaoEstoque);
    }

}
