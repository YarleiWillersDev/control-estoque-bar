package br.com.barghesla.barestoque.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(

    @NotBlank(message = "O nome não pode ser vazio")
    @Size(min = 3, max = 100)
    String nome) {

}
