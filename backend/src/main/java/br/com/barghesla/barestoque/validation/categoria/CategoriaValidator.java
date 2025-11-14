package br.com.barghesla.barestoque.validation.categoria;

import java.util.List;
import org.springframework.stereotype.Component;

import br.com.barghesla.barestoque.exception.categoria.CategoriaJaExistenteException;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
import br.com.barghesla.barestoque.model.Categoria;
import br.com.barghesla.barestoque.repository.CategoriaRepository;

@Component
public class CategoriaValidator {

    private final CategoriaRepository categoriaRepository;

    public CategoriaValidator(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public void validarCategoriaDuplicadaAoCriar(String nome) {
        categoriaRepository.findFirstByNomeIgnoreCase(nome)
            .ifPresent(c -> { throw new CategoriaJaExistenteException(
                "Já existe uma categoria com este nome cadastrada na base de dados!"); });
    }

    public void validarCategoriaDuplicadaAoAtualizar(String nome, Long idAtual) {
        categoriaRepository.findFirstByNomeIgnoreCase(nome)
            .ifPresent(c -> {
                if (!c.getId().equals(idAtual)) {
                    throw new CategoriaJaExistenteException(
                        "Já existe uma categoria com este nome cadastrada na base de dados!");
                }
            });
    }

    // Opcional: remova se optar por listas vazias
    public void validarListaVazia(List<Categoria> categorias) {
        validarListaVaziaInterno(categorias,
            "Não existe uma categoria com este nome cadastrada na base de dados!");
    }

    private void validarListaVaziaInterno(List<Categoria> categorias, String mensagem) {
        if (categorias == null || categorias.isEmpty()) {
            throw new CategoriaNaoEncontradaException(mensagem);
        }
    }
}

