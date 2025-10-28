package br.com.barghesla.barestoque.service.movimentacao;

// DTOs
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueRequest;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueResponse;
import br.com.barghesla.barestoque.exception.movimentacao.MovimentacaoEstoqueInexistenteException;
import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.model.Perfil;
import br.com.barghesla.barestoque.model.Produto;
import br.com.barghesla.barestoque.model.StatusProduto;
import br.com.barghesla.barestoque.model.TipoMovimentacaoEstoque;
import br.com.barghesla.barestoque.model.Usuario;
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
class MovimentacaoBuscarPorIdTest {

    @Autowired
    private MovimentacaoEstoqueServiceImpl movimentacaoService;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private static String rnd() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // --- Helpers ---

    private Categoria categoria = new Categoria();

    private Produto novoProdutoPersistido(String nome, int saldo) {
        Produto p = new Produto();
        p.setNome(nome);
        p.setDescricao("Gerado em teste");
        p.setQuantidade(saldo);
        p.setPrecoUnitario(new BigDecimal("10.00"));
        p.setCategoria(categoria);
        p.setStatus(StatusProduto.ATIVO);
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
    
    /**
     * Helper que simula o registro de uma movimentação via API, usando DTOs.
     */
    private MovimentacaoEstoqueResponse registrarMovimentacao(TipoMovimentacaoEstoque tipo, int qtd, Produto produto, Usuario usuario) {
        var request = new MovimentacaoEstoqueRequest(null, tipo.name(), qtd, produto.getId(), usuario.getId());
        return movimentacaoService.registrarMovimentacao(request);
    }

    // --- Testes Refatorados ---

    @Test
    void deveBuscarPorId_comSucesso() {
        // Preparação
        Produto p = novoProdutoPersistido("P-" + rnd(), 10);
        Usuario u = novoUsuarioPersistido("Tester-" + rnd(), "tester"+rnd()+"@ex.com");
        MovimentacaoEstoqueResponse salvo = registrarMovimentacao(TipoMovimentacaoEstoque.ENTRADA, 4, p, u);

        // Ação
        MovimentacaoEstoqueResponse encontrado = movimentacaoService.buscarPorId(salvo.id());

        // Verificação - Assertions nos campos do DTO de resposta
        assertThat(encontrado.id()).isEqualTo(salvo.id());
        assertThat(encontrado.produto().id()).isEqualTo(p.getId());
        assertThat(encontrado.usuarioID().id()).isEqualTo(u.getId());
        assertThat(encontrado.tipo()).isEqualTo(TipoMovimentacaoEstoque.ENTRADA);
        assertThat(encontrado.quantidade()).isEqualTo(4);
        assertThat(encontrado.dataMovimentacao()).isNotNull();
    }

    @Test
    void deveLancarExcecao_quandoNaoEncontrar() {
        // Ação e Verificação
        long idInexistente = 999999L;
        assertThatThrownBy(() -> movimentacaoService.buscarPorId(idInexistente))
                .isInstanceOf(MovimentacaoEstoqueInexistenteException.class)
                .hasMessageContaining(String.valueOf(idInexistente));
    }
}
