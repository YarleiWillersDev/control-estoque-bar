package br.com.barghesla.barestoque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.barghesla.barestoque.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
