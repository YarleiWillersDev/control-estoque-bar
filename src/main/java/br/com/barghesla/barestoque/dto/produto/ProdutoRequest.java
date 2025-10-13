package br.com.barghesla.barestoque.dto.produto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProdutoRequest(

        Long id,

        @NotBlank(message = "O nome não pode ser vazio.")
        @Size(min = 3, max = 100)
        String nome,
        
        String descricao,

        @NotNull
        @Positive
        Integer quantidade,

        @NotNull
        @Positive
        BigDecimal precoUnitario,

        @NotNull 
        Long categoriaId
) {

}
