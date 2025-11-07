package br.com.barghesla.barestoque.controller.movimentacao;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueRequest;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueResponse;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueUpdateQuantidadeRequest;
import br.com.barghesla.barestoque.service.movimentacao.MovimentacaoEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/movimentacoes")
@Tag(name = "Movimentações de Estoque", description = "Operações relacionadas com a movimentação de estoque da API")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @PostMapping
    @Operation(summary = "Cria uma movimentação de estoque", description = "Cria uma movimentação de estoque registrando-a na base de dados")
    @ApiResponse(responseCode = "201", description = "Movimentação de estoque criada com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovimentacaoEstoqueResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)
    
    public ResponseEntity<MovimentacaoEstoqueResponse> criar(@Valid @RequestBody MovimentacaoEstoqueRequest request) {
        MovimentacaoEstoqueResponse novaMovimentacao = movimentacaoEstoqueService.registrarMovimentacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaMovimentacao);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza a quantidade da movimentação de estoque", 
               description = "Atualiza a quantidade da movimentação de estoque alterando-a na base de dados")
    @ApiResponse(responseCode = "200", description = "Quantidade da movimentação de estoque alterada com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovimentacaoEstoqueResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Movimentação de estoque não encontrada", content = @Content)

    public ResponseEntity<MovimentacaoEstoqueResponse> atualizarQauntidade(
        @Valid @RequestBody MovimentacaoEstoqueUpdateQuantidadeRequest request, 
        @Parameter(description = "ID utilizado para atualizar quantidade da movimentação") @PathVariable long id) {

        MovimentacaoEstoqueResponse movimentacaoAtualizada = movimentacaoEstoqueService.atualizarQuantidade(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(movimentacaoAtualizada);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca movimentação de estoque pelo ID", 
               description = "Busca a movimentação de estoque cadastrada na base de dados através do ID")
    @ApiResponse(responseCode = "200", description = "Movimentação encontrada com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovimentacaoEstoqueResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Movimentação de estoque não encontrada", content = @Content)

    public ResponseEntity<MovimentacaoEstoqueResponse> buscarPorId(@Parameter(description = "ID utilizado para buscar movimentação") @PathVariable long id) {
        MovimentacaoEstoqueResponse movimentacao = movimentacaoEstoqueService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(movimentacao);
    }

    @GetMapping
    @Operation(summary = "Busca todas as movimentações de estoque", 
               description = "Busca todas as movimentações de estoque cadastradas na base de dados")
    @ApiResponse(responseCode = "200", description = "Movimentações encontradas com sucesso",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MovimentacaoEstoqueResponse.class))))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Nenhuma movimentação de estoque foi encontrada", content = @Content)

    public ResponseEntity<List<MovimentacaoEstoqueResponse>> listarTodos() {
        List<MovimentacaoEstoqueResponse> movimentacoes = movimentacaoEstoqueService.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(movimentacoes);
    }

    @GetMapping(params = "produtoId")
    @Operation(summary = "Busca movimentação de estoque pelo ID do produto", 
               description = "Busca a movimentação de estoque cadastrada na base de dados através do ID do produto")
    @ApiResponse(responseCode = "200", description = "Movimentação encontrada com sucesso",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MovimentacaoEstoqueResponse.class))))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Movimentação de estoque não encontrada", content = @Content)

    public ResponseEntity<List<MovimentacaoEstoqueResponse>> buscarPorProduto(
        @Parameter(description = "ID utilizado para buscar movimentação") 
        @RequestParam("produtoId") long produtoId) {

        List<MovimentacaoEstoqueResponse> movimetacoesProduto = movimentacaoEstoqueService.buscarPorProduto(produtoId);
        return ResponseEntity.status(HttpStatus.OK).body(movimetacoesProduto);
    }
}
