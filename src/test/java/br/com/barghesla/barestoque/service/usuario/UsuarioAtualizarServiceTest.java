package br.com.barghesla.barestoque.service.usuario;

import br.com.barghesla.barestoque.entity.Usuario;
import br.com.barghesla.barestoque.exception.usuario.EmailJaExistenteException;
import br.com.barghesla.barestoque.exception.usuario.UsuarioNaoEncontradoException;
import br.com.barghesla.barestoque.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional // cada teste roda em transação com rollback automático
class UsuarioAtualizarServiceTest {

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
    void deveAtualizarDadosBasicosComSucesso() {
        // Arrange
        String sufixo = rnd();
        Usuario existente = novoUsuarioPersistido("Alice-"+sufixo, "alice"+sufixo+"@ex.com", "ADMIN", "12345678");

        Usuario novo = new Usuario();
        novo.setNome("Alice Silva");
        novo.setEmail("alice"+sufixo+"@ex.com"); // mesmo e-mail para não disparar checagem
        novo.setPerfil("USER");
        novo.setSenha("abcdefgh"); // senha válida

        // Act
        Usuario atualizado = usuarioService.atualizar(existente.getId(), novo);

        // Assert
        assertThat(atualizado.getNome()).isEqualTo("Alice Silva");
        assertThat(atualizado.getEmail()).isEqualTo("alice"+sufixo+"@ex.com");
        assertThat(atualizado.getPerfil()).isEqualTo("USER");
    }

    @Test
    void deveManterEmailQuandoNaoAlterado() {
        // Arrange
        String sufixo = rnd();
        Usuario existente = novoUsuarioPersistido("Bob-"+sufixo, "bob"+sufixo+"@ex.com", "ADMIN", "12345678");

        Usuario novo = new Usuario();
        novo.setNome("Bob Atualizado");
        novo.setEmail(existente.getEmail()); // igual
        novo.setPerfil("ADMIN");
        novo.setSenha("87654321");

        // Act
        Usuario atualizado = usuarioService.atualizar(existente.getId(), novo);

        // Assert
        assertThat(atualizado.getEmail()).isEqualTo(existente.getEmail());
        assertThat(atualizado.getNome()).isEqualTo("Bob Atualizado");
    }

    @Test
    void deveLancarExcecaoQuandoEmailDuplicadoAoAlterar() {
        // Arrange
        String sufixo = rnd();
        Usuario u1 = novoUsuarioPersistido("Carol-"+sufixo, "carol"+sufixo+"@ex.com", "USER", "12345678");
        Usuario u2 = novoUsuarioPersistido("Dave-"+sufixo, "dave"+sufixo+"@ex.com", "USER", "12345678");

        Usuario novo = new Usuario();
        novo.setNome("Dave Novo");
        novo.setEmail(u1.getEmail()); // tenta usar e-mail de outro usuário
        novo.setPerfil("USER");
        novo.setSenha("abcdefgh");

        // Act + Assert
        assertThatThrownBy(() -> usuarioService.atualizar(u2.getId(), novo))
            .isInstanceOf(EmailJaExistenteException.class);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        // Arrange
        Usuario novo = new Usuario();
        novo.setNome("Qualquer");
        novo.setEmail("qualquer"+rnd()+"@ex.com");
        novo.setPerfil("USER");
        novo.setSenha("12345678");

        // Act + Assert
        assertThatThrownBy(() -> usuarioService.atualizar(999999L, novo))
            .isInstanceOf(UsuarioNaoEncontradoException.class);
    }

    @Test
    void devePermitirSenhaNaoInformadaNaAtualizacao() {
        // Arrange
        String sufixo = rnd();
        Usuario existente = novoUsuarioPersistido("Eve-"+sufixo, "eve"+sufixo+"@ex.com", "ADMIN", "12345678");

        Usuario novo = new Usuario();
        novo.setNome("Eve SemSenha");
        novo.setEmail(existente.getEmail());
        novo.setPerfil("ADMIN");
        // senha omitida: comportar-se como “não alterar”

        // Act
        Usuario atualizado = usuarioService.atualizar(existente.getId(), novo);

        // Assert
        assertThat(atualizado.getNome()).isEqualTo("Eve SemSenha");
        assertThat(atualizado.getSenha()).isNotNull(); // permanece com a senha anterior
    }
}

