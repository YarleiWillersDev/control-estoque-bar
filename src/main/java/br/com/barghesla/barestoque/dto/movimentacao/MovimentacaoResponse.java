package br.com.barghesla.barestoque.dto.movimentacao;

import java.time.LocalDateTime;
import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
import br.com.barghesla.barestoque.dto.usuario.UsuarioResponse;
import br.com.barghesla.barestoque.entity.TipoMovimentacaoEstoque;

public record MovimentacaoResponse(
        Long id,
        TipoMovimentacaoEstoque tipo,
        Integer quantidade,
        LocalDateTime dataMovimentacao,
        ProdutoResponse produto,
        UsuarioResponse usuarioID) {

}
