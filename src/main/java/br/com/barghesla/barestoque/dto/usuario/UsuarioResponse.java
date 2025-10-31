package br.com.barghesla.barestoque.dto.usuario;

import br.com.barghesla.barestoque.model.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta com os dados de um usuário")
public record UsuarioResponse(

    @Schema(description = "ID único do usuário gerado pelo sistema",
            example = "23",
            accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "Nome completo do usuário",
            example = "Yarlei Rafael Willers")
    String nome,

    @Schema(description = "Endereço de e-mail do usuário",
            example = "usuario@gmail.com")
    String email, 

    @Schema(description = "Perfil de acesso do usuário",
            example = "VENDEDOR",
            allowableValues = {"GERENTE", "VENDEDOR"})
    Perfil perfil) {

}
