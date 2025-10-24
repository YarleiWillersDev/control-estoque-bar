package br.com.barghesla.barestoque.mapper;

import java.util.List;

import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueRequest;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueResponse;
import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
import br.com.barghesla.barestoque.dto.usuario.UsuarioResponse;
import br.com.barghesla.barestoque.model.MovimentacaoEstoque;
import br.com.barghesla.barestoque.model.TipoMovimentacaoEstoque;

public final class MovimentacaoEstoqueMapper {

    private MovimentacaoEstoqueMapper() {}

    public static MovimentacaoEstoque toEntity(MovimentacaoEstoqueRequest request) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        
        try {
            movimentacao.setTipo(TipoMovimentacaoEstoque.valueOf(request.tipo().toUpperCase()));
        } catch (IllegalArgumentException e) {
            // Lança uma exceção se o tipo for inválido (ex: "ENTRADA", "SAIDA")
            throw new IllegalArgumentException("Tipo de movimentação inválido: " + request.tipo());
        }

        movimentacao.setQuantidade(request.quantidade());
        return movimentacao;
    }


    public static MovimentacaoEstoqueResponse toResponse(MovimentacaoEstoque entity) {
        if (entity == null) {
            return null;
        }

        // Usa os mappers existentes para converter as entidades aninhadas
        ProdutoResponse produtoResponse = ProdutoMapper.toResponse(entity.getProduto());
        UsuarioResponse usuarioResponse = UsuarioMapper.toResponse(entity.getUsuarioID());

        return new MovimentacaoEstoqueResponse(
                entity.getId(),
                entity.getTipo(),
                entity.getQuantidade(),
                entity.getDataMovimentacao(),
                produtoResponse,
                usuarioResponse
        );
    }

    public static List<MovimentacaoEstoqueResponse> toResponse(List<MovimentacaoEstoque> movimentacoes) {
        return movimentacoes.stream().map(MovimentacaoEstoqueMapper::toResponse).toList();

    }

}
