package br.com.barghesla.barestoque.service.categoria;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.barghesla.barestoque.entity.Categoria;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
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
    public Categoria criar(Categoria categoria) {
        categoriaValidator.validarCategoriaDuplicadaAoCriar(categoria.getNome());
        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public Categoria atualizar(Long id, Categoria categoria) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(
                        "Não existem categorias com este id cadastradas na base de dados!"));

        if (categoria.getNome() != null) {
            categoriaValidator.validarCategoriaDuplicadaAoAtualizar(categoria.getNome(), id);
        }

        categoriaUpdater.aplicar(existente, categoria);

        return categoriaRepository.save(existente);
    }

    @Override
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(
                        "Não existem categorias para este ID cadastradas na base de dados!"));
    }

    @Override
    public List<Categoria> buscarPorNome(String nome) {
        List<Categoria> categorias = categoriaRepository.findAllByNomeContainingIgnoreCase(nome);
        // Se optar por retornar lista vazia, remova a linha abaixo
        categoriaValidator.validarListaVazia(categorias);
        return categorias;
    }

    @Override
    public List<Categoria> listarTodas() {
        List<Categoria> categorias = categoriaRepository.findAllByOrderByNomeAsc();
        // Se optar por retornar lista vazia, remova a linha abaixo
        categoriaValidator.validarListaVazia(categorias);
        return categorias;
    }

}
