package br.com.barghesla.barestoque.service.usuario;

import br.com.barghesla.barestoque.dto.usuario.UsuarioResponse;
import br.com.barghesla.barestoque.entity.Usuario;
import br.com.barghesla.barestoque.exception.usuario.CampoNomeNuloException;
import br.com.barghesla.barestoque.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // Método auxiliar para popular o banco para os testes
    private Usuario novo(String nome) {
        String s = rnd();
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail("u" + s + "@ex.com");
        u.setPerfil("USER");
        u.setSenha("12345678");
        return usuarioRepository.save(u);
    }

    @Test
    void deveRetornarMultiposResultadosPorTrechoCaseInsensitive() {
        // Arrange
        novo("Maria Clara");
        novo("Mariana");
        novo("João");

        // Act
        // O método agora retorna uma lista de UsuarioResponse
        List<UsuarioResponse> lista = usuarioService.buscarPorNome("   mArI  "); // espaços e case misto

        // Assert
        // A asserção agora extrai o nome do DTO de resposta
        assertThat(lista).extracting(UsuarioResponse::nome)
                .containsExactlyInAnyOrder("Maria Clara", "Mariana");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverCorrespondencias() {
        // Arrange
        novo("João");

        // Act
        List<UsuarioResponse> lista = usuarioService.buscarPorNome("zzz" + rnd());

        // Assert
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
