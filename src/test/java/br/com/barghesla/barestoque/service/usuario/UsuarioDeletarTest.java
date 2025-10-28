package br.com.barghesla.barestoque.service.usuario;

import br.com.barghesla.barestoque.exception.usuario.UsuarioNaoEncontradoException;
import br.com.barghesla.barestoque.model.Perfil;
import br.com.barghesla.barestoque.model.Usuario;
import br.com.barghesla.barestoque.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
        u.setPerfil(Perfil.VENDEDOR);
        u.setSenha(senha);
        return usuarioRepository.save(u);
    }

    @Test
    void deveDeletarUsuarioExistenteComSucesso() {
        // Arrange
        String sufixo = rnd();
        Usuario u = novoUsuarioPersistido("Del-" + sufixo, "del" + sufixo + "@ex.com", "USER", "12345678");
        long idParaDeletar = u.getId();

        // Act & Assert
        // Verifica que o método executa sem lançar uma exceção
        assertDoesNotThrow(() -> usuarioService.deletar(idParaDeletar));

        // Confirma que o usuário não existe mais no banco de dados
        assertThat(usuarioRepository.findById(idParaDeletar)).isEmpty();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        long idInexistente = 999999L;

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.deletar(idInexistente))
                .isInstanceOf(UsuarioNaoEncontradoException.class);
    }
}
