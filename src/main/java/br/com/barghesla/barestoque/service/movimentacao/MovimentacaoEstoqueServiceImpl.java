package br.com.barghesla.barestoque.service.movimentacao;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.barghesla.barestoque.entity.MovimentacaoEstoque;

@Service
public class MovimentacaoEstoqueServiceImpl implements MovimentacaoEstoqueService{

    @Override
    public MovimentacaoEstoque registrarMovimentacao(MovimentacaoEstoque movimentacao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarMovimentacao'");
    }

    @Override
    public MovimentacaoEstoque atualizar(Long id, MovimentacaoEstoque movimentacao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");
    }

    @Override
    public MovimentacaoEstoque buscarPorId(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
    }

    @Override
    public List<MovimentacaoEstoque> listarTodos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarTodos'");
    }

    @Override
    public List<MovimentacaoEstoque> buscarPorProduto(Long produtoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorProduto'");
    }

}
