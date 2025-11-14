package br.com.barghesla.barestoque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.barghesla.barestoque.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByEmailIgnoreCase(String email);
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    List<Usuario> findAllByOrderByNomeAsc();
    boolean existsByEmailAndIdNot(String email, Long id);
    Optional<Usuario> findByEmail(String email);
}
