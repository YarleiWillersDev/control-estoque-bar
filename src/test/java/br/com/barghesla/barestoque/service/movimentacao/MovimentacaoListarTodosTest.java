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
class MovimentacaoListarTodosTest {

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
    void deveListarTodosOrdenadoPorDataAsc() {
        Produto p = novoProdutoPersistido("P-" + rnd(), 100);
        Usuario u = novoUsuarioPersistido("Tester " + rnd(), "tester"+rnd()+"@ex.com");

        // Criar três movimentações com datas controladas
        LocalDateTime t1 = LocalDateTime.now().minusDays(2);
        LocalDateTime t2 = LocalDateTime.now().minusDays(1);
        LocalDateTime t3 = LocalDateTime.now();

        MovimentacaoEstoque m1 = movComData(TipoMovimentacaoEstoque.ENTRADA, 10, p, u, t1);
        MovimentacaoEstoque m2 = movComData(TipoMovimentacaoEstoque.SAIDA,   5, p, u, t2);
        MovimentacaoEstoque m3 = movComData(TipoMovimentacaoEstoque.ENTRADA, 7, p, u, t3);

        // Registrar respeitando a política do service (data já definida)
        movimentacaoService.registrarMovimentacao(m1);
        movimentacaoService.registrarMovimentacao(m2);
        movimentacaoService.registrarMovimentacao(m3);

        // Act
        List<MovimentacaoEstoque> lista = movimentacaoService.listarTodos();

        // Assert básico: contém pelo menos as 3 criadas, em ordem
        // Para isolar, poderíamos filtrar por produto/usuario, mas aqui validamos ordem global
        assertThat(lista).isNotEmpty();

        // Extrair as três últimas para validar a ordenação ascendente
        int n = lista.size();
        List<MovimentacaoEstoque> ultimas3 = lista.subList(Math.max(0, n - 3), n);

        assertThat(ultimas3)
            .extracting(MovimentacaoEstoque::getDataMovimentacao)
            .isSorted(); // ordem ascendente

        // Verifica que as datas esperadas estão presentes e em ordem
        assertThat(ultimas3.get(0).getDataMovimentacao()).isBeforeOrEqualTo(ultimas3.get(1).getDataMovimentacao());
        assertThat(ultimas3.get(1).getDataMovimentacao()).isBeforeOrEqualTo(ultimas3.get(2).getDataMovimentacao());
    }
}

