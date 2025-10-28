package br.com.barghesla.barestoque.service.movimentacao;

// DTOs
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueRequest;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueResponse;
import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.model.Perfil;
import br.com.barghesla.barestoque.model.Produto;
import br.com.barghesla.barestoque.model.StatusProduto;
import br.com.barghesla.barestoque.model.TipoMovimentacaoEstoque;
import br.com.barghesla.barestoque.model.Usuario;
// Repositórios
import br.com.barghesla.barestoque.repository.CategoriaRepository; // Importar
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.repository.UsuarioRepository;

// Demais importações
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MovimentacaoListarTodosTest {

    @Autowired
    private MovimentacaoEstoqueServiceImpl movimentacaoService;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CategoriaRepository categoriaRepository; // 1. Injetar o repositório

    private static String rnd() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    
    // --- Helpers Corrigidos ---

    private Categoria novaCategoriaPersistida(String nome) { // 2. Helper para Categoria
        Categoria c = new Categoria();
        c.setNome(nome);
        return categoriaRepository.save(c);
    }

    private Produto novoProdutoPersistido(String nome, int saldo, Categoria categoria) { // 3. Helper de Produto atualizado
        Produto p = new Produto();
        p.setNome(nome);
        p.setDescricao("Gerado em teste");
        p.setQuantidade(saldo);
        p.setPrecoUnitario(new BigDecimal("10.00"));
        p.setStatus(StatusProduto.ATIVO);
        p.setCategoria(categoria); // Associar a categoria persistida
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
    
    private void registrarMovimentacao(TipoMovimentacaoEstoque tipo, int qtd, Produto produto, Usuario usuario) {
        var request = new MovimentacaoEstoqueRequest(null, tipo.name(), qtd, produto.getId(), usuario.getId());
        movimentacaoService.registrarMovimentacao(request);
    }

    // --- Teste Corrigido ---

    @Test
    void deveListarTodosOrdenadoPorDataAsc() throws InterruptedException {
        // 4. Criar a Categoria antes de criar os outros dados
        Categoria cat = novaCategoriaPersistida("Porções");
        Produto p = novoProdutoPersistido("P-" + rnd(), 100, cat);
        Usuario u = novoUsuarioPersistido("Tester-" + rnd(), "tester"+rnd()+"@ex.com");

        // Ação: Registrar movimentações
        registrarMovimentacao(TipoMovimentacaoEstoque.ENTRADA, 10, p, u);
        Thread.sleep(10);
        registrarMovimentacao(TipoMovimentacaoEstoque.SAIDA,   5, p, u);
        Thread.sleep(10);
        registrarMovimentacao(TipoMovimentacaoEstoque.ENTRADA, 7, p, u);

        // Ação Principal
        List<MovimentacaoEstoqueResponse> lista = movimentacaoService.listarTodos();

        // Verificação
        assertThat(lista).hasSize(3);
        assertThat(lista)
                .extracting(MovimentacaoEstoqueResponse::dataMovimentacao)
                .isSorted();
    }
}
