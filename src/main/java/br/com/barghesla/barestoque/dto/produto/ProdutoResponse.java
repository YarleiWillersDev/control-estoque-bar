package br.com.barghesla.barestoque.dto.produto;

import java.math.BigDecimal;
import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;
import br.com.barghesla.barestoque.model.StatusProduto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta com os dados de um Produto")
public record ProdutoResponse(

        @Schema(description = "ID único do Produto gerado pelo sistema",
                example = "111",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Nome do Produto",
                example = "Arroz Branco")
        String nome,

        @Schema(description = "Descrição do Produto",
                example = "Parbolizado de 1KG")
        String descricao,

        @Schema(description = "Quantidade do Produto",
                example = "50")
        Integer quantidade,

        @Schema(description = "Preço unitário do Produto",
                example = "10.50")
        BigDecimal precoUnitario,

        @Schema(description = "ID da categoria vinculada ao Produto")
        CategoriaResponse categoria,

        @Schema(description = "Status do Produto no sistema",
                example = "ATIVO",
                allowableValues = {"ATIVO", "INATIVO"})
        StatusProduto status) {

}
