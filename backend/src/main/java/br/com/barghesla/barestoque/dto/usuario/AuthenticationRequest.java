package br.com.barghesla.barestoque.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de requisição para autenticação do usuário")
public record AuthenticationRequest(

    @Schema(description = "Edereço de e-mail do usuário cadastrado",
            example = "usuario@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    String email,

    @Schema(description = "Senha do usuário cadastrado",
            example = "SenhaSegura@123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    String senha
) {}
