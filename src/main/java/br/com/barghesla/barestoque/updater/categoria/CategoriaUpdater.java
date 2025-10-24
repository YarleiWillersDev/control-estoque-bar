package br.com.barghesla.barestoque.updater.categoria;

import org.springframework.stereotype.Component;

import br.com.barghesla.barestoque.model.Categoria;

@Component
public class CategoriaUpdater {
    public void aplicar(Categoria existente, Categoria nova) {
        if (nova.getNome() != null) {
            existente.setNome(nova.getNome());
        }
    }
}
