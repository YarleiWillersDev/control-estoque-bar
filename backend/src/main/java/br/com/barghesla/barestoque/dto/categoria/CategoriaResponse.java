package br.com.barghesla.barestoque.dto.categoria;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta com os dados de uma categoria")
public record CategoriaResponse(
    
    @Schema(description = "ID único da categoria gerado pelo sistema",
            example = "10",
            accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "Nome da categoria",
            example = "Bebidas")
    String nome) {

}
