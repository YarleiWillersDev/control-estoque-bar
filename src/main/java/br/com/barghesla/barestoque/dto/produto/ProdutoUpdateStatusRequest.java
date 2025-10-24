package br.com.barghesla.barestoque.dto.produto;

import jakarta.validation.constraints.NotBlank;

public record ProdutoUpdateStatusRequest(
    @NotBlank(message = "O status não pode ser nulo ou vazio.")
    String status
) {

}
