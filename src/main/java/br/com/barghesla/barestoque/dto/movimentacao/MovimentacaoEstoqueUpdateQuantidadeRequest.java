package br.com.barghesla.barestoque.dto.movimentacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovimentacaoEstoqueUpdateQuantidadeRequest(
    @NotNull(message = "A nova quantidade não pode ser nula.")
    @Positive(message = "A nova quantidade deve ser um valor positivo.")
    Integer novaQuantidade
) {}
