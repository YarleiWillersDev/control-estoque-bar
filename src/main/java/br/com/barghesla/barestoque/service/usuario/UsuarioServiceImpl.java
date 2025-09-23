package br.com.barghesla.barestoque.service.usuario;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.barghesla.barestoque.entity.Usuario;
import br.com.barghesla.barestoque.exception.usuario.EmailJaExistenteException;
import br.com.barghesla.barestoque.exception.usuario.UsuarioNaoEncontradoException;
import br.com.barghesla.barestoque.repository.UsuarioRepository;
import br.com.barghesla.barestoque.updater.usuario.UsuarioUpdater;
import br.com.barghesla.barestoque.validation.usuario.UsuarioValidator;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;
    private final UsuarioUpdater usuarioUpdater;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioValidator usuarioValidator, UsuarioUpdater usuarioUpdater) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioValidator = usuarioValidator;
        this.usuarioUpdater = usuarioUpdater;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        usuarioValidator.validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario atualizar(Long id, Usuario novo) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(
                        "Não existe usuário cadastrado para este ID na base de dados."));

        usuarioValidator.validarAtualizacao(existente, novo);

        if (!equalsIgnoreCase(existente.getEmail(), novo.getEmail())
                && usuarioRepository.existsByEmailAndIdNot(novo.getEmail(), id)) {
            throw new EmailJaExistenteException("Já existe um usuário com este email cadastrado na base de dados.");
        }

        String senhaParaPersistir = novo.getSenha(); 
        usuarioUpdater.aplicar(existente, novo, senhaParaPersistir);

        return usuarioRepository.save(existente);
    }

    private boolean equalsIgnoreCase(String a, String b) {
        if (a == null && b == null)
            return true;
        if (a == null || b == null)
            return false;
        return a.equalsIgnoreCase(b);
    }

    @Override
    @Transactional
    public boolean deletar(Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(
                "Não existe usuário cadastrado para este ID na base de dados."));
        usuarioRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Usuario> buscarPorNome(String nome) {
        String termo = usuarioValidator.validarNomeVazio(nome);
        return usuarioRepository.findByNomeContainingIgnoreCase(termo);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAllByOrderByNomeAsc();
    }

}
