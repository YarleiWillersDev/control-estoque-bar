package br.com.barghesla.barestoque.dto.movimentacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Objeto de requisição para atualizar a quantidade de uma movimentação de estoque existente")
public record MovimentacaoEstoqueUpdateQuantidadeRequest(

    @Schema(description = "A nova quantidade para a movimentação",
            example = "120",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "A nova quantidade não pode ser nula.")
    @Positive(message = "A nova quantidade deve ser um valor positivo.")
    Integer novaQuantidade
) {}

