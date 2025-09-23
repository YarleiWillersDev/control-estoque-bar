package br.com.barghesla.barestoque.validation.produto;
import org.springframework.stereotype.Component;
import br.com.barghesla.barestoque.entity.Produto;
import br.com.barghesla.barestoque.entity.StatusProduto;
import br.com.barghesla.barestoque.exception.categoria.CategoriaNaoEncontradaException;
import br.com.barghesla.barestoque.exception.produto.PrecoInvalidoException;
import br.com.barghesla.barestoque.exception.produto.ProdutoJaAtivoException;
import br.com.barghesla.barestoque.exception.produto.ProdutoJaInativoException;
import br.com.barghesla.barestoque.exception.produto.QuantidadeInvalidaException;
import br.com.barghesla.barestoque.exception.usuario.NomeObrigatorioException;
import br.com.barghesla.barestoque.repository.CategoriaRepository;

@Component
public class ProdutoValidator {
    private final CategoriaRepository categoriaRepository;

    public ProdutoValidator(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public void validarCriacao(Produto p) {
        validarBasico(p);
        validarCategoriaObrigatoria(p);
    }

    public void validarAtualizacao(Produto existente, Produto novo) {
        // Validar apenas se veio no payload
        if (novo.getNome() != null) validarNome(novo);
        if (novo.getPrecoUnitario() != null) validarPreco(novo);
        if (novo.getQuantidade() != null) validarQuantidade(novo);
        if (novo.getCategoria() != null) validarCategoriaExistente(novo);
    }

    public void validarBasico(Produto p) {
        validarNome(p);
        validarPreco(p);
        validarQuantidade(p);
    }

    private void validarNome(Produto p) {
        if (p.getNome() == null || p.getNome().isBlank())
            throw new NomeObrigatorioException("Nome é obrigatório.");
    }

    private void validarPreco(Produto p) {
        if (p.getPrecoUnitario() == null || p.getPrecoUnitario().signum() < 0)
            throw new PrecoInvalidoException("Preço deve ser >= 0.");
    }

    public void validarQuantidade(Produto p) {
        if (p.getQuantidade() == null || p.getQuantidade() < 0)
            throw new QuantidadeInvalidaException("Quantidade deve ser >= 0.");
    }

    private void validarCategoriaObrigatoria(Produto p) {
        if (p.getCategoria() == null || p.getCategoria().getId() == null)
            throw new CategoriaNaoEncontradaException("Categoria é obrigatória.");
        validarCategoriaExistente(p);
    }

    private void validarCategoriaExistente(Produto p) {
        categoriaRepository.findById(p.getCategoria().getId())
           .orElseThrow(() -> new CategoriaNaoEncontradaException("Categoria inválida."));
    }

    public void validarAtivacao(Produto produto) {
        if (produto.getStatus() == StatusProduto.ATIVO)
            throw new ProdutoJaAtivoException("Produto já está ATIVO!");
    }

    public void validarInativacao(Produto produto) {
        if (produto.getStatus() == StatusProduto.INATIVO)
            throw new ProdutoJaInativoException("Produto já está INATIVO");
    }
}
