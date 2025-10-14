package br.com.barghesla.barestoque.service.usuario;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.barghesla.barestoque.dto.usuario.UsuarioRequest;
import br.com.barghesla.barestoque.dto.usuario.UsuarioResponse;
import br.com.barghesla.barestoque.exception.usuario.EmailJaExistenteException;
import br.com.barghesla.barestoque.exception.usuario.UsuarioNaoEncontradoException;
import br.com.barghesla.barestoque.mapper.UsuarioMapper;
import br.com.barghesla.barestoque.model.Usuario;
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
    @Transactional
    public UsuarioResponse salvar(UsuarioRequest request) {
        Usuario usuarioNovo = UsuarioMapper.toEntity(request);
        usuarioValidator.validarUsuario(usuarioNovo);
        Usuario usuarioSalvo = usuarioRepository.save(usuarioNovo);
        return UsuarioMapper.toResponse(usuarioSalvo);
    }

    @Override
    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(
                        "Não existe usuário cadastrado para este ID na base de dados."));

        Usuario usuarioComNovosDados = UsuarioMapper.toEntity(request);

        usuarioValidator.validarAtualizacao(existente, usuarioComNovosDados);
        validarEmailUnicoParaAtualizacao(id, existente.getEmail(), usuarioComNovosDados.getEmail());

        String senhaParaPersistir = usuarioComNovosDados.getSenha(); 
        usuarioUpdater.aplicar(existente, usuarioComNovosDados, senhaParaPersistir);

        Usuario usuarioSalvo = usuarioRepository.save(existente);
        return UsuarioMapper.toResponse(usuarioSalvo);
    }

    private void validarEmailUnicoParaAtualizacao(Long idUsuario, String emailAtual, String novoEmail) {
        if (novoEmail == null || novoEmail.equalsIgnoreCase(emailAtual)) {
            return;
        }
        if (usuarioRepository.existsByEmailAndIdNot(novoEmail, idUsuario)) {
            throw new EmailJaExistenteException("Já existe um usuário com este email cadastrado na base de dados.");
        }
    }
    
    @Override
    @Transactional
    public void deletar(Long id) {
        usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(
                "Não existe usuário cadastrado para este ID na base de dados."));
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarPorNome(String nome) {
        String termo = usuarioValidator.validarNomeVazio(nome);
        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(termo);
        return UsuarioMapper.toResponse(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAllByOrderByNomeAsc();
        return UsuarioMapper.toResponse(usuarios);
    }

}
