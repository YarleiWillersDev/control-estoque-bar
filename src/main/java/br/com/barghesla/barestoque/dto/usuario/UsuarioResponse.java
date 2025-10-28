package br.com.barghesla.barestoque.dto.usuario;

import br.com.barghesla.barestoque.model.Perfil;

public record UsuarioResponse(
    Long id, 
    String nome, 
    String email, 
    Perfil perfil) {

}
