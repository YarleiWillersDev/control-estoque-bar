package br.com.barghesla.barestoque.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta contendo token JWT de autenticação")
public record AuthenticationResponse(
    
    @Schema(description = "Token JWT gerado para o usuário autenticado",
            example = "eyJhbGciOiJIUzI1Ni...JV_adQssw5c",
            accessMode = Schema.AccessMode.READ_ONLY)
    String token) {

}
