package br.com.barghesla.barestoque.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requisição para atualizar o status do Produto")
public record ProdutoUpdateStatusRequest(

    @Schema(description = "O novo status do produto.", 
            example = "INATIVO",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"ATIVO", "INATIVO"})
    @NotBlank(message = "O status não pode ser nulo ou vazio.")
    String status
) {

}
