package br.com.barghesla.barestoque.config;

import br.com.barghesla.barestoque.exception.categoria.*;
import br.com.barghesla.barestoque.exception.movimentacao.*;
import br.com.barghesla.barestoque.exception.produto.*;
import br.com.barghesla.barestoque.exception.usuario.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    private record ErrorResponse(String mensagem) {
    }

    @ExceptionHandler({
            ProdutoNaoCadastradoException.class,
            UsuarioNaoEncontradoException.class,
            CategoriaNaoEncontradaException.class,
            MovimentacaoEstoqueInexistenteException.class

    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({
            ProdutoJaExistenteException.class,
            EmailJaExistenteException.class,
            LoginJaExistenteException.class,
            NomeDuplicadoException.class,
            CategoriaJaExistenteException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictExceptions(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({
            PrecoInvalidoException.class,
            ProdutoJaAtivoException.class,
            ProdutoJaInativoException.class,
            ProdutoNaoPodeSerNuloException.class,
            ProdutoStatusInvalidoException.class,
            QuantidadeInvalidaException.class,
            CampoNomeNuloException.class,
            EmailObrigatorioException.class,
            NomeObrigatorioException.class,
            PerfilObrigatorioException.class,
            SenhaFracaException.class,
            SenhaInvalidaException.class,
            SenhaObrigatoriaException.class,
            UsuarioNullException.class,
            AlteracaoDeDataException.class,
            AlteracaoDeProdutoException.class,
            AlteracaoDeTipoException.class,
            AlteracaoDeUsuarioException.class,
            DataMovimentacaoEstoqueNulaException.class,
            ProdutoIdMovimentacaoEstoqueNuloException.class,
            QuantidadeMovimentacaoEstoqueNegativaException.class,
            QuantidadeMovimentacaoEstoqueNulaException.class,
            TipoDeMovimentacaoEstoqueInvalidoException.class,
            TipoDeMovimentacaoEstoqueNuloException.class,
            UsuarioIdMovimentacaoEstoqueNuloException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestExceptions(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
