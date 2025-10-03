package br.com.barghesla.barestoque.mapper;

import java.util.List;
import br.com.barghesla.barestoque.dto.categoria.CategoriaRequest;
import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;
import br.com.barghesla.barestoque.entity.Categoria;

public final class CategoriaMapper {

    private CategoriaMapper() {} // Evitar que um construtor desta classe seja instanciado

    public static Categoria toEntity(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNome(request.nome());
        return categoria;
    }

    public static CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome());
    }

    public static List<CategoriaResponse> toResponse(List<Categoria> categorias) {
        return categorias.stream().map(CategoriaMapper::toResponse).toList();
    }
}
