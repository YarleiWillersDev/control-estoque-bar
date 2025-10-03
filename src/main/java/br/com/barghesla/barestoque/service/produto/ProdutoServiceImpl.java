package br.com.barghesla.barestoque.service.produto;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.barghesla.barestoque.dto.produto.ProdutoRequest;
import br.com.barghesla.barestoque.dto.produto.ProdutoResponse;
import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
import br.com.barghesla.barestoque.exception.produto.ProdutoNaoCadastradoException;
import br.com.barghesla.barestoque.mapper.ProdutoMapper;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.repository.ProdutoRepository;
import br.com.barghesla.barestoque.updater.produto.ProdutoUpdater;
import br.com.barghesla.barestoque.validation.produto.ProdutoValidator;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoValidator produtoValidator;
    private final ProdutoUpdater produtoUpdater;
    private final CategoriaRepository categoriaRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, ProdutoValidator produtoValidator,
            ProdutoUpdater produtoUpdater, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.produtoValidator = produtoValidator;
        this.produtoUpdater = produtoUpdater;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
            .orElseThrow(() -> new CategoriaNaoEncontradaException("A categoria informada não existe na base de dados"));
        Produto produto = ProdutoMapper.toEntity(request, categoria);
        produtoValidator.validarCriacao(produto);
        produto.setStatus(StatusProduto.ATIVO);
        Produto produtoSalvo = produtoRepository.save(produto);
        return ProdutoMapper.toResponse(produtoSalvo);
    }

    @Override
    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
            .orElseThrow(() -> new CategoriaNaoEncontradaException("A categoria informada não existe na base de dados"));
        Produto existente = produtoRepository.findById(id)
            .orElseThrow(() -> new ProdutoNaoCadastradoException("Não existem produtos cadastrados para este id na base de dados!"));
        Produto produtoComNovosDados = ProdutoMapper.toEntity(request, categoria);
        produtoValidator.validarAtualizacao(existente, produtoComNovosDados);
        produtoUpdater.aplicar(existente, produtoComNovosDados);
        return ProdutoMapper.toResponse(existente);
    }

    @Override
    @Transactional
    public ProdutoResponse inativar(Long id) {
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoCadastradoException(
                        "Não existem produtos cadastrados para este id na base de dados!"));
        produtoValidator.validarInativacao(existente);
        existente.setStatus(StatusProduto.INATIVO);
        return ProdutoMapper.toResponse(existente);
    }

    @Override
    @Transactional
    public ProdutoResponse ativar(Long id) {
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoCadastradoException(
                        "Não existem produtos cadastrados para este id na base de dados!"));
        produtoValidator.validarAtivacao(existente);
        existente.setStatus(StatusProduto.ATIVO);
        return ProdutoMapper.toResponse(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        Produto existente = produtoRepository.findById(id)
            .orElseThrow(() -> new ProdutoNaoCadastradoException(
                "Não existem produtos cadastrados para este id na base de dados"));
        return ProdutoMapper.toResponse(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarPorNome(String nome) {
        List<Produto> listaProdutos = produtoRepository.findByNomeContainingIgnoreCase(nome);
        return ProdutoMapper.toResponse(listaProdutos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarPorCategoria(Long categoriaID) {
        List<Produto> listaProdutos = produtoRepository.findByCategoriaIdOrderByNomeAsc(categoriaID);
        return ProdutoMapper.toResponse(listaProdutos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarTodos() {
        List<Produto> listaProdutos = produtoRepository.findAll();
        return ProdutoMapper.toResponse(listaProdutos);
    }
}
