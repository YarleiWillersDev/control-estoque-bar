package br.com.barghesla.barestoque.controller.usuario;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.barghesla.barestoque.dto.usuario.UsuarioRequest;
import br.com.barghesla.barestoque.dto.usuario.UsuarioResponse;
import br.com.barghesla.barestoque.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Operações relacionadas aos usuários da API")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo usuário e cadastra o mesmo na base de dados")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)

    public ResponseEntity<UsuarioResponse> criarNovoUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse novoUsuario = usuarioService.salvar(usuarioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um usuário existente", description = "Atualiza um usuário existente salvando-o na base de dados")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)

    public ResponseEntity<UsuarioResponse> atualizar(
        @Valid @RequestBody UsuarioRequest usuarioRequest, 
        @Parameter(description = "ID do usuário que será atualizado") @PathVariable long id) {

        UsuarioResponse usuarioResponse = usuarioService.atualizar(id, usuarioRequest);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um usuário existente", description = "Deleta um usuário existente excluindo-o na base de dados")
    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@Parameter(description = "ID do usuário que será excluído") @PathVariable long id) {
        usuarioService.deletar(id);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca um usuário pelo nome", description = "Busca um usuário existente na base de dados através do nome ou parte dele")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class))))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)

    public ResponseEntity<List<UsuarioResponse>> buscarPorNome(
        @Parameter(description = "Nome ou parte do nome do usuário a ser pesquisado") 
        @RequestParam(name = "nome") String nome) {
        
        List<UsuarioResponse> usuariosEncontrados = usuarioService.buscarPorNome(nome);
        return ResponseEntity.status(HttpStatus.OK).body(usuariosEncontrados);
    }

    @GetMapping()
    @Operation(summary = "Lista todos os usuários", description = "Lista todos os usuários existentes na base de dados")
    @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class))))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil de 'GERENTE'.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Nenhum usuário foi encontrado", content = @Content)

    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }
}
