package br.com.barghesla.barestoque.dto.movimentacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MovimentacaoEstoqueRequest(
        
        Long id, 
        
        @NotBlank(message = "O tipo da movimetação não pode ser vazio")
        @Size(min = 5, max = 7)
        String tipo,
        
        @NotNull(message = "A quantida não pode ser vazia")
        @Positive(message = "A quantidade deve ser positiva")
        Integer quantidade,
        
        @NotNull(message = "O ID do produto não pode ser vazio")
        Long produto, 
        
        @NotNull(message = "O id do usuario não pode ser vazio")
        Long usuarioID) {

}
