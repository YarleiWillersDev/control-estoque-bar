package br.com.barghesla.barestoque.model;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Getter @Setter //Gera os getters e setter automaticamente
@NoArgsConstructor @AllArgsConstructor // Gera um construtor com argumentos e um sem
@Table(name = "produto")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", length=255, nullable=false)
    private String nome;

    @Column(name = "descricao", length=300, nullable=true)
    private String descricao;

    @Column(name = "quantidade", length=10, nullable=false)
    private Integer quantidade;

    @Column(name = "preco_unitario", precision=10, scale=2, nullable=false)
    private BigDecimal precoUnitario;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private StatusProduto status = StatusProduto.ATIVO;
}
