package br.com.barghesla.barestoque.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
    Long id, 
    
    @NotBlank(message = "O nome não pode ser vazio")
    @Size(min = 3, max = 100)
    String nome, 

    @NotBlank(message = "O email não pode ser vazio")
    @Email(message = "Formato de email inválido")
    @Size(max = 100)
    String email,

    @NotBlank(message = "A senha não pode ser vazia")
    @Size(min = 8, message = "A senha deve conter no mínimo 8 caracteres")
    String senha, 

    @NotBlank(message = "O perfil não pode ser vazio")
    String perfil) {

}
