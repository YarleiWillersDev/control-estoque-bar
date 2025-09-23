package br.com.barghesla.barestoque.service.movimentacao;

import br.com.barghesla.barestoque.entity.MovimentacaoEstoque;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.entity.TipoMovimentacaoEstoque;
import br.com.barghesla.barestoque.entity.Usuario;
import br.com.barghesla.barestoque.repository.MovimentacaoRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

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

    private static String rnd() {
        return String.valueOf(System.nanoTime());
    }

    private Produto novoProdutoPersistido(String nome, int saldo) {
        Produto p = new Produto();
        p.setNome(nome);
        p.setDescricao("Gerado em teste");
        p.setQuantidade(saldo);
        p.setPrecoUnitario(new BigDecimal("10.00"));
        p.setStatus(StatusProduto.ATIVO);
        return produtoRepository.save(p);
    }

    private Usuario novoUsuarioPersistido(String nome, String email) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setPerfil("USER"); // ajuste conforme seu modelo
        u.setSenha("12345678"); // se houver validação
        return usuarioRepository.save(u);
    }

    private MovimentacaoEstoque novaMov(TipoMovimentacaoEstoque tipo, int qtd, Produto produto) {
        MovimentacaoEstoque m = new MovimentacaoEstoque();
        m.setTipo(tipo);
        m.setQuantidade(qtd);
        m.setProduto(produto);
        return m;
    }

    @Test
    void deveRegistrarEntradaSomandoSaldo() {
        Produto p = novoProdutoPersistido("P-" + rnd(), 10);
        Usuario u = novoUsuarioPersistido("Tester " + rnd(), "tester" + rnd() + "@ex.com");

        MovimentacaoEstoque mov = novaMov(TipoMovimentacaoEstoque.ENTRADA, 5, p);
        mov.setUsuarioID(u); // ASSOCIAR O USUÁRIO

        MovimentacaoEstoque salvo = movimentacaoService.registrarMovimentacao(mov);

        Produto atualizado = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(atualizado.getQuantidade()).isEqualTo(15);
        assertThat(salvo.getUsuarioID().getId()).isEqualTo(u.getId());
    }

    @Test
    void deveRegistrarSaidaSubtraindoSaldo() {
        Produto p = novoProdutoPersistido("P-" + rnd(), 12);
        Usuario u = novoUsuarioPersistido("Tester " + rnd(), "tester" + rnd() + "@ex.com");

        MovimentacaoEstoque mov = novaMov(TipoMovimentacaoEstoque.SAIDA, 7, p);
        mov.setUsuarioID(u); // associar usuário persistido

        MovimentacaoEstoque salvo = movimentacaoService.registrarMovimentacao(mov);

        Produto atualizado = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(atualizado.getQuantidade()).isEqualTo(5);
        assertThat(salvo.getUsuarioID().getId()).isEqualTo(u.getId());
    }

    @Test
    void deveFalharSaidaSemSaldoESemPersistir() {
        Produto p = novoProdutoPersistido("P-" + rnd(), 3);
        MovimentacaoEstoque mov = novaMov(TipoMovimentacaoEstoque.SAIDA, 5, p);

        assertThatThrownBy(() -> movimentacaoService.registrarMovimentacao(mov))
                .isInstanceOf(RuntimeException.class); // troque pela exceção de domínio se houver

        Produto atualizado = produtoRepository.findById(p.getId()).orElseThrow();
        assertThat(atualizado.getQuantidade()).isEqualTo(3);
        assertThat(movimentacaoRepository.findAll()).isEmpty();
    }
}
