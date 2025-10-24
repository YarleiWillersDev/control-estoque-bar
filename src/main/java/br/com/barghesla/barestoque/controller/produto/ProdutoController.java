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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrarNovoProduto(@Valid @RequestBody ProdutoRequest produtoRequest) {
        ProdutoResponse novoProduto = produtoService.criar(produtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizarProdutoExistente(@Valid @RequestBody ProdutoRequest produtoRequest, @PathVariable Long id) {
        ProdutoResponse produtoAtualizado = produtoService.atualizar(id, produtoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(produtoAtualizado);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProdutoResponse> atualizarStatus(@PathVariable Long id, @Valid @RequestBody ProdutoUpdateStatusRequest request) {
        ProdutoResponse produtoComStatusAtualizado = produtoService.atualizarStatus(id, request.status());
        return ResponseEntity.status(HttpStatus.OK).body(produtoComStatusAtualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        ProdutoResponse produtoEncontradoPeloId = produtoService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(produtoEncontradoPeloId);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponse>> buscarPorNome(@RequestParam(name = "nome") String nome) {
        List<ProdutoResponse> produtosEncotradosPorNome = produtoService.buscarPorNome(nome);
        return ResponseEntity.status(HttpStatus.OK).body(produtosEncotradosPorNome);
    }

    @GetMapping(params = "categoriaId")
    public ResponseEntity<List<ProdutoResponse>> buscarPorCategoria(@RequestParam Long categoriaId) {
        List<ProdutoResponse> produtosEncontradosPelaCategoria = produtoService.buscarPorCategoria(categoriaId);
        return ResponseEntity.status(HttpStatus.OK).body(produtosEncontradosPelaCategoria);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos () {
        List<ProdutoResponse> listaDeProdutos = produtoService.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(listaDeProdutos);
    }

}
