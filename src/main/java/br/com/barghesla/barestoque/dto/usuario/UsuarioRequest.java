package br.com.barghesla.barestoque.dto.usuario;

import br.com.barghesla.barestoque.model.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de requisição para cirar ou atualizar um usuário")
public record UsuarioRequest(

    @Schema(description = "Nome completo do usuário",
            example = "Yarlei Rafael Willers",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O nome não pode ser vazio")
    @Size(min = 3, max = 100)
    String nome, 

    @Schema(description = "Endereço de e-mail único do usuário",
            example = "[email protected]",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O email não pode ser vazio")
    @Email(message = "Formato de email inválido")
    @Size(max = 100)
    String email,

    @Schema(description = "Senha do usuário (mínimo de 8 caracteres)",
            example = "SenhaSegura@123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "A senha não pode ser vazia")
    @Size(min = 8, message = "A senha deve conter no mínimo 8 caracteres")
    String senha, 

    @Schema(description = "Perfil de acesso do usuário no sistema",
            example = "GERENTE",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"GERENTE", "VENDEDOR"})
    @NotNull(message = "O perfil não pode ser vazio")
    Perfil perfil) {

}
