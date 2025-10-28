package br.com.barghesla.barestoque.service.usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import br.com.barghesla.barestoque.dto.usuario.UsuarioResponse;
import br.com.barghesla.barestoque.model.Perfil;
import br.com.barghesla.barestoque.model.Usuario;
import br.com.barghesla.barestoque.repository.UsuarioRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UsuarioListarTodosTest {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    private static String rnd() { return String.valueOf(System.nanoTime()); }

    private Usuario novo(String nome) {
        String s = rnd();
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail("u" + s + "@ex.com");
        u.setPerfil(Perfil.VENDEDOR);;
        u.setSenha("12345678");
        return usuarioRepository.save(u);
    }

    @Test
    void deveListarOrdenadoPorNomeAsc() {
        // Arrange
        novo("Carlos");
        novo("Ana");
        novo("Bruno");

        // Act
        // A variável 'lista' agora é uma List<UsuarioResponse>
        List<UsuarioResponse> lista = usuarioService.listarTodos();

        // Assert
        // A asserção extrai o campo 'nome' do DTO de resposta
        assertThat(lista).extracting(UsuarioResponse::nome)
                .containsExactly("Ana", "Bruno", "Carlos");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverUsuarios() {
        // Arrange (nenhum usuário criado)
        
        // Act
        List<UsuarioResponse> lista = usuarioService.listarTodos();
        
        // Assert
        assertThat(lista).isNotNull().isEmpty();
    }
}
