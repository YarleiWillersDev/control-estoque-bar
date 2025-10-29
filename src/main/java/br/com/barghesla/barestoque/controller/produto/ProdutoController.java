package br.com.barghesla.barestoque.controller.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.barghesla.barestoque.dto.produto.ProdutoRequest;
import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
import br.com.barghesla.barestoque.dto.produto.ProdutoUpdateStatusRequest;
import br.com.barghesla.barestoque.service.produto.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Operações relacionadas aos pordutos do estoque")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo produto", description = "Cria um novo produto cadastrando-o na base de dados")
    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso", 
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)

    public ResponseEntity<ProdutoResponse> cadastrarNovoProduto(@Valid @RequestBody ProdutoRequest produtoRequest) {
        ProdutoResponse novoProduto = produtoService.criar(produtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atuliazar um produto existente", description = "Atualiza um produto existente alterando-o na base de dados")
    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso", 
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)

    public ResponseEntity<ProdutoResponse> atualizarProdutoExistente(
        @Valid @RequestBody ProdutoRequest produtoRequest,
        @Parameter(description = "ID do produto a ser atualizado") @PathVariable Long id) {
        
        ProdutoResponse produtoAtualizado = produtoService.atualizar(id, produtoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(produtoAtualizado);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do produto", description = "Atualiza o status de um produto existente salvando-o na base de dados")
    @ApiResponse(responseCode = "200", description = "Status do produto atualizado com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)

    public ResponseEntity<ProdutoResponse> atualizarStatus(@PathVariable Long id,
            @Valid @RequestBody ProdutoUpdateStatusRequest request) {
        ProdutoResponse produtoComStatusAtualizado = produtoService.atualizarStatus(id, request.status());
        return ResponseEntity.status(HttpStatus.OK).body(produtoComStatusAtualizado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto pelo ID", description = "Busca o produto referênciado pelo ID passado pelo request")
    @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)

    public ResponseEntity<ProdutoResponse> buscarPorId(@Parameter(description = "ID do produto encontrado") @PathVariable Long id) {
        ProdutoResponse produtoEncontradoPeloId = produtoService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(produtoEncontradoPeloId);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar produto pelo nome", description = "Busca o produto referênciado pelo nome passado pelo request")
    @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)

    public ResponseEntity<List<ProdutoResponse>> buscarPorNome(
        @Parameter(description = "Nome ou parte do nome do produto a ser pesquisado")
        @RequestParam(name = "nome") String nome) {
            
        List<ProdutoResponse> produtosEncotradosPorNome = produtoService.buscarPorNome(nome);
        return ResponseEntity.status(HttpStatus.OK).body(produtosEncotradosPorNome);
    }

    @GetMapping(params = "categoriaId")
    @Operation(summary = "Buscar produto pelo ID da categoria", description = "Busca o produto referênciado pelo ID da categoria passado pelo request")
    @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)

    public ResponseEntity<List<ProdutoResponse>> buscarPorCategoria(@Parameter(description = "ID da categoria referenciada")@RequestParam Long categoriaId) {
        List<ProdutoResponse> produtosEncontradosPelaCategoria = produtoService.buscarPorCategoria(categoriaId);
        return ResponseEntity.status(HttpStatus.OK).body(produtosEncontradosPelaCategoria);
    }

    @GetMapping
    @Operation(summary = "Buscar todos produtos cadastrados", description = "Busca todos os produtos que estão cadastrados na base de dados")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)

    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        List<ProdutoResponse> listaDeProdutos = produtoService.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(listaDeProdutos);
    }

}
