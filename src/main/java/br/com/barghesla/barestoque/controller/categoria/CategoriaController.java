package br.com.barghesla.barestoque.controller.categoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> cadastrarNovaCategoria(@Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse novaCategoria = categoriaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse categoriaAtualizada = categoriaService.atualizar(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(categoriaAtualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {
        CategoriaResponse categoriaEncontrada = categoriaService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoriaEncontrada);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponse>> buscarPorNome(@RequestParam(name = "nome") String nome) {
        List<CategoriaResponse> categoriaEncontrada = categoriaService.buscarPorNome(nome);
        return ResponseEntity.status(HttpStatus.OK).body(categoriaEncontrada);
    }

    @GetMapping()
    public ResponseEntity<List<CategoriaResponse>> listarTodos() {
        List<CategoriaResponse> listaDeCategorias = categoriaService.listarTodas();
        return ResponseEntity.status(HttpStatus.OK).body(listaDeCategorias);
    } 

}
