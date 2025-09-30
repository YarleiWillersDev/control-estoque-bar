package br.com.barghesla.barestoque.dto.produto;

import java.math.BigDecimal;
import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        Integer quantidade,
        BigDecimal precoUnitario,
        CategoriaResponse categoriaID,
        String status) {

}
