package br.com.barghesla.barestoque.service.usuario;

import br.com.barghesla.barestoque.entity.Usuario;
import br.com.barghesla.barestoque.exception.usuario.CampoNomeNuloException;
import br.com.barghesla.barestoque.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional // cada teste em transação com rollback
class UsuarioBuscarPorNomeTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static String rnd() { return String.valueOf(System.nanoTime()); }

    private Usuario novo(String nome) {
        String s = rnd();
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail("u"+s+"@ex.com");
        u.setPerfil("USER");
        u.setSenha("12345678");
        return usuarioRepository.save(u);
    }

    @Test
    void deveRetornarMultiposResultadosPorTrechoCaseInsensitive() {
        novo("Maria Clara");
        novo("Mariana");
        novo("João");

        var lista = usuarioService.buscarPorNome("  mArI  "); // espaços e case misto

        assertThat(lista).extracting(Usuario::getNome)
                .contains("Maria Clara", "Mariana");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverCorrespondencias() {
        novo("João");
        var lista = usuarioService.buscarPorNome("zzz"+rnd());
        assertThat(lista).isEmpty();
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        assertThatThrownBy(() -> usuarioService.buscarPorNome(null))
                .isInstanceOf(CampoNomeNuloException.class);
    }

    @Test
    void deveLancarExcecaoQuandoNomeForApenasEspacos() {
        assertThatThrownBy(() -> usuarioService.buscarPorNome("   "))
                .isInstanceOf(CampoNomeNuloException.class);
    }
}

