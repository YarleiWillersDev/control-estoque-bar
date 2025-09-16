package br.com.barghesla.barestoque.service.categoria;

import java.util.List;
import org.springframework.stereotype.Service;
import br.com.barghesla.barestoque.entity.Categoria;

@Service
public interface CategoriaService {

    public Categoria criar(Categoria categoria);
    public Categoria atualizar(Long id, Categoria categoria);
    public Categoria buscarPorId(Long id);
    public List<Categoria> buscarPorNome(String nome);
    public List<Categoria> listarTodas();

} 
    

