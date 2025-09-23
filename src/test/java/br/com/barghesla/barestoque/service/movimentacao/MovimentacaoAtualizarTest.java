package br.com.barghesla.barestoque.service.movimentacao;

import br.com.barghesla.barestoque.entity.MovimentacaoEstoque;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.entity.TipoMovimentacaoEstoque;
import br.com.barghesla.barestoque.entity.Usuario;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

    private static String rnd() { return String.valueOf(System.nanoTime()); }

    // Helpers idênticos ao teste de registro

    private Produto novoProdutoPersistido(String nome, int saldo) {
        Produto p = new Produto();
        p.setNome(nome);
        p.setDescricao("Gerado em teste");
        p.setQuantidade(saldo);                      // seu modelo usa 'quantidade'
        p.setPrecoUnitario(new BigDecimal("10.00")); // NOT NULL no Oracle
        p.setStatus(StatusProduto.ATIVO);
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

    private MovimentacaoEstoque novaMov(TipoMovimentacaoEstoque tipo, int qtd, Produto produto, Usuario usuario) {
        MovimentacaoEstoque m = new MovimentacaoEstoque();
        m.setTipo(tipo);
        m.setQuantidade(qtd);
        m.setProduto(produto);
        m.setUsuarioID(usuario); // manter o mesmo padrão do teste de registro
        return m;
    }

    // Atualização: apenas quantidade pode mudar

    @Test
    void deveAtualizarEntradaSomenteQuantidade_somandoDeltaNoSaldo() {
        Produto p = novoProdutoPersistido("P-" + rnd(), 10);
        Usuario u = novoUsuarioPersistido("Tester " + rnd(), "tester"+rnd()+"@ex.com");

        // Registro inicial: ENTRADA 4 => saldo 14
        MovimentacaoEstoque mov = novaMov(TipoMovimentacaoEstoque.ENTRADA, 4, p, u);
        MovimentacaoEstoque salvo = movimentacaoService.registrarMovimentacao(mov);

        // Atualiza apenas a quantidade para 9 (delta +5) => saldo esperado 19
        MovimentacaoEstoque novo = new MovimentacaoEstoque();
        novo.setQuantidade(9);
        // Garantir que nada além da quantidade seja informado
        novo.setTipo(null);
        novo.setProduto(null);
        novo.setUsuarioID(null);
        novo.setDataMovimentacao(null);

        MovimentacaoEstoque atualizado = movimentacaoService.atualizar(salvo.getId(), novo);

        Produto pAtual = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(pAtual.getQuantidade()).isEqualTo(19);
        assertThat(atualizado.getQuantidade()).isEqualTo(9);
        assertThat(atualizado.getTipo()).isEqualTo(TipoMovimentacaoEstoque.ENTRADA);
        assertThat(atualizado.getProduto().getId()).isEqualTo(p.getId());
        assertThat(atualizado.getUsuarioID().getId()).isEqualTo(u.getId());
    }

    @Test
    void deveAtualizarSaidaSomenteQuantidade_validandoSaldo() {
        Produto p = novoProdutoPersistido("P-" + rnd(), 8);
        Usuario u = novoUsuarioPersistido("Tester " + rnd(), "tester"+rnd()+"@ex.com");

        // Registro inicial: SAIDA 3 => saldo 5
        MovimentacaoEstoque mov = novaMov(TipoMovimentacaoEstoque.SAIDA, 3, p, u);
        MovimentacaoEstoque salvo = movimentacaoService.registrarMovimentacao(mov);

        // Atualiza apenas quantidade para 7 (delta +4) => saldo esperado 1
        MovimentacaoEstoque novo = new MovimentacaoEstoque();
        novo.setQuantidade(7);
        novo.setTipo(null);
        novo.setProduto(null);
        novo.setUsuarioID(null);
        novo.setDataMovimentacao(null);

        MovimentacaoEstoque atualizado = movimentacaoService.atualizar(salvo.getId(), novo);

        Produto pAtual = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(pAtual.getQuantidade()).isEqualTo(1);
        assertThat(atualizado.getQuantidade()).isEqualTo(7);
        assertThat(atualizado.getTipo()).isEqualTo(TipoMovimentacaoEstoque.SAIDA);
        assertThat(atualizado.getUsuarioID().getId()).isEqualTo(u.getId());
    }

    @Test
    void deveFalharAoAumentarSaidaAcimaDoSaldo_eNaoAlterarSaldo() {
        Produto p = novoProdutoPersistido("P-" + rnd(), 5);
        Usuario u = novoUsuarioPersistido("Tester " + rnd(), "tester"+rnd()+"@ex.com");

        // Registro inicial: SAIDA 4 => saldo 1
        MovimentacaoEstoque mov = novaMov(TipoMovimentacaoEstoque.SAIDA, 4, p, u);
        MovimentacaoEstoque salvo = movimentacaoService.registrarMovimentacao(mov);

        // Atualização inválida: quantidade 8 (delta +4) => criaria saldo negativo
        MovimentacaoEstoque novo = new MovimentacaoEstoque();
        novo.setQuantidade(8);
        novo.setTipo(null);
        novo.setProduto(null);
        novo.setUsuarioID(null);
        novo.setDataMovimentacao(null);

        assertThatThrownBy(() -> movimentacaoService.atualizar(salvo.getId(), novo))
                .isInstanceOf(RuntimeException.class); // troque pela exceção específica

        Produto pAtual = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(pAtual.getQuantidade()).isEqualTo(1); // sem alteração
    }
}
