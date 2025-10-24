package br.com.barghesla.barestoque.controller.movimentacao;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueResponse> criar(@Valid @RequestBody MovimentacaoEstoqueRequest request) {
        MovimentacaoEstoqueResponse novaMovimentacao = movimentacaoEstoqueService.registrarMovimentacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaMovimentacao);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponse> atualizarQauntidade(@Valid @RequestBody MovimentacaoEstoqueUpdateQuantidadeRequest request, 
                                                                            @PathVariable long id) {
        MovimentacaoEstoqueResponse movimentacaoAtualizada = movimentacaoEstoqueService.atualizarQuantidade(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(movimentacaoAtualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponse> buscarPorId(@PathVariable long id) {
        MovimentacaoEstoqueResponse movimentacao = movimentacaoEstoqueService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(movimentacao);
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoqueResponse>> listarTodos() {
        List<MovimentacaoEstoqueResponse> movimentacoes = movimentacaoEstoqueService.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(movimentacoes);
    }

    @GetMapping(params = "produtoId")
    public ResponseEntity<List<MovimentacaoEstoqueResponse>> buscarPorProduto(@RequestParam("produtoId") long produtoId) {
        List<MovimentacaoEstoqueResponse> movimetacoesProduto = movimentacaoEstoqueService.buscarPorProduto(produtoId);
        return ResponseEntity.status(HttpStatus.OK).body(movimetacoesProduto);
    }
}
