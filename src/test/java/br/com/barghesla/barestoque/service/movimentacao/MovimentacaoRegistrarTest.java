package br.com.barghesla.barestoque.service.movimentacao;

// DTOs
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueRequest;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueResponse;
import br.com.barghesla.barestoque.exception.produto.QuantidadeInvalidaException;
import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.model.Perfil;
import br.com.barghesla.barestoque.model.Produto;
import br.com.barghesla.barestoque.model.StatusProduto;
import br.com.barghesla.barestoque.model.Usuario;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.MovimentacaoRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.repository.UsuarioRepository;

// Demais importações
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MovimentacaoRegistrarTest {

    @Autowired
    private MovimentacaoEstoqueServiceImpl movimentacaoService;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CategoriaRepository categoriaRepository; // 1. Injetar CategoriaRepository

    private static String rnd() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // --- Helpers Corrigidos ---

    private Categoria novaCategoriaPersistida(String nome) { // 2. Helper para Categoria
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        return categoriaRepository.save(categoria);
    }

    private Produto novoProdutoPersistido(String nome, int saldo, Categoria categoria) { // 3. Helper de Produto atualizado
        Produto p = new Produto();
        p.setNome(nome);
        p.setDescricao("Gerado em teste");
        p.setQuantidade(saldo);
        p.setPrecoUnitario(new BigDecimal("10.00"));
        p.setStatus(StatusProduto.ATIVO);
        p.setCategoria(categoria); // Associar a categoria
        return produtoRepository.save(p);
    }

    private Usuario novoUsuarioPersistido(String nome, String email) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setPerfil(Perfil.VENDEDOR);
        u.setSenha("12345678");
        return usuarioRepository.save(u);
    }

    // --- Testes Corrigidos ---

    @Test
    void deveRegistrarEntradaSomandoSaldo() {
        // Preparação
        Categoria cat = novaCategoriaPersistida("Bebidas"); // 4. Criar Categoria
        Produto p = novoProdutoPersistido("P-" + rnd(), 10, cat);
        Usuario u = novoUsuarioPersistido("Tester-" + rnd(), "tester" + rnd() + "@ex.com");
        var request = new MovimentacaoEstoqueRequest(null, "ENTRADA", 5, p.getId(), u.getId());

        // Ação
        MovimentacaoEstoqueResponse salvo = movimentacaoService.registrarMovimentacao(request);

        // Verificação
        Produto atualizado = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(atualizado.getQuantidade()).isEqualTo(15);
        assertThat(salvo.usuarioID().id()).isEqualTo(u.getId());
        assertThat(salvo.produto().id()).isEqualTo(p.getId());
    }

    @Test
    void deveRegistrarSaidaSubtraindoSaldo() {
        // Preparação
        Categoria cat = novaCategoriaPersistida("Aperitivos");
        Produto p = novoProdutoPersistido("P-" + rnd(), 12, cat);
        Usuario u = novoUsuarioPersistido("Tester-" + rnd(), "tester" + rnd() + "@ex.com");
        var request = new MovimentacaoEstoqueRequest(null, "SAIDA", 7, p.getId(), u.getId());

        // Ação
        MovimentacaoEstoqueResponse salvo = movimentacaoService.registrarMovimentacao(request);

        // Verificação
        Produto atualizado = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(atualizado.getQuantidade()).isEqualTo(5);
        assertThat(salvo.usuarioID().id()).isEqualTo(u.getId());
    }

    @Test
    void deveFalharSaidaSemSaldoESemPersistir() {
        // Preparação
        Categoria cat = novaCategoriaPersistida("Salgados");
        Produto p = novoProdutoPersistido("P-" + rnd(), 3, cat);
        Usuario u = novoUsuarioPersistido("Tester-" + rnd(), "tester" + rnd() + "@ex.com");
        var request = new MovimentacaoEstoqueRequest(null, "SAIDA", 5, p.getId(), u.getId());

        // Ação e Verificação
        assertThatThrownBy(() -> movimentacaoService.registrarMovimentacao(request))
                .isInstanceOf(QuantidadeInvalidaException.class);

        Produto atualizado = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(atualizado.getQuantidade()).isEqualTo(3);
        assertThat(movimentacaoRepository.count()).isZero();
    }
}
