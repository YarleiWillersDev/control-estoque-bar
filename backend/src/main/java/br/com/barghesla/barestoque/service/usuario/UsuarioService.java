package br.com.barghesla.barestoque.service.usuario;

import java.util.List;
import org.springframework.stereotype.Service;

import br.com.barghesla.barestoque.dto.usuario.UsuarioRequest;
import br.com.barghesla.barestoque.dto.usuario.UsuarioResponse;

@Service
public interface UsuarioService {
    UsuarioResponse salvar(UsuarioRequest request);
    UsuarioResponse atualizar(Long id, UsuarioRequest request);
    void deletar(Long id);
    List<UsuarioResponse> buscarPorNome(String nome);
    List<UsuarioResponse> listarTodos();
}
