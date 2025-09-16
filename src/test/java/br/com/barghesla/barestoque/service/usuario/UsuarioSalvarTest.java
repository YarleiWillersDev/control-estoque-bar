package br.com.barghesla.barestoque.service.usuario;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.barghesla.barestoque.entity.Usuario;
import br.com.barghesla.barestoque.exception.usuario.EmailJaExistenteException;
import br.com.barghesla.barestoque.exception.usuario.NomeDuplicadoException;
import br.com.barghesla.barestoque.exception.usuario.SenhaInvalidaException;
import br.com.barghesla.barestoque.repository.UsuarioRepository;

@SpringBootTest
@Transactional
class UsuarioSalvarTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparBanco() {
        usuarioRepository.deleteAll();
    }

    @Test
    void deveSalvarUsuarioQuandoValido() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome("Yarlei");
        usuario.setEmail("yarlei@email.com");
        usuario.setSenha("12345678");

        // Act
        Usuario salvo = usuarioService.salvar(usuario);

        // Assert
        assertNotNull(salvo.getId());
        assertEquals("Yarlei", salvo.getNome());
        assertEquals("yarlei@email.com", salvo.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoNomeDuplicado() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Yarlei");
        usuario1.setEmail("yarlei1@email.com");
        usuario1.setSenha("12345678");
        usuarioService.salvar(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Yarlei"); // mesmo nome
        usuario2.setEmail("yarlei2@email.com");
        usuario2.setSenha("12345678");

        // Act & Assert
        assertThrows(NomeDuplicadoException.class, () -> usuarioService.salvar(usuario2));
    }

    @Test
    void deveLancarExcecaoQuandoEmailDuplicado() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Rafael");
        usuario1.setEmail("rafael@email.com");
        usuario1.setSenha("12345678");
        usuarioService.salvar(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Outro Nome");
        usuario2.setEmail("rafael@email.com"); // mesmo email
        usuario2.setSenha("12345678");

        // Act & Assert
        assertThrows(EmailJaExistenteException.class, () -> usuarioService.salvar(usuario2));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaInvalida() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("123"); // senha muito curta

        // Act & Assert
        assertThrows(SenhaInvalidaException.class, () -> usuarioService.salvar(usuario));
    }
}

