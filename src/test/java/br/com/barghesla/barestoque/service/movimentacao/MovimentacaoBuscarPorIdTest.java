package br.com.barghesla.barestoque.service.movimentacao;

import br.com.barghesla.barestoque.entity.MovimentacaoEstoque;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.entity.TipoMovimentacaoEstoque;
import br.com.barghesla.barestoque.entity.Usuario;
import br.com.barghesla.barestoque.exception.movimentacao.MovimentacaoEstoqueInexistenteException;
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
class MovimentacaoBuscarPorIdTest {

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

    private MovimentacaoEstoque novaMov(TipoMovimentacaoEstoque tipo, int qtd, Produto produto, Usuario usuario) {
        MovimentacaoEstoque m = new MovimentacaoEstoque();
        m.setTipo(tipo);
        m.setQuantidade(qtd);
        m.setProduto(produto);
        m.setUsuarioID(usuario); // manter o mesmo padrão do restante do projeto
        return m;
    }

    @Test
    void deveBuscarPorId_comSucesso() {
        Produto p = novoProdutoPersistido("P-" + rnd(), 10);
        Usuario u = novoUsuarioPersistido("Tester " + rnd(), "tester"+rnd()+"@ex.com");

        MovimentacaoEstoque mov = novaMov(TipoMovimentacaoEstoque.ENTRADA, 4, p, u);
        MovimentacaoEstoque salvo = movimentacaoService.registrarMovimentacao(mov);

        MovimentacaoEstoque encontrado = movimentacaoService.buscarPorId(salvo.getId());

        assertThat(encontrado.getId()).isEqualTo(salvo.getId());
        assertThat(encontrado.getProduto().getId()).isEqualTo(p.getId());
        assertThat(encontrado.getUsuarioID().getId()).isEqualTo(u.getId());
        assertThat(encontrado.getTipo()).isEqualTo(TipoMovimentacaoEstoque.ENTRADA);
        assertThat(encontrado.getQuantidade()).isEqualTo(4);
    }

    @Test
    void deveLancarExcecao_quandoNaoEncontrar() {
        assertThatThrownBy(() -> movimentacaoService.buscarPorId(999999L))
            .isInstanceOf(MovimentacaoEstoqueInexistenteException.class)
            .hasMessageContaining("999999");
    }
}

