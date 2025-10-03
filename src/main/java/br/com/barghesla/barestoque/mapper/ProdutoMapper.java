package br.com.barghesla.barestoque.mapper;

import java.util.List;

import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;
import br.com.barghesla.barestoque.dto.produto.ProdutoRequest;
import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;

public final class ProdutoMapper {

    private ProdutoMapper() {}

    public static Produto toEntity(ProdutoRequest request, Categoria categoria) {
        Produto produto = new Produto();
        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setQuantidade(request.quantidade());
        produto.setPrecoUnitario(request.precoUnitario());
        produto.setCategoria(categoria);
        return produto;
    }

    public static ProdutoResponse toResponse(Produto produto) {
        CategoriaResponse categoriaResponse = new CategoriaResponse(
            produto.getCategoria().getId(),
            produto.getCategoria().getNome()
        );

        return new ProdutoResponse(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getQuantidade(),
            produto.getPrecoUnitario(),
            categoriaResponse, // Usa o DTO de categoria aninhado
            produto.getStatus()
        );
    }

    public static List<ProdutoResponse> toResponse(List<Produto> produtos) {
        return produtos.stream().map(ProdutoMapper::toResponse).toList();
    }

}
