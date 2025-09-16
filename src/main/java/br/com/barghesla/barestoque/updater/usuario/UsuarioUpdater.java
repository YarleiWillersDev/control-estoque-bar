package br.com.barghesla.barestoque.updater.usuario;

import org.springframework.stereotype.Component;
import br.com.barghesla.barestoque.entity.Usuario;

@Component
public class UsuarioUpdater {

    // Hoje recebe senha em texto; quando houver segurança, passe o hash
    public void aplicar(Usuario existente, Usuario novo, String senhaParaPersistir) {
        existente.setNome(novo.getNome());
        existente.setEmail(novo.getEmail());
        existente.setPerfil(novo.getPerfil());

        if (senhaParaPersistir != null && !senhaParaPersistir.isBlank()) {
            existente.setSenha(senhaParaPersistir);
        }
    }
}
