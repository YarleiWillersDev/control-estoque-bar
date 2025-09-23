package br.com.barghesla.barestoque.service.usuario;

import java.util.List;
import org.springframework.stereotype.Service;
import br.com.barghesla.barestoque.entity.Usuario;

@Service
public interface UsuarioService {
    Usuario salvar(Usuario usuario);
    Usuario atualizar(Long id, Usuario usuario);
    boolean deletar(Long id);
    List<Usuario> buscarPorNome(String nome);
    List<Usuario> listarTodos();
}
