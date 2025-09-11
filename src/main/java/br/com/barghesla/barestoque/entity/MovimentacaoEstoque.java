package br.com.barghesla.barestoque.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name="movimentacao_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false)
    private Long id;

    @Column(name="tipo", length=10, nullable=false)
    private String tipo;

    @Column(name="quantidade", nullable=false)
    private Integer quantidade;

    @Column(name="data_movimentacao", nullable=false)
    private LocalDateTime dataMovimentacao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name="produto_id", nullable=false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name="usuario_id", nullable=false)
    private Usuario usuario;
}
