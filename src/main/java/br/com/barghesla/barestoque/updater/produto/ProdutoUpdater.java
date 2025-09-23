package br.com.barghesla.barestoque.updater.produto;

import org.springframework.stereotype.Component;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;

@Component
public class ProdutoUpdater {
    public void aplicar(Produto existente, Produto novo) {
        if (novo.getNome() != null) existente.setNome(novo.getNome());
        if (novo.getDescricao() != null) existente.setDescricao(novo.getDescricao());
        if (novo.getQuantidade() != null) existente.setQuantidade(novo.getQuantidade());
        if (novo.getPrecoUnitario() != null) existente.setPrecoUnitario(novo.getPrecoUnitario());
        if (novo.getCategoria() != null) existente.setCategoria(novo.getCategoria());
        if (novo.getStatus() != null) existente.setStatus(novo.getStatus());
        if (existente.getStatus() == null) existente.setStatus(StatusProduto.ATIVO);
    }
}

