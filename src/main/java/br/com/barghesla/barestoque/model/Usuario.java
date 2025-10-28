package br.com.barghesla.barestoque.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter //Metodos GET e SET gerados automaticamente pelo Lombok
@NoArgsConstructor @AllArgsConstructor //Construtores gerados automaticamente pelo Lombok

@Table(name="usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="nome", length=200, nullable=false)
    private String nome;

    @Column(name="email", length=150, nullable=false, unique=true)
    private String email;

    @Column(name="senha", length=255, nullable=false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.perfil == null) return Collections.emptyList();
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.perfil.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

}
