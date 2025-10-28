package br.com.barghesla.barestoque.service.usuario;

import br.com.barghesla.barestoque.dto.usuario.UsuarioRequest;
import br.com.barghesla.barestoque.dto.usuario.UsuarioResponse;
import br.com.barghesla.barestoque.exception.usuario.EmailJaExistenteException;
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

@SpringBootTest
@Transactional // cada teste roda em transação com rollback automático
class UsuarioAtualizarServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static String rnd() { return String.valueOf(System.nanoTime()); }

    // Este método auxiliar continua útil para criar o estado inicial no banco
    private Usuario novoUsuarioPersistido(String nome, String email, String perfil, String senha) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setPerfil(Perfil.VENDEDOR);
        u.setSenha(senha); // A senha será 'hasheada' pelo listener da entidade, se houver
        return usuarioRepository.save(u);
    }

    @Test
    void deveAtualizarDadosBasicosComSucesso() {
        // Arrange
        String sufixo = rnd();
        Usuario existente = novoUsuarioPersistido("Alice-" + sufixo, "alice" + sufixo + "@ex.com", "ADMIN", "12345678");

        // O objeto com os novos dados agora é um UsuarioRequest
        UsuarioRequest request = new UsuarioRequest(
                "Alice Silva",
                "alice" + sufixo + "@ex.com", // mesmo e-mail
                "abcdefgh",
                Perfil.VENDEDOR
        );

        // Act
        // O método agora retorna um UsuarioResponse
        UsuarioResponse atualizado = usuarioService.atualizar(existente.getId(), request);

        // Assert
        assertThat(atualizado.nome()).isEqualTo("Alice Silva");
        assertThat(atualizado.email()).isEqualTo("alice" + sufixo + "@ex.com");
        assertThat(atualizado.perfil()).isEqualTo("USER");
        assertThat(atualizado.id()).isEqualTo(existente.getId());
    }

    @Test
    void deveManterEmailQuandoNaoAlterado() {
        // Arrange
        String sufixo = rnd();
        Usuario existente = novoUsuarioPersistido("Bob-" + sufixo, "bob" + sufixo + "@ex.com", "ADMIN", "12345678");

        UsuarioRequest request = new UsuarioRequest(
                "Bob Atualizado",
                existente.getEmail(), // igual
                "87654321",
                Perfil.GERENTE
        );

        // Act
        UsuarioResponse atualizado = usuarioService.atualizar(existente.getId(), request);

        // Assert
        assertThat(atualizado.email()).isEqualTo(existente.getEmail());
        assertThat(atualizado.nome()).isEqualTo("Bob Atualizado");
    }

    @Test
    void deveLancarExcecaoQuandoEmailDuplicadoAoAlterar() {
        // Arrange
        String sufixo = rnd();
        Usuario u1 = novoUsuarioPersistido("Carol-" + sufixo, "carol" + sufixo + "@ex.com", "USER", "12345678");
        Usuario u2 = novoUsuarioPersistido("Dave-" + sufixo, "dave" + sufixo + "@ex.com", "USER", "12345678");

        UsuarioRequest request = new UsuarioRequest(
                "Dave Novo",
                u1.getEmail(), // tenta usar e-mail de outro usuário
                "abcdefgh",
                Perfil.VENDEDOR
        );

        // Act + Assert
        assertThatThrownBy(() -> usuarioService.atualizar(u2.getId(), request))
                .isInstanceOf(EmailJaExistenteException.class);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        // Arrange
        UsuarioRequest request = new UsuarioRequest(
                "Qualquer",
                "qualquer" + rnd() + "@ex.com",
                "12345678",
                Perfil.VENDEDOR
        );
        long idInexistente = 999999L;

        // Act + Assert
        assertThatThrownBy(() -> usuarioService.atualizar(idInexistente, request))
                .isInstanceOf(UsuarioNaoEncontradoException.class);
    }

    @Test
    void devePermitirSenhaNaoInformadaNaAtualizacao() {
        // Arrange
        String sufixo = rnd();
        Usuario existente = novoUsuarioPersistido("Eve-" + sufixo, "eve" + sufixo + "@ex.com", "ADMIN", "senha_antiga_123");

        UsuarioRequest request = new UsuarioRequest(
                "Eve SemSenha",
                existente.getEmail(),
                null, // Senha omitida (nula) na requisição
                Perfil.VENDEDOR
        );

        // Act
        UsuarioResponse atualizado = usuarioService.atualizar(existente.getId(), request);

        // Assert
        assertThat(atualizado.nome()).isEqualTo("Eve SemSenha");
        assertThat(atualizado.perfil()).isEqualTo("USER");
        // Não é possível verificar a senha pelo DTO, pois ele corretamente não a expõe.
        // O teste garante que a operação é concluída com sucesso, confiando
        // que a lógica do Updater (que deve ser testada em sua própria classe)
        // ignora a senha nula.
    }
}
