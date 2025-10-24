package br.com.barghesla.barestoque.validation.usuario;

import org.springframework.stereotype.Component;

import br.com.barghesla.barestoque.exception.usuario.CampoNomeNuloException;
import br.com.barghesla.barestoque.exception.usuario.EmailJaExistenteException;
import br.com.barghesla.barestoque.exception.usuario.EmailObrigatorioException;
import br.com.barghesla.barestoque.exception.usuario.NomeDuplicadoException;
import br.com.barghesla.barestoque.exception.usuario.NomeObrigatorioException;
import br.com.barghesla.barestoque.exception.usuario.PerfilObrigatorioException;
import br.com.barghesla.barestoque.exception.usuario.SenhaInvalidaException;
import br.com.barghesla.barestoque.exception.usuario.UsuarioNullException;
import br.com.barghesla.barestoque.model.Usuario;
import br.com.barghesla.barestoque.repository.UsuarioRepository;

@Component
public class UsuarioValidator {

    private final UsuarioRepository usuarioRepository;

    public UsuarioValidator(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void validarUsuario(Usuario usuario) {
        validarUsuarioNulo(usuario);
        validarNomeNulo(usuario);
        validarNomeJaCadastrado(usuario);
        validarEmailNulo(usuario);
        validarEmailJaCadastrado(usuario);
        validarSenha(usuario);
        validarPerfil(usuario);
    }

    public void validarUsuarioNulo(Usuario usuario) {
        if (usuario == null) {
            throw new UsuarioNullException("Usuário não pode ser nulo");
        }
    }

    public void validarNomeNulo(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new NomeObrigatorioException("O campo nome do usuário não pode ser nulo ou vazio");
        }
    }

    public void validarNomeJaCadastrado(Usuario usuario) {
        if (usuarioRepository.existsByNomeIgnoreCase(usuario.getNome())) {
            throw new NomeDuplicadoException("Já existe um usuário com este nome cadastrado na base de dados");
        }
    }

    public void validarEmailNulo(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new EmailObrigatorioException("O campo email do usuario não pode ser nulo ou vazio");
        }
    }

    public void validarEmailJaCadastrado(Usuario usuario) {
        if (usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())) {
            throw new EmailJaExistenteException("Já existe um usuário com este email cadastrado na base de dados");
        }
    }

    public void validarSenha(Usuario usuario) {
        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new SenhaInvalidaException("O campo senha do usuario não pode ser nulo ou vazio");
        }
        if (usuario.getSenha().length() < 8) {
            throw new SenhaInvalidaException("A senha deve ter pelo menos 8 caracteres");
        }
    }

    public void validarPerfil(Usuario ususario) {
        if (ususario.getPerfil() == null || ususario.getPerfil().isBlank()) {
            throw new PerfilObrigatorioException("O campo perfil não pode ser nulo ou vazio!");
        }
    }

    public void validarAtualizacao(Usuario existente, Usuario novo) {
        if (!isNomePresente(novo)) {
            throw new NomeObrigatorioException("O campo nome do usuário não pode ser nulo ou vazio");
        }
        if (!isEmailPresente(novo)) {
            throw new EmailObrigatorioException("O campo email do usuario não pode ser nulo ou vazio");
        }
        if (!safeEqualsIgnoreCase(existente.getEmail(), novo.getEmail())
                && !isEmailDisponivel(novo)) {
            throw new EmailJaExistenteException("Já existe um usuário com este email cadastrado na base de dados");
        }
        if (!isSenhaValidaSeInformada(novo)) {
            throw new SenhaInvalidaException("A senha deve ter pelo menos 8 caracteres");
        }
        if (!isPerfilPresente(novo)) {
            throw new PerfilObrigatorioException("O campo perfil não pode ser nulo ou vazio!");
        }
    }

    private boolean isNomePresente(Usuario usuario) {
        return usuario.getNome() != null && !usuario.getNome().isBlank();
    }

    private boolean isEmailPresente(Usuario usuario) {
        return usuario.getEmail() != null && !usuario.getEmail().isBlank();
    }

    private boolean isEmailDisponivel(Usuario usuario) {
        return !usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail());
    }

    private boolean isSenhaValidaSeInformada(Usuario usuario) {
        if (usuario.getSenha() == null || usuario.getSenha().isBlank())
            return true;
        return usuario.getSenha().length() >= 8;
    }

    private boolean isPerfilPresente(Usuario usuario) {
        return usuario.getPerfil() != null && !usuario.getPerfil().isBlank();
    }

    private boolean safeEqualsIgnoreCase(String a, String b) {
        if (a == null && b == null)
            return true;
        if (a == null || b == null)
            return false;
        return a.equalsIgnoreCase(b);
    }

    public String validarNomeVazio(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new CampoNomeNuloException("Nome não pode ser nulo ou conter somente espaços.");
        }
        return nome.trim();
    }

}
