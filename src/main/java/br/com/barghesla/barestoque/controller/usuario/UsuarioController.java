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
import jakarta.validation.Valid;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criarNovoUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse novoUsuario = usuarioService.salvar(usuarioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@Valid @RequestBody UsuarioRequest usuarioRequest, @PathVariable long id) {
        UsuarioResponse usuarioResponse = usuarioService.atualizar(id, usuarioRequest);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable long id) {
        usuarioService.deletar(id);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponse>> buscarPorNome(@RequestParam(name = "nome") String nome) {
        List<UsuarioResponse> usuariosEncontrados = usuarioService.buscarPorNome(nome);
        return ResponseEntity.status(HttpStatus.OK).body(usuariosEncontrados);
    }

    @GetMapping()
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }
}
