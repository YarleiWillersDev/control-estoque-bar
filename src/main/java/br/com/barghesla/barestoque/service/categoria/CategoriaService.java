package br.com.barghesla.barestoque.service.categoria;

import java.util.List;
import org.springframework.stereotype.Service;
import br.com.barghesla.barestoque.dto.categoria.CategoriaRequest;
import br.com.barghesla.barestoque.dto.categoria.CategoriaResponse;

@Service
public interface CategoriaService {
    public CategoriaResponse criar(CategoriaRequest request);
    public CategoriaResponse atualizar(Long id, CategoriaRequest request);
    public CategoriaResponse buscarPorId(Long id);
    public List<CategoriaResponse> buscarPorNome(String nome);
    public List<CategoriaResponse> listarTodas();
} 
    

