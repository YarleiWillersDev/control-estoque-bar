package br.com.barghesla.barestoque.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Usuario {

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

    @Column(name="perfil", length=20, nullable=false)
    private String perfil;
}
