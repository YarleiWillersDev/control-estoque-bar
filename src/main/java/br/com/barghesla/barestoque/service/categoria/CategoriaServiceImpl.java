package br.com.barghesla.barestoque.service.categoria;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.barghesla.barestoque.dto.categoria.CategoriaRequest;
import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;
import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
import br.com.barghesla.barestoque.mapper.CategoriaMapper;
import br.com.barghesla.barestoque.repository.CategoriaRepository;
import br.com.barghesla.barestoque.updater.categoria.CategoriaUpdater;
import br.com.barghesla.barestoque.validation.categoria.CategoriaValidator;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaValidator categoriaValidator;
    private final CategoriaUpdater categoriaUpdater;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaValidator categoriaValidator,
            CategoriaUpdater categoriaUpdater) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaValidator = categoriaValidator;
        this.categoriaUpdater = categoriaUpdater;
    }

    @Override
    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        Categoria categoria = CategoriaMapper.toEntity(request);
        categoriaValidator.validarCategoriaDuplicadaAoCriar(categoria.getNome());
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return CategoriaMapper.toResponse(categoriaSalva);
    }

    @Override
    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
            .orElseThrow(() -> new CategoriaNaoEncontradaException(
                "Não existem categorias com este id cadastradas na base de dados"));
        Categoria categoriaComNovosDados = CategoriaMapper.toEntity(request);
        if (categoriaComNovosDados.getNome() != null) {
            categoriaValidator.validarCategoriaDuplicadaAoAtualizar(categoriaComNovosDados.getNome(), id);
        }
        categoriaUpdater.aplicar(categoriaExistente, categoriaComNovosDados);
        Categoria categoriaAtualizada = categoriaRepository.save(categoriaExistente);
        return CategoriaMapper.toResponse(categoriaAtualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(
                        "Não existem categorias para este ID cadastradas na base de dados!"));
        return CategoriaMapper.toResponse(categoriaExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> buscarPorNome(String nome) {
        List<Categoria> categorias = categoriaRepository.findAllByNomeContainingIgnoreCase(nome);
        return CategoriaMapper.toResponse(categorias);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodas() {
        List<Categoria> categorias = categoriaRepository.findAllByOrderByNomeAsc();
        return CategoriaMapper.toResponse(categorias);
    }
}
