package br.com.barghesla.barestoque.service.usuario;

import br.com.barghesla.barestoque.entity.Usuario;
import br.com.barghesla.barestoque.exception.usuario.UsuarioNaoEncontradoException;
import br.com.barghesla.barestoque.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional // rollback após cada teste
class UsuarioDeletarTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static String rnd() { return String.valueOf(System.nanoTime()); }

    private Usuario novoUsuarioPersistido(String nome, String email, String perfil, String senha) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setPerfil(perfil);
        u.setSenha(senha);
        return usuarioRepository.save(u);
    }

    @Test
    void deveDeletarUsuarioExistenteERetornarTrue() {
        // Arrange
        String sufixo = rnd();
        Usuario u = novoUsuarioPersistido("Del-"+sufixo, "del"+sufixo+"@ex.com", "USER", "12345678");

        // Act
        boolean resultado = usuarioService.deletar(u.getId());

        // Assert
        assertThat(resultado).isTrue();
        assertThat(usuarioRepository.findById(u.getId())).isEmpty();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Act + Assert
        assertThatThrownBy(() -> usuarioService.deletar(999999L))
            .isInstanceOf(UsuarioNaoEncontradoException.class);
    }
}

