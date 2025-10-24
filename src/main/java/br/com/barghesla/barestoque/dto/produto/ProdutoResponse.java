package br.com.barghesla.barestoque.dto.produto;

import java.math.BigDecimal;
import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;
import br.com.barghesla.barestoque.model.StatusProduto;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        Integer quantidade,
        BigDecimal precoUnitario,
        CategoriaResponse categoriaID,
        StatusProduto status) {

}
