package br.com.barghesla.barestoque.service.usuario;

import br.com.barghesla.barestoque.dto.usuario.UsuarioRequest;
import br.com.barghesla.barestoque.dto.usuario.UsuarioResponse;
import br.com.barghesla.barestoque.exception.usuario.EmailJaExistenteException;
import br.com.barghesla.barestoque.exception.usuario.NomeDuplicadoException;
import br.com.barghesla.barestoque.exception.usuario.SenhaInvalidaException;
import br.com.barghesla.barestoque.model.Perfil;
import br.com.barghesla.barestoque.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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
        // Criamos o DTO de requisição com os dados do novo usuário
        UsuarioRequest request = new UsuarioRequest(
                "Yarlei",
                "yarlei@email.com",
                "12345678",
                Perfil.VENDEDOR
        );

        // Act
        // O método 'salvar' agora recebe o request e retorna um response
        UsuarioResponse salvo = usuarioService.salvar(request);

        // Assert
        // Validamos os campos do DTO de resposta
        assertNotNull(salvo.id());
        assertEquals("Yarlei", salvo.nome());
        assertEquals("yarlei@email.com", salvo.email());
        assertEquals("USER", salvo.perfil());
    }

    @Test
    void deveLancarExcecaoQuandoNomeDuplicado() {
        // Arrange
        // Salvamos o primeiro usuário usando o DTO
        UsuarioRequest usuario1 = new UsuarioRequest(
                "Yarlei",
                "yarlei1@email.com",
                "12345678",
                Perfil.VENDEDOR
        );
        usuarioService.salvar(usuario1);

        // Criamos um segundo DTO com o mesmo nome
        UsuarioRequest usuario2 = new UsuarioRequest(
                "Yarlei", // mesmo nome
                "yarlei2@email.com",
                "12345678",
                Perfil.VENDEDOR
        );

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.salvar(usuario2))
                .isInstanceOf(NomeDuplicadoException.class);
    }

    @Test
    void deveLancarExcecaoQuandoEmailDuplicado() {
        // Arrange
        UsuarioRequest usuario1 = new UsuarioRequest(
                "Rafael",
                "rafael@email.com",
                "12345678",
                Perfil.VENDEDOR
        );
        usuarioService.salvar(usuario1);

        UsuarioRequest usuario2 = new UsuarioRequest(
                "Outro Nome",
                "rafael@email.com", // mesmo email
                "12345678",
                Perfil.VENDEDOR
        );

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.salvar(usuario2))
                .isInstanceOf(EmailJaExistenteException.class);
    }

    @Test
    void deveLancarExcecaoQuandoSenhaInvalida() {
        // Arrange
        UsuarioRequest usuario = new UsuarioRequest(
                "João",
                "joao@email.com",
                "123", // senha muito curta
                Perfil.VENDEDOR
        );

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.salvar(usuario))
                .isInstanceOf(SenhaInvalidaException.class);
    }
}
