package br.com.barghesla.barestoque.dto.categoria;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de requisição para cirar um categoria")
public record CategoriaRequest(

    @Schema(description = "Nome da Categoria",
            example = "Bebidas",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O nome não pode ser vazio")
    @Size(min = 3, max = 100)
    String nome) {

}
