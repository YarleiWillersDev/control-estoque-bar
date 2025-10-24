package br.com.barghesla.barestoque.validation.movimentacao;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

import br.com.barghesla.barestoque.exception.movimentacao.ProdutoIdMovimentacaoEstoqueNuloException;
import br.com.barghesla.barestoque.exception.movimentacao.QuantidadeMovimentacaoEstoqueNegativaException;
import br.com.barghesla.barestoque.exception.movimentacao.QuantidadeMovimentacaoEstoqueNulaException;
import br.com.barghesla.barestoque.exception.movimentacao.TipoDeMovimentacaoEstoqueNuloException;
import br.com.barghesla.barestoque.exception.movimentacao.UsuarioIdMovimentacaoEstoqueNuloException;
import br.com.barghesla.barestoque.model.MovimentacaoEstoque;

@Component
public class MovimentacaoEstoqueValidation {

    public void validarCamposParaRegistro(MovimentacaoEstoque movimentacaoEstoque) {
        validarTipoObrigatorio(movimentacaoEstoque);
        validarQuantidadePositiva(movimentacaoEstoque);
        validarDataNula(movimentacaoEstoque);
        validarProdutoObrigatorio(movimentacaoEstoque);
        validarUsuarioObrigatotio(movimentacaoEstoque);
    }

    public void validarCamposParaAtualizarQuantidade(MovimentacaoEstoque mov) {
        validarQuantidadePositiva(mov);
        bloquearAlteracaoDeTipo(mov);
        bloquearAlteracaoDeProduto(mov);
        bloquearAlteracaoDeUsuario(mov);
        bloquearAlteracaoDeData(mov);
    }

    private void validarTipoObrigatorio(MovimentacaoEstoque movimentacaoEstoque) {
        if (movimentacaoEstoque.getTipo() == null) {
            throw new TipoDeMovimentacaoEstoqueNuloException("O campo TIPO da movimentação de estoque não pode ser nulo");
        }
    }

    private void validarQuantidadePositiva(MovimentacaoEstoque movimentacaoEstoque) {
        if (movimentacaoEstoque.getQuantidade() == null) {
            throw new QuantidadeMovimentacaoEstoqueNulaException("O campo QUANTIDADE da movimentação de estoque não pode ser nulo");
        }
        if (movimentacaoEstoque.getQuantidade() < 0) {
            throw new QuantidadeMovimentacaoEstoqueNegativaException("O campo QUANTIDADE da movimentação de estoque não pode ser negativo");
        }
    }

    private void validarDataNula(MovimentacaoEstoque movimentacaoEstoque) {
        if (movimentacaoEstoque.getDataMovimentacao() == null) {
            movimentacaoEstoque.setDataMovimentacao(LocalDateTime.now());
        }
    }

    private void validarProdutoObrigatorio(MovimentacaoEstoque movimentacaoEstoque) {
        if (movimentacaoEstoque.getProduto() == null) {
            throw new ProdutoIdMovimentacaoEstoqueNuloException("O campo PRODUTO ID não pode ser nulo");
        }
    }

    private void validarUsuarioObrigatotio(MovimentacaoEstoque movimentacaoEstoque) {
        if (movimentacaoEstoque.getUsuarioID() == null) {
            throw new UsuarioIdMovimentacaoEstoqueNuloException("O campo USUARIO ID não pode ser nulo");
        }
    }

    private void bloquearAlteracaoDeTipo(MovimentacaoEstoque mov) {

    }

    private void bloquearAlteracaoDeProduto(MovimentacaoEstoque mov) {
        
    }

    private void bloquearAlteracaoDeUsuario(MovimentacaoEstoque mov) {
        
    }

    private void bloquearAlteracaoDeData(MovimentacaoEstoque mov) {
        
    }



}
