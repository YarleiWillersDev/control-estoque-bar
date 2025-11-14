package br.com.barghesla.barestoque.dto.movimentacao;

import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
import br.com.barghesla.barestoque.dto.usuario.UsuarioResponse;
import br.com.barghesla.barestoque.model.TipoMovimentacaoEstoque;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Objeto de resposta com os dados de uma movimentação de estoque")
public record MovimentacaoEstoqueResponse(
    
    @Schema(description = "ID único da movimentação gerado pelo sistema",
            example = "301",
            accessMode = Schema.AccessMode.READ_ONLY)
    Long id,
    
    @Schema(description = "Tipo da movimentação (ENTRADA ou SAIDA)",
            example = "ENTRADA",
            allowableValues = {"ENTRADA", "SAIDA"})
    TipoMovimentacaoEstoque tipo,
    
    @Schema(description = "Quantidade de itens que foram movimentados",
            example = "100")
    Integer quantidade,
    
    @Schema(description = "Data e hora em que a movimentação foi registrada",
            example = "2025-10-31T11:50:00",
            accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime dataMovimentacao,
    
    @Schema(description = "Dados do produto que foi movimentado")
    ProdutoResponse produto,
    
    @Schema(description = "Dados do usuário que registrou a movimentação")
    UsuarioResponse usuario
) {}

