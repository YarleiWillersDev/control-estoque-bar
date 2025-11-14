package br.com.barghesla.barestoque.dto.produto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de requisição para criar ou atualizar produto")
public record ProdutoRequest(

        @Schema(description = "Nome do Produto",
                example = "Cerveja",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome não pode ser vazio.")
        @Size(min = 3, max = 100)
        String nome,
        
        @Schema(description = "Descrição do Produto",
                example = "Arroz branco parbolizado")
        String descricao,

        @Schema(description = "Quantidade do Produto",
                example = "50",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @PositiveOrZero
        Integer quantidade,

        @Schema(description = "Preço unitário do Produto",
                example = "9.50",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Positive
        BigDecimal precoUnitario,

        @Schema(description = "ID da categoria que o Produto pertence",
                example = "2",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull 
        Long categoriaId
) {

}
