package br.com.barghesla.barestoque.service.usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import br.com.barghesla.barestoque.entity.Usuario;
import br.com.barghesla.barestoque.repository.UsuarioRepository;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UsuarioListarTodosTest {

    @Autowired UsuarioService usuarioService;
    @Autowired UsuarioRepository usuarioRepository;

    private static String rnd(){ return String.valueOf(System.nanoTime()); }

    private Usuario novo(String nome){
        String s = rnd();
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail("u"+s+"@ex.com");
        u.setPerfil("USER");
        u.setSenha("12345678");
        return usuarioRepository.save(u);
    }

    @Test
    void deveListarOrdenadoPorNomeAsc() {
        novo("Carlos");
        novo("Ana");
        novo("Bruno");

        var lista = usuarioService.listarTodos();

        assertThat(lista).extracting(Usuario::getNome)
              .containsSubsequence("Ana", "Bruno", "Carlos");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverUsuarios() {
        // sem inserts: banco limpo por rollback de outros testes
        var lista = usuarioService.listarTodos();
        assertThat(lista).isNotNull().isEmpty();
    }
}



