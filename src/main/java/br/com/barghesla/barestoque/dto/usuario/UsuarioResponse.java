package br.com.barghesla.barestoque.dto.usuario;

public record UsuarioResponse(
    Long id, 
    String nome, 
    String email, 
    String perfil) {

}
