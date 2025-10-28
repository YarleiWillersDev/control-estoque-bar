package br.com.barghesla.barestoque.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.barghesla.barestoque.model.Usuario;
import br.com.barghesla.barestoque.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService{

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados"));
        return usuario;
    }

}
