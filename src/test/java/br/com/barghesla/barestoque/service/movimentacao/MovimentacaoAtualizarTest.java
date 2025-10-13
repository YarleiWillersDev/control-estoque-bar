package br.com.barghesla.barestoque.service.movimentacao;

// DTOs
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueRequest;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueResponse;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueUpdateQuantidadeRequest;

// Entidades
import br.com.barghesla.barestoque.entity.Categoria; // Importar Categoria
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.entity.TipoMovimentacaoEstoque;
import br.com.barghesla.barestoque.entity.Usuario;

// Exceções
import br.com.barghesla.barestoque.exception.produto.QuantidadeInvalidaException;

// Repositórios
import br.com.barghesla.barestoque.repository.CategoriaRepository; // Importar CategoriaRepository
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
class MovimentacaoAtualizarTest {

    @Autowired
    private MovimentacaoEstoqueServiceImpl movimentacaoService;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CategoriaRepository categoriaRepository; // <-- 1. Injetar o repositório de Categoria

    private static String rnd() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // --- Helpers Corrigidos ---

    /**
     * 2. Helper para criar e persistir uma Categoria.
     */
    private Categoria novaCategoriaPersistida(String nome) {
        Categoria c = new Categoria();
        c.setNome(nome);
        return categoriaRepository.save(c);
    }

    /**
     * 3. Helper de Produto agora recebe e associa a Categoria.
     */
    private Produto novoProdutoPersistido(String nome, int saldo, Categoria categoria) {
        Produto p = new Produto();
        p.setNome(nome);
        p.setDescricao("Gerado em teste");
        p.setQuantidade(saldo);
        p.setPrecoUnitario(new BigDecimal("10.00"));
        p.setStatus(StatusProduto.ATIVO);
        p.setCategoria(categoria); // <-- A correção crucial
        return produtoRepository.save(p);
    }

    private Usuario novoUsuarioPersistido(String nome, String email) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setPerfil("USER");
        u.setSenha("12345678");
        return usuarioRepository.save(u);
    }
    
    private MovimentacaoEstoqueResponse registrarMovimentacao(TipoMovimentacaoEstoque tipo, int qtd, Produto produto, Usuario usuario) {
        var request = new MovimentacaoEstoqueRequest(null, tipo.name(), qtd, produto.getId(), usuario.getId());
        return movimentacaoService.registrarMovimentacao(request);
    }

    // --- Testes Corrigidos ---

    @Test
    void deveAtualizarEntradaSomenteQuantidade_somandoDeltaNoSaldo() {
        // 4. Criar a Categoria antes de criar os outros dados
        Categoria cat = novaCategoriaPersistida("Bebidas");
        Produto p = novoProdutoPersistido("P-" + rnd(), 10, cat);
        Usuario u = novoUsuarioPersistido("Tester-" + rnd(), "tester"+rnd()+"@ex.com");

        MovimentacaoEstoqueResponse salvo = registrarMovimentacao(TipoMovimentacaoEstoque.ENTRADA, 4, p, u);

        var request = new MovimentacaoEstoqueUpdateQuantidadeRequest(9);
        MovimentacaoEstoqueResponse atualizado = movimentacaoService.atualizar(salvo.id(), request);

        Produto pAtual = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(pAtual.getQuantidade()).isEqualTo(19);
        assertThat(atualizado.quantidade()).isEqualTo(9);
    }

    @Test
    void deveAtualizarSaidaSomenteQuantidade_validandoSaldo() {
        Categoria cat = novaCategoriaPersistida("Aperitivos");
        Produto p = novoProdutoPersistido("P-" + rnd(), 8, cat);
        Usuario u = novoUsuarioPersistido("Tester-" + rnd(), "tester"+rnd()+"@ex.com");

        MovimentacaoEstoqueResponse salvo = registrarMovimentacao(TipoMovimentacaoEstoque.SAIDA, 3, p, u);

        var request = new MovimentacaoEstoqueUpdateQuantidadeRequest(7);
        MovimentacaoEstoqueResponse atualizado = movimentacaoService.atualizar(salvo.id(), request);

        Produto pAtual = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(pAtual.getQuantidade()).isEqualTo(1);
        assertThat(atualizado.quantidade()).isEqualTo(7);
    }

    @Test
    void deveFalharAoAumentarSaidaAcimaDoSaldo_eNaoAlterarSaldo() {
        Categoria cat = novaCategoriaPersistida("Congelados");
        Produto p = novoProdutoPersistido("P-" + rnd(), 5, cat);
        Usuario u = novoUsuarioPersistido("Tester-" + rnd(), "tester"+rnd()+"@ex.com");

        MovimentacaoEstoqueResponse salvo = registrarMovimentacao(TipoMovimentacaoEstoque.SAIDA, 4, p, u);

        var request = new MovimentacaoEstoqueUpdateQuantidadeRequest(8);
        
        assertThatThrownBy(() -> movimentacaoService.atualizar(salvo.id(), request))
                .isInstanceOf(QuantidadeInvalidaException.class);

        Produto pAtual = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(pAtual.getQuantidade()).as("O saldo do produto não deve ser alterado em caso de falha").isEqualTo(1);
    }
}
