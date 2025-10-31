package br.com.barghesla.barestoque.dto.movimentacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de requisição para criar e atualizar Movimentação de Estoque")
public record MovimentacaoEstoqueRequest(
        
        @Schema(description = "Tipo da Movimentação de Estoque",
                example = "ENTRADA",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"ENTRADA", "SAIDA"})
        @NotBlank(message = "O tipo da movimetação não pode ser vazio")
        @Size(min = 5, max = 7)
        String tipo,
        
        @Schema(description = "Quantidade da Movimentação de Estoque",
                example = "150",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "A quantida não pode ser vazia")
        @Positive(message = "A quantidade deve ser positiva")
        Integer quantidade,
        
        @Schema(description = "ID do produto alvo da Movimentação de Estoque",
                example = "25",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O ID do produto não pode ser vazio")
        Long produto, 
        
        @Schema(description = "ID do usuário que criou a Movimentação de Estoque",
                example = "22",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O id do usuario não pode ser vazio")
        Long usuarioId) {

}
