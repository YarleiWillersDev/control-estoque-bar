package br.com.barghesla.barestoque.controller.categoria;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.barghesla.barestoque.dto.categoria.CategoriaRequest;
import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;
import br.com.barghesla.barestoque.service.categoria.CategoriaService;
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
@RequestMapping("/categorias")
@Tag(name = "Categoria", description = "Operações as categorias do estoque")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @Operation(summary = "Criar nova categoria", description = "Cria nova categoria cadastrando-a na base de dados")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer permissão de 'GERENTE'.", content = @Content)

    public ResponseEntity<CategoriaResponse> cadastrarNovaCategoria(@Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse novaCategoria = categoriaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria existente", description = "Atualiza categoria existente atualizando-a na base de dados")
    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer permissão de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)

    public ResponseEntity<CategoriaResponse> atualizarCategoria(
        @Parameter(description = "ID da cartegoira que será atualizada") @PathVariable Long id, 
        @Valid @RequestBody CategoriaRequest request) {

        CategoriaResponse categoriaAtualizada = categoriaService.atualizar(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(categoriaAtualizada);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria existente", description = "Busca categoria existente na base de dados através do ID")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer permissão de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)

    public ResponseEntity<CategoriaResponse> buscarPorId(@Parameter(description = "ID da categoria que será buscada") @PathVariable Long id) {
        CategoriaResponse categoriaEncontrada = categoriaService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoriaEncontrada);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar categoria existente", description = "Busca categoria existente na base de dados através do nome ou parte dele")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoriaResponse.class))))
    @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer permissão de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)

    public ResponseEntity<List<CategoriaResponse>> buscarPorNome(
        @Parameter(description = "Nome ou parte dele utilizado para buscar a categoria")
        @RequestParam(name = "nome") String nome) {

        List<CategoriaResponse> categoriaEncontrada = categoriaService.buscarPorNome(nome);
        return ResponseEntity.status(HttpStatus.OK).body(categoriaEncontrada);
    }

    @GetMapping()
    @Operation(summary = "Buscar categorias existentes", description = "Busca todas as categorias existentes na base de dados")
    @ApiResponse(responseCode = "200", description = "Categorias encontradas com sucesso",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoriaResponse.class))))
    @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer permissão de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Nenhuma categoria foi encontrada", content = @Content)

    public ResponseEntity<List<CategoriaResponse>> listarTodos() {
        List<CategoriaResponse> listaDeCategorias = categoriaService.listarTodas();
        return ResponseEntity.status(HttpStatus.OK).body(listaDeCategorias);
    } 
    
}
