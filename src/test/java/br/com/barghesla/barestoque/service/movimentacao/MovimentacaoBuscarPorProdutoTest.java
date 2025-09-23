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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MovimentacaoBuscarPorProdutoTest {

    @Autowired
    private MovimentacaoEstoqueServiceImpl movimentacaoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static String rnd() { return String.valueOf(System.nanoTime()); }

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
        u.setPerfil("USER");
        u.setSenha("12345678");
        return usuarioRepository.save(u);
    }

    private MovimentacaoEstoque movComData(TipoMovimentacaoEstoque tipo, int qtd, Produto p, Usuario u, LocalDateTime data) {
        MovimentacaoEstoque m = new MovimentacaoEstoque();
        m.setTipo(tipo);
        m.setQuantidade(qtd);
        m.setProduto(p);
        m.setUsuarioID(u);
        m.setDataMovimentacao(data);
        return m;
    }

    @Test
    void deveRetornarApenasDoProdutoEmOrdemDesc() {
        // Arrange
        Produto p1 = novoProdutoPersistido("P1-" + rnd(), 100);
        Produto p2 = novoProdutoPersistido("P2-" + rnd(), 200);
        Usuario u = novoUsuarioPersistido("Tester " + rnd(), "tester"+rnd()+"@ex.com");

        LocalDateTime t1 = LocalDateTime.now().minusDays(3);
        LocalDateTime t2 = LocalDateTime.now().minusDays(1);
        LocalDateTime t3 = LocalDateTime.now();

        // Para p1: 3 movimentações com datas distintas
        movimentacaoService.registrarMovimentacao(movComData(TipoMovimentacaoEstoque.ENTRADA, 10, p1, u, t1));
        movimentacaoService.registrarMovimentacao(movComData(TipoMovimentacaoEstoque.SAIDA,   5,  p1, u, t2));
        movimentacaoService.registrarMovimentacao(movComData(TipoMovimentacaoEstoque.ENTRADA, 7,  p1, u, t3));

        // Para p2: 1 movimentação para garantir filtragem
        movimentacaoService.registrarMovimentacao(movComData(TipoMovimentacaoEstoque.ENTRADA, 20, p2, u, t2));

        // Act
        List<MovimentacaoEstoque> lista = movimentacaoService.buscarPorProduto(p1.getId());

        // Assert: apenas do p1
        assertThat(lista).isNotEmpty();
        assertThat(lista).allMatch(m -> m.getProduto().getId().equals(p1.getId()));

        // Assert: ordem decrescente por data
        assertThat(lista)
            .extracting(MovimentacaoEstoque::getDataMovimentacao)
            .isSortedAccordingTo(java.util.Comparator.reverseOrder());

        // Datas esperadas: t3, t2, t1
        assertThat(lista.get(0).getDataMovimentacao()).isEqualTo(t3);
        assertThat(lista.get(1).getDataMovimentacao()).isEqualTo(t2);
        assertThat(lista.get(2).getDataMovimentacao()).isEqualTo(t1);
    }
}

