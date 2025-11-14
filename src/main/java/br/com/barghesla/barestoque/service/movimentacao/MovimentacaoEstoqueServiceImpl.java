package br.com.barghesla.barestoque.service.movimentacao;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueRequest;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueResponse;
import br.com.barghesla.barestoque.dto.movimentacao.MovimentacaoEstoqueUpdateQuantidadeRequest;
import br.com.barghesla.barestoque.exception.movimentacao.MovimentacaoEstoqueInexistenteException;
import br.com.barghesla.barestoque.mapper.MovimentacaoEstoqueMapper;
import br.com.barghesla.barestoque.model.MovimentacaoEstoque;
import br.com.barghesla.barestoque.model.Produto;
import br.com.barghesla.barestoque.model.Usuario;
import br.com.barghesla.barestoque.repository.MovimentacaoRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.repository.UsuarioRepository;
import br.com.barghesla.barestoque.updater.movimentacao.MovimentacaoEstoqueUpdater;

@Service
public class MovimentacaoEstoqueServiceImpl implements MovimentacaoEstoqueService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueUpdater movimentacaoEstoqueUpdater;
    private final UsuarioRepository usuarioRepository;

    public MovimentacaoEstoqueServiceImpl(MovimentacaoRepository movimentacaoRepository,
            ProdutoRepository produtoRepository, MovimentacaoEstoqueUpdater movimentacaoEstoqueUpdater, UsuarioRepository usuarioRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
        this.movimentacaoEstoqueUpdater = movimentacaoEstoqueUpdater;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public MovimentacaoEstoqueResponse registrarMovimentacao(MovimentacaoEstoqueRequest request) {
        MovimentacaoEstoque movimentacaoEstoque = construirMovimentacaoConformeNecessidadesDoRequest(request);
        movimentacaoEstoqueUpdater.prepararParaRegistrar(movimentacaoEstoque);
        MovimentacaoEstoque movimentacaoSalva = movimentacaoRepository.save(movimentacaoEstoque);
        return MovimentacaoEstoqueMapper.toResponse(movimentacaoSalva);
    }

    private MovimentacaoEstoque construirMovimentacaoConformeNecessidadesDoRequest(MovimentacaoEstoqueRequest request) {
        if (request.produto() == null || request.usuarioId() == null) {
            throw new IllegalArgumentException("IDs de produto e usuário são obrigatórios.");
        }

        Produto produto = produtoRepository.findById(request.produto())
                .orElseThrow(
                        () -> new IllegalArgumentException("Produto com ID " + request.produto() + " não encontrado."));

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuário com ID " + request.usuarioId() + " não encontrado."));

        MovimentacaoEstoque movimentacao = MovimentacaoEstoqueMapper.toEntity(request);

        movimentacao.setProduto(produto);
        movimentacao.setUsuarioID(usuario);

        return movimentacao;
    }

    @Override
    @Transactional
    public MovimentacaoEstoqueResponse atualizarQuantidade(Long id, MovimentacaoEstoqueUpdateQuantidadeRequest request) {
        MovimentacaoEstoque movimentacaoAtual = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new MovimentacaoEstoqueInexistenteException("Movimentação com ID " + id + " não encontrada."));

        int novaQuantidade = request.novaQuantidade();
        movimentacaoEstoqueUpdater.atualizarQuantidade(movimentacaoAtual, novaQuantidade);

        MovimentacaoEstoque movimentacaoSalva = movimentacaoRepository.save(movimentacaoAtual);
        return MovimentacaoEstoqueMapper.toResponse(movimentacaoSalva);
    }

    @Override
    @Transactional(readOnly = true)
    public MovimentacaoEstoqueResponse buscarPorId(Long id) {
        MovimentacaoEstoque movimentacao = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new MovimentacaoEstoqueInexistenteException(
                        "Não existem movimentações de estoque para o ID: " + id + " cadastradas na base de dados"));
        return MovimentacaoEstoqueMapper.toResponse(movimentacao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueResponse> listarTodos() {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoRepository.findAllByOrderByDataMovimentacao();
        return MovimentacaoEstoqueMapper.toResponse(movimentacoes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueResponse> buscarPorProduto(Long produtoId) {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoRepository.findByProdutoIdOrderByDataMovimentacaoDesc(produtoId);
        return MovimentacaoEstoqueMapper.toResponse(movimentacoes);
    }

}
